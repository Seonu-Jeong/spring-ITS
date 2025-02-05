package org.sparta.its.redis;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;
import org.sparta.its.domain.reservation.service.ReservationTestRedisService;
import org.sparta.its.domain.user.entity.User;
import org.sparta.its.domain.user.repository.UserRepository;
import org.sparta.its.global.exception.ReservationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;

@SpringBootTest
@SqlGroup({
	@Sql(
		value = {"/sql/domain/reservation/service/concurrency-setup.sql"},
		config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED),
		executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
	),
	@Sql(
		value = {"/sql/domain/reservation/service/concurrency-delete.sql"},
		config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED),
		executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
	)
})
public class RedisCacheReservationTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ReservationTestRedisService testRedisFacadeService;

	@Test
	public void cacheReservationTest() throws InterruptedException {

		//given
		List<User> users = userRepository.findAll();
		Long concertId = 1L;
		Long seatId = 1L;
		LocalDate date = LocalDate.of(2025, 5, 05);

		int numThreads = 10;
		CountDownLatch doneSignal = new CountDownLatch(numThreads);
		ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

		AtomicInteger successCount = new AtomicInteger();
		AtomicInteger failCount = new AtomicInteger();

		// when
		for (int i = 0; i < numThreads; i++) {
			int userIdx = i;
			executorService.execute(() -> {
				try {
					String key
						= testRedisFacadeService.lockSelectSeat(concertId, seatId, date, users.get(userIdx).getId());
					testRedisFacadeService.completeRedisReservation(key, users.get(userIdx).getId());
					successCount.getAndIncrement();
				} catch (ReservationException ex) {
					failCount.getAndIncrement();
				} finally {
					doneSignal.countDown();
				}
			});
		}
		doneSignal.await();
		executorService.shutdown();

		//then
		assertAll(
			() -> assertThat(successCount.get()).isEqualTo(1),
			() -> assertThat(failCount.get()).isEqualTo(9)
		);
	}
}
