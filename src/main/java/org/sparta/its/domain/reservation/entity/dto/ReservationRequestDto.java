package org.sparta.its.domain.reservation.entity.dto;

import org.sparta.its.domain.concert.entity.Concert;
import org.sparta.its.domain.hall.entity.Seat;
import org.sparta.its.domain.reservation.entity.entity.Reservation;
import org.sparta.its.domain.reservation.entity.entity.ReservationStatus;
import org.sparta.its.domain.user.entity.User;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReservationRequestDto {

	private Long seatId;
	private Long userId;
	private Long concertId;

	public Reservation toEntity(Seat seat, User user, Concert concert) {
		return Reservation.builder()
			.seat(seat)
			.user(user)
			.concert(concert)
			.status(ReservationStatus.PENDING)
			.build();
	}
}
