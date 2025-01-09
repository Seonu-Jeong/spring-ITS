package org.sparta.its.domain.concert.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.sparta.its.domain.concert.entity.Concert;
import org.sparta.its.domain.concert.entity.ConcertImage;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class ConcertResponse {

	@Getter
	@RequiredArgsConstructor
	@Builder
	public static class ResponseDto {
		private final Long id;

		private final Long hallId;

		private final String title;

		private final String singer;

		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
		private final LocalDateTime startAt;

		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
		private final LocalDateTime endAt;

		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
		private final LocalTime runningStartTime;

		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
		private final LocalTime runningEndTime;

		private final Integer price;

		private final List<String> images;

		public static ResponseDto toDto(Concert concert) {
			return ResponseDto.builder()
				.id(concert.getId())
				.hallId(concert.getHall().getId())
				.title(concert.getTitle())
				.singer(concert.getSinger())
				.startAt(concert.getStartAt())
				.endAt(concert.getEndAt())
				.runningStartTime(concert.getRunningStartTime())
				.runningEndTime(concert.getRunningEndTime())
				.price(concert.getPrice())
				.images(concert.getConcertImages().stream().map(ConcertImage::getImageUrl).toList())
				.build();
		}
	}
}
