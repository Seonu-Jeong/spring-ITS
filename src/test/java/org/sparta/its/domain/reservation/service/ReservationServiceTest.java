package org.sparta.its.domain.reservation.service;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.sparta.its.domain.reservation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import com.mysema.commons.lang.Assert;

@SpringBootTest
@SqlGroup(
	{
		@Sql(value = "classpath:sql/concurrency/concurrencyDummyData.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(value = "classpath:sql/concurrency/concurrencyDeleteDummyData.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	}
)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class ReservationServiceTest {

	@Autowired
	private ReservationFacadeLockService testService;

	@Autowired
	private ReservationLockService reservationLockService;

	@Autowired
	private ReservationRepository reservationRepository;

	private final static Long USER_COUNT = 5L;
	private final static LocalDate TEST_DATE = LocalDate.parse("2070-12-15");

	@AfterEach
	public void deleteAllReservation() {
		reservationRepository.deleteAll();
	}

	@Test
	@Order(1)
	@DisplayName("파샤드 패턴으로 인한 동시성 제어")
	public void concurrencyTestNamedLockWithOuter() {

		IntStream.range(0, 20).parallel().forEach(i -> {

			long user = ThreadLocalRandom.current().nextLong(USER_COUNT) + 1;

			try {
				testService.testNamedLockV1(1L, 1L, TEST_DATE, user);
			} catch (Exception e) {
			}
		});
		Assert.isTrue(reservationRepository.count() == 1, "실패");
	}

	@Test
	@Order(2)
	@DisplayName("내부적 트랜잭션 처리를 통한 동시정제어")
	public void concurrencyTestNamedLockWithInner() {

		IntStream.range(0, 20).parallel().forEach(i -> {

			long user = ThreadLocalRandom.current().nextLong(USER_COUNT) + 5;

			try {
				reservationLockService.selectSeatWithInnerLock(1L, 1L, TEST_DATE, user);
			} catch (Exception e) {
			}
		});
		Assert.isTrue(reservationRepository.count() == 1, "실패");
	}
}
