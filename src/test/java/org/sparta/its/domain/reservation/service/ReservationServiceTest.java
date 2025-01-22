package org.sparta.its.domain.reservation.service;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class ReservationServiceTest {

	@Autowired
	private TestService testService;

	@Autowired
	private ReservationService reservationService;

	@Test
	public void concurrencyTestNamedLock() {
		IntStream.range(0, 10).parallel().forEach(i -> {

			long concert = ThreadLocalRandom.current().nextLong(5L) + 1;
			long seat = ThreadLocalRandom.current().nextLong(100L) + 1;
			LocalDate date = LocalDate.parse("2025-12-15");
			long user = ThreadLocalRandom.current().nextLong(4L) + 1;

			// log.warn("{}/{}/{}", concert, seat, user);
			// testService.testNamedLockV2(1L, seat, date, user);
			reservationService.selectSeat(1L, 1L, date, user);
			// testService.test(1L, 1L, date, 1L);
		});
	}
}
