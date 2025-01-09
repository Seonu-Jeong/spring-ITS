package org.sparta.its.domain.reservation.service;

import java.util.Optional;

import org.sparta.its.domain.concert.entity.Concert;
import org.sparta.its.domain.concert.entity.service.ConcertService;
import org.sparta.its.domain.hall.entity.Seat;
import org.sparta.its.domain.hall.entity.service.SeatService;
import org.sparta.its.domain.cancelList.entity.CancelList;
import org.sparta.its.domain.cancelList.entity.CancelStatus;
import org.sparta.its.domain.reservation.entity.Reservation;
import org.sparta.its.domain.reservation.entity.ReservationStatus;
import org.sparta.its.domain.reservation.dto.ReservationResponseDto;
import org.sparta.its.domain.reservation.repository.ReservationRepository;
import org.sparta.its.domain.user.entity.User;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {

	private ReservationRepository reservationRepository;

	private SeatService seatService;

	private ConcertService concertService;

	private CancelListService cancelListService;

	@Override
	public ReservationResponseDto selectSeat(
		Long concertId,
		Long seatId) {
		//콘서트 조회
		Concert concert = concertService.findById(seatId)
			.orElseThrow(() -> new IllegalArgumentException("콘서트를 찾을 수 없습니다"));
		Seat seat = seatService.findById(seatId).orElseThrow(() -> new IllegalArgumentException("자리를 찾을 수 없습니다"));

		Optional<Reservation> existingReservation = reservationRepository.findReservationForSeatAndConcert(seatId,
			concertId, ReservationStatus.PENDING);

		if (existingReservation.isPresent()) {
			throw new IllegalStateException("이 자리는 이미 예약 되었습니다.");
		}

		Reservation reservation = Reservation.builder()
			.seat(seat)
			.concert(concert)
			.status(ReservationStatus.PENDING)
			.build();

		reservationRepository.save(reservation);

		return ReservationResponseDto.fromEntity(reservation);
	}

	@Override
	public ReservationResponseDto completeReservation(
		Long reservationId) {

		Reservation reservation = reservationRepository.findById(reservationId)
			.orElseThrow(() -> new IllegalArgumentException("해당 예약을 찾을 수 없습니다"));

		reservation.completeReservation();

		reservationRepository.save(reservation);

		return ReservationResponseDto.fromEntity(reservation);
	}

	@Override
	public void cancelReservation(
		Long reservationId,
		String description) {

		Reservation reservation = reservationRepository.findById(reservationId)
			.orElseThrow(() -> new IllegalArgumentException("해당 예약을 찾을 수 없습니다"));

		if (reservation.getStatus() != ReservationStatus.COMPLETED) {
			throw new IllegalStateException("완료되지 않은 예약은 취소할 수 없습니다.");
		}

		// 예약 상태를 취소로 변경
		reservation.cancelReservation();
		reservationRepository.save(reservation);

		// 취소 내역 저장
		User user = reservation.getUser();
		CancelList cancelList = CancelList.builder()
			.user(user)
			.description(description)
			.status(CancelStatus.REQUESTED)
			.build();

		cancelListService.save(cancelList);
	}
}
