package org.sparta.its.domain.reservation.entity.service;

import java.util.Optional;

import org.sparta.its.domain.concert.entity.Concert;
import org.sparta.its.domain.concert.entity.service.ConcertService;
import org.sparta.its.domain.hall.entity.Seat;
import org.sparta.its.domain.hall.entity.service.SeatService;
import org.sparta.its.domain.reservation.entity.entity.Reservation;
import org.sparta.its.domain.reservation.entity.entity.ReservationStatus;
import org.sparta.its.domain.reservation.entity.dto.ReservationResponseDto;
import org.sparta.its.domain.reservation.entity.repository.ReservationRepository;
import org.sparta.its.domain.user.Service.UserService;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {

	private ReservationRepository reservationRepository;

	private SeatService seatService;

	private UserService userService;

	private ConcertService concertService;

	@Override
	public ReservationResponseDto selectSeat(Long concertId, Long seatId){
		//콘서트 조회
		Concert concert = concertService.findById(seatId).orElseThrow(()-> new IllegalArgumentException("콘서트를 찾을 수 없습니다"));
		Seat seat = seatService.findById(seatId).orElseThrow(()-> new IllegalArgumentException("자리를 찾을 수 없습니다"));

		Optional<Reservation> existingReservation = reservationRepository.findReservationForSeatAndConcert(seatId, concertId, ReservationStatus.PENDING);

		if(existingReservation.isPresent()){
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
}
