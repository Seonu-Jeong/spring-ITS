package org.sparta.its.domain.cancelList.dto;

import static org.sparta.its.domain.user.entity.QUser.*;

import org.sparta.its.domain.cancelList.entity.CancelList;

import lombok.Builder;
import lombok.Getter;

public class CancelListResponse {
	@Getter
	@Builder
	public static class CancelListDtoRead{

		private final Long userId;

		private final String title;

		private final String userEmail;

		private final String description;

		private final String status;

		public static CancelListDtoRead toDto(CancelList cancelList) {
			return CancelListDtoRead.builder()
				.userId(cancelList.getUser().getId())
				.title(cancelList.getConcertTitle())
				.userEmail(cancelList.getUser().getEmail())
				.description(cancelList.getRejectComment())
				.status(cancelList.getStatus().toString())
				.build();
		}
	}
}
