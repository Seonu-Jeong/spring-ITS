package org.sparta.its.domain.reservation.service;

import java.util.Optional;

import org.sparta.its.domain.concert.entity.Concert;
import org.sparta.its.domain.concert.repository.ConcertRepository;
import org.sparta.its.domain.hall.entity.Seat;
import org.sparta.its.domain.hall.repository.SeatRepository;
import org.sparta.its.domain.reservation.dto.ReservationRequest;
import org.sparta.its.domain.reservation.entity.Reservation;
import org.sparta.its.domain.reservation.entity.ReservationStatus;
import org.sparta.its.domain.reservation.dto.ReservationResponse;
import org.sparta.its.domain.reservation.repository.ReservationRepository;
import org.sparta.its.global.exception.ConcertException;
import org.sparta.its.global.exception.HallException;
import org.sparta.its.global.exception.ImageException;
import org.sparta.its.global.exception.ReservationException;
import org.sparta.its.global.exception.errorcode.ConcertErrorCode;
import org.sparta.its.global.exception.errorcode.HallErrorCode;
import org.sparta.its.global.exception.errorcode.ImageErrorCode;
import org.sparta.its.global.exception.errorcode.ReservationErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {

	private final ReservationRepository reservationRepository;
	private final SeatRepository seatRepository;
	private final ConcertRepository concertRepository;

	/**
	 * 좌석 선택
	 *
	 * @param concertId 콘서트 아이디
	 * @param seatId 좌석 아이디
	 * @return ReservationResponse.SelectDto 선택된 좌석 예약 정보
	 */
	@Transactional
	public ReservationResponse.SelectDto selectSeat(Long seatId, Long concertId) {
		// 콘서트 조회
		Concert concert = concertRepository.findByIdOrThrow(concertId);
		// 좌석 조회
		Seat seat = seatRepository.findByIdOrThrow(seatId);
		// 예약 가능 여부 확인
		Optional<Reservation> existingReservation = reservationRepository
			.findReservationForSeatAndConcert(seat, concert, ReservationStatus.PENDING);

		if (existingReservation.isPresent()) {
			throw new ReservationException(ReservationErrorCode.ALREADY_BOOKED);
		}

		// 예약 생성
		Reservation reservation = Reservation.builder()
			.seat(seat)
			.concert(concert)
			.status(ReservationStatus.PENDING)
			.build();

		reservationRepository.save(reservation);

		return ReservationResponse.SelectDto.toDto(reservation);
	}
}
