package org.sparta.its.domain.reservation.dto;

import org.sparta.its.domain.reservation.entity.Reservation;

import lombok.Builder;
import lombok.Getter;

public class ReservationResponse {
	@Getter
	@Builder
	public static class SelectDto {
		private final Long reservationId;

		private final Long seatId;

		private final String status;

		private final String concertTitle;

		private final String concertDate;

		public static SelectDto toDto(Reservation reservation) {
			return SelectDto.builder()
				.reservationId(reservation.getId())
				.seatId(reservation.getSeat().getId())
				.status(reservation.getStatus().name())
				.concertTitle(reservation.getConcert().getTitle())
				.concertDate(reservation.getConcert().getStartAt().toString())
				.build();
		}
	}
}
