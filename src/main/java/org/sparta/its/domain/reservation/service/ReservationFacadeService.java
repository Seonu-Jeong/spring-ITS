package org.sparta.its.domain.reservation.service;

import static org.sparta.its.global.exception.errorcode.ReservationErrorCode.*;

import java.time.LocalDate;

import org.sparta.its.domain.reservation.dto.ReservationResponse;
import org.sparta.its.domain.reservation.repository.ReservationRepository;
import org.sparta.its.global.exception.ReservationException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * create on 2025. 02. 02.
 * create by IntelliJ IDEA.
 *
 * 예약 관련 FacadeService.
 *
 * @author Seonu-Jeong, TaeHyeon Kim
 */
@Service
@RequiredArgsConstructor
public class ReservationFacadeService {

	private final ReservationService reservationService;
	private final ReservationRepository reservationRepository;

	/**
	 * 좌석 선택
	 *
	 * @param concertId 콘서트 고유 식별자
	 * @param seatId 좌석 고유 식별자
	 * @param date 공연 날짜
	 * @param userId 유저 고유 식별자
	 * @return {@link ReservationResponse.SelectDto}
	 */
	public ReservationResponse.SelectDto selectSeat(Long concertId, Long seatId, LocalDate date, Long userId) {
		String key = keyGenerator(concertId, seatId, date);

		Boolean isGetLock = null;

		ReservationResponse.SelectDto selectDto = null;

		try {
			isGetLock = reservationRepository.getLock(key) == 1;

			if (isGetLock) {
				selectDto = reservationService.selectSeat(concertId, seatId, date, userId);
			} else {
				throw new ReservationException(RETRY_REQUESET);
			}
		} finally {
			if (isGetLock) {
				reservationRepository.releaseLock(key);
			}
		}

		return selectDto;
	}

	private String keyGenerator(Long concertId, Long seatId, LocalDate date) {
		return concertId + "/" + seatId + "/" + date;
	}

}