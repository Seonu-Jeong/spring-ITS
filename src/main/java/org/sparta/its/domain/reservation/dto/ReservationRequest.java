package org.sparta.its.domain.reservation.dto;

import org.sparta.its.domain.reservation.entity.Reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class ReservationRequest {
	@AllArgsConstructor
	@Getter
	public static class CancelDto {
		private String rejectComment;

		public Reservation toEntity(Reservation reservation) {
			return Reservation.builder()
				.rejectComment(rejectComment)
				.build();
		}
	}
}

