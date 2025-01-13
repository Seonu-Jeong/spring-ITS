package org.sparta.its.domain.reservation.dto;

import org.sparta.its.domain.reservation.entity.Reservation;

import lombok.Getter;

public class ReservationRequest {
	@AllArgsConstructor
	@Getter
	public static class CancelDto {

		private final String rejectComment;

		public CancelList toEntity(String concertTitle, Integer seatNum, User user) {
			return CancelList.builder()
				.user(user)
				.rejectComment(rejectComment)
				.status(CancelStatus.REQUESTED)
				.concertTitle(concertTitle)
				.seatNum(seatNum).build();
		}
	}
}

