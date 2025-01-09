package org.sparta.its.domain.reservation.dto;

import org.sparta.its.domain.reservation.entity.Reservation;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReservationResponseDto {
	private Long reservationId;
	private Long seatId;
	private Long userId;
	private String status;
	private String concertName;
	private String concertDate;

	public static ReservationResponseDto fromEntity(Reservation reservation) {
		return ReservationResponseDto.builder()
			.reservationId(reservation.getReservationId())
			.seatId(reservation.getSeat().getSeatId())
			.userId(reservation.getUser().getUserId())
			.status(reservation.getStatus().name())
			.concertName(reservation.getConcert().getName())
			.concertDate(reservation.getConcert().getDate().toString())
			.build();
	}
}
