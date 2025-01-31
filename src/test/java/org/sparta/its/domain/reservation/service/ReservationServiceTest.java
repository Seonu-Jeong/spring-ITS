package org.sparta.its.domain.reservation.service;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sparta.its.domain.reservation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ReservationServiceTest {

	@Autowired
	private TestService testService;

	@Autowired
	private ReservationLockService reservationLockService;

	@Autowired
	private ReservationRepository reservationRepository;

	private final static Long USER_COUNT = 5L;
	private static final LocalDate TEST_DATE = LocalDate.parse("2025-12-15");

	@BeforeEach
	public void createConcert() {
		
	}

	@AfterEach
	public void deleteAllReservation() {
		reservationRepository.deleteAll();
	}

	@Test
	@DisplayName("내부적 트랜잭션 처리를 통한 동시정제어")
	public void concurrencyTestNamedLockWithInner() {

		IntStream.range(0, 20).parallel().forEach(i -> {

			long user = ThreadLocalRandom.current().nextLong(USER_COUNT) + 1;

			reservationLockService.selectSeatWithInnerLock(1L, 1L, TEST_DATE, user);

		});
	}

	@Test
	@DisplayName("파샤드 패턴으로 인한 동시성 제어")
	public void concurrencyTestNamedLockWithOuter() {
		IntStream.range(0, 20).parallel().forEach(i -> {

			long user = ThreadLocalRandom.current().nextLong(USER_COUNT) + 1;

			testService.testNamedLockV1(1L, 1L, TEST_DATE, user);
		});
	}
}
