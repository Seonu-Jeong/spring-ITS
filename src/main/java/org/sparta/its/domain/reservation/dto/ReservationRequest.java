package org.sparta.its.domain.reservation.dto;

import java.time.LocalDate;

import org.sparta.its.domain.cancelList.entity.CancelList;
import org.sparta.its.domain.cancelList.entity.CancelStatus;
import org.sparta.its.domain.user.entity.User;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class ReservationRequest {

	@Getter
	@RequiredArgsConstructor
	public static class CancelDto {

		private final String rejectComment;

		public CancelList toEntity(String concertTitle, Integer seatNum, LocalDate date,User user) {
			return CancelList.builder()
				.user(user)
				.rejectComment(rejectComment)
				.concertDate(date)
				.status(CancelStatus.REQUESTED)
				.concertTitle(concertTitle)
				.seatNum(seatNum).build();
		}
	}
}

