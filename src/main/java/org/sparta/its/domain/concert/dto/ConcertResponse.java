package org.sparta.its.domain.concert.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.sparta.its.domain.concert.entity.Concert;
import org.sparta.its.domain.concertimage.entity.ConcertImage;

import lombok.Builder;
import lombok.Getter;

public class ConcertResponse {

	@Getter
	@Builder
	public static class CreateDto {
		private final Long id;

		private final Long hallId;

		private final String title;

		private final String singer;

		private final LocalDateTime startAt;

		private final LocalDateTime endAt;

		private final LocalTime runningStartTime;

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

	@Getter
	@Builder
	public static class ReadDto {
		private final Long id;

		private final String hallName;

		private final String title;

		private final String singer;

		private final LocalDateTime startAt;

		private final LocalDateTime endAt;

		private final LocalTime runningStartTime;

		private final LocalTime runningEndTime;

		private final Integer price;

		private final List<String> images;

		public static ReadDto toDto(Concert concert) {
			return ReadDto.builder()
				.id(concert.getId())
				.hallName(concert.getHall().getName())
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

	@Getter
	@Builder
	public static class UpdateDto {

		private final Long id;

		private final String hallName;

		private final String title;

		private final String singer;

		private final LocalDateTime startAt;

		private final LocalDateTime endAt;

		private final LocalTime runningStartTime;

		private final LocalTime runningEndTime;

		private final Integer price;

		private final List<String> images;

		public static UpdateDto toDto(Concert concert) {
			return UpdateDto.builder()
				.id(concert.getId())
				.hallName(concert.getHall().getName())
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
