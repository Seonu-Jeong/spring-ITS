package org.sparta.its.domain.concert.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.sparta.its.domain.concert.entity.Concert;
import org.sparta.its.domain.concert.entity.ConcertImage;
import org.sparta.its.domain.hall.entity.Hall;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class CreateConcert {

	@Getter
	@RequiredArgsConstructor
	public static class RequestDto {

		@NotNull(message = "공연장 고유 식발자는 필수값 입니다.")
		private final Long hallId;

		@NotBlank(message = "콘서트 제목은 필수값 입니다.")
		private final String title;

		@NotBlank(message = "가수 이름은 필수값 입니다.")
		private final String singer;

		@NotNull(message = "공연 시작 날짜 필수값 입니다.")
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
		private final LocalDateTime startAt;

		@NotNull(message = "공연 시작 날짜 필수값 입니다.")
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
		private final LocalDateTime endAt;

		@NotNull(message = "공연 시작 시간 필수값 입니다.")
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
		private final LocalTime runningStartTime;

		@NotNull(message = "공연 시작 날짜 필수값 입니다.")
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
		private final LocalTime runningEndTime;

		@NotNull(message = "공연 가격은 필수값 입니다.")
		private final Integer price;

		public Concert toEntity(Hall hall, List<String> images) {
			return Concert.builder()
				.hall(hall)
				.title(title)
				.singer(singer)
				.startAt(startAt)
				.endAt(endAt)
				.runningStartTime(runningStartTime)
				.runningEndTime(runningEndTime)
				.price(price)
				.concertImages(images.stream().map(image -> new ConcertImage()).toList())
				.build();
		}
	}

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
