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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService{

	private final ReservationRepository reservationRepository;
	private final SeatRepository seatRepository;
	private final ConcertRepository concertRepository;
	/**
	 * 좌석 선택
	 *
	 * @param selectDto 선택 요청 Dto
	 * @return ReservationResponse.SelectDto 선택된 좌석 예약 정보
	 */
	@Transactional
	public ReservationResponse.SelectDto selectSeat(
		ReservationRequest.SelectDto selectDto) {
		// 콘서트 조회
		Concert concert = concertRepository.findById(selectDto.getConcertId())
			.orElseThrow(()-> new IllegalArgumentException("해당 콘서트를 찾을 수 없습니다"));
		// 좌석 조회
		Seat seat = seatRepository.findById(selectDto.getSeatId())
			.orElseThrow(()-> new IllegalArgumentException("해당 좌석을 찾을 수 없습니다"));
		// 예약 가능 여부 확인
		Optional<Reservation> existingReservation = reservationRepository
			.findReservationForSeatAndConcert(selectDto.getConcertId(), selectDto.getSeatId(), ReservationStatus.PENDING);

		if (existingReservation.isPresent()) {
			throw new IllegalStateException("이 자리는 이미 예약되었습니다.");
		}

		// 예약 생성
		Reservation reservation = selectDto.toEntity(seat, concert);

		reservationRepository.save(reservation);

		return ReservationResponse.SelectDto.toDto(reservation);
	}
}
