package org.sparta.its.domain.cancelList.dto;

import java.time.LocalDate;

import org.sparta.its.domain.cancelList.entity.CancelList;

import lombok.Builder;
import lombok.Getter;

public class CancelListResponse {
	@Getter
	@Builder
	public static class CancelListDtoRead{

		private final Long userId;

		private final String title;

		private final LocalDate date;

		private final String userEmail;

		private final String description;

		private final String status;

		public static CancelListDtoRead toDto(CancelList cancelList) {
			return CancelListDtoRead.builder()
				.userId(cancelList.getUser().getId())
				.title(cancelList.getConcertTitle())
				.date(cancelList.getConcertDate())
				.userEmail(cancelList.getUser().getEmail())
				.description(cancelList.getRejectComment())
				.status(cancelList.getStatus().toString())
				.build();
		}
	}
}
