package org.sparta.its.domain.reservation.dto;

import java.time.LocalDate;
import java.time.LocalTime;

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

		private final LocalDate concertDate;

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

		private final Integer seatNumber;

		private final String status;

		public static CompleteDto toDto(Reservation reservation) {
			return CompleteDto.builder()
				.reservationId(reservation.getId())
				.userName(reservation.getUser().getName())
				.concertTitle(reservation.getConcert().getTitle())
				.seatNumber(reservation.getSeat().getSeatNumber())
				.status(reservation.getStatus().name())
				.build();
		}
	}

	@Getter
	@Builder
	public static class CancelDto {

		private final Long reservationId;

		private final Integer seatNumber;

		public static CancelDto toDto(Reservation reservation) {
			return CancelDto.builder()
				.reservationId(reservation.getId())
				.seatNumber(reservation.getSeat().getSeatNumber())
				.build();
		}
	}

	@Getter
	@Builder
	public static class ReservationListDto {

		private final Long concertId;

		private final String hallName;

		private final String concertTitle;

		private final LocalDate startAt;

		private final LocalDate endAt;

		private final LocalTime runningStartTime;

		private final LocalTime runningEndTime;

		private final Integer price;

		public static ReservationListDto toDto(Reservation reservation) {
			return ReservationListDto.builder()
				.concertId(reservation.getConcert().getId())
				.hallName(reservation.getConcert().getHall().getName())
				.concertTitle(reservation.getConcert().getTitle())
				.startAt(reservation.getConcert().getStartAt())
				.endAt(reservation.getConcert().getEndAt())
				.runningStartTime(reservation.getConcert().getRunningStartTime())
				.runningEndTime(reservation.getConcert().getRunningEndTime())
				.price(reservation.getConcert().getPrice())
				.build();
		}
	}
}
