package org.sparta.its.domain.reservation.service;

import java.time.LocalDate;

import org.sparta.its.domain.reservation.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * create on 2025. 01. 25.
 * create by IntelliJ IDEA.
 *
 * 예약 관련 Service.
 *
 * @author Jun Heo
 */
@Service
@RequiredArgsConstructor
public class TestService {

	private final ReservationService reservationService;
	private final ReservationRepository reservationRepository;

	public void testNamedLockV1(Long concertId, Long seatId, LocalDate date, Long userId) {
		String key = keyGenerator(concertId, seatId, date);
		try {
			reservationRepository.getLock(key);
			reservationService.selectSeat(concertId, seatId, date, userId);
		} catch (Exception e) {

		} finally {
			reservationRepository.releaseLock(key);
		}
	}

	private String keyGenerator(Long concertId, Long seatId, LocalDate date) {

		return concertId + "/" + seatId + "/" + date;
	}
}
