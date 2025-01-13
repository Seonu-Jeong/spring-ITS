package org.sparta.its.domain.reservation.dto;

import java.time.LocalDateTime;

import org.sparta.its.domain.hall.dto.HallResponse;
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

		private final LocalDateTime concertDate;

		public static SelectDto toDto(Reservation reservation) {
			return SelectDto.builder()
				.reservationId(reservation.getId())
				.seatId(reservation.getSeat().getId())
				.status(reservation.getStatus().name())
				.concertTitle(reservation.getConcert().getTitle())
				.concertDate(reservation.getConcert().getStartAt())
				.build();
		}
	}

	@Getter
	@Builder
	public static class CompleteDto {
		private final Long reservationId;

		private final String userName;

		private final String concertTitle;

		private final int seatNumber;

		private final String status;

		public static CompleteDto toDto(Reservation reservation) {
			return CompleteDto.builder()
				.reservationId(reservation.getId())
				// .userName(reservation.getUser().getName())
				.concertTitle(reservation.getConcert().getTitle())
				.seatNumber(reservation.getSeat().getSeatNumber())
				.status(reservation.getStatus().name())
				.build();
		}
	}
}
