package org.sparta.its.domain.reservation.service;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sparta.its.domain.user.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Transactional
@Slf4j
class ReservationServiceTest {

	@Autowired
	private TestService testService;

	@Autowired
	private ReservationLockService reservationLockService;

	@Autowired
	private UserService userService;

	@AfterEach
	public void deleteAllReservation() {
		// reservationRepository.deleteAll();
	}

	@Test
	@DisplayName("내부적 트랜잭션 처리를 통한 동시정제어")
	public void concurrencyTestNamedLockWithInner() {

		IntStream.range(0, 20).parallel().forEach(i -> {

			long concert = ThreadLocalRandom.current().nextLong(3L) + 1;
			long seat = ThreadLocalRandom.current().nextLong(5L) + 1;
			LocalDate date = LocalDate.parse("2025-12-15");
			long user = ThreadLocalRandom.current().nextLong(10L) + 1;

			log.info("{}/{}/{}", concert, seat, user);
			try {
				reservationLockService.selectSeatWithInnerLock(1L, 1L, date, user);
			} catch (Exception e) {

			}
		});
	}

	@Test
	@DisplayName("외부적 트랜잭션 처리를 통한 동시정제어 (큐방식)")
	public void concurrencyTestNamedLockWithOuter() {
		IntStream.range(0, 20).parallel().forEach(i -> {

			// long concert = ThreadLocalRandom.current().nextLong(3L) + 1;
			long seat = ThreadLocalRandom.current().nextLong(5L) + 1;
			LocalDate date = LocalDate.parse("2025-12-15");
			long user = ThreadLocalRandom.current().nextLong(4L) + 1;

			// log.info("{}/{}/{}", co ncert, seat, user);

			testService.testNamedLockV1(1L, 1L, date, user);
		});
	}
}
