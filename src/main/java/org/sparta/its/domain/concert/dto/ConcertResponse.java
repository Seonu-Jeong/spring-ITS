package org.sparta.its.domain.concert.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.sparta.its.domain.concert.entity.Concert;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class ConcertResponse {

	@Getter
	@RequiredArgsConstructor
	@Builder
	public static class CreateDto {
		private final Long id;

		private final Long hallId;

		private final String title;

		private final String singer;

		@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
		private final LocalDateTime startAt;

		@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
		private final LocalDateTime endAt;

		@DateTimeFormat(pattern = "HH:mm")
		private final LocalTime runningStartTime;

		@DateTimeFormat(pattern = "HH:mm")
		private final LocalTime runningEndTime;

		private final Integer price;

		private final List<String> images;

		public static CreateDto toDto(Concert concert, List<String> imageUrls) {
			return CreateDto.builder()
				.id(concert.getId())
				.hallId(concert.getHall().getId())
				.title(concert.getTitle())
				.singer(concert.getSinger())
				.startAt(concert.getStartAt())
				.endAt(concert.getEndAt())
				.runningStartTime(concert.getRunningStartTime())
				.runningEndTime(concert.getRunningEndTime())
				.price(concert.getPrice())
				.images(imageUrls)
				.build();
		}
	}
}
