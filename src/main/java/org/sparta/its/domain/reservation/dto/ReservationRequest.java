package org.sparta.its.domain.reservation.dto;

import org.sparta.its.domain.concert.entity.Concert;
import org.sparta.its.domain.hall.entity.Seat;
import org.sparta.its.domain.reservation.entity.Reservation;
import org.sparta.its.domain.reservation.entity.ReservationStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;


public class ReservationRequest{

	@Getter
	@Builder
	public static class SelectDto {
		@NotNull(message = "seatId 는 필수입니다.")
		private Long seatId;

		@NotNull(message = "concertId 는 필수입니다.")
		private Long concertId;

		public Reservation toEntity(Seat seat, Concert concert) {
			return Reservation.builder()
				.seat(seat)
				.concert(concert)
				.status(ReservationStatus.PENDING)
				.build();
		}
	}
}
