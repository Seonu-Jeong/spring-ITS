package org.sparta.its.domain.concert.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

import org.sparta.its.domain.concert.entity.Concert;
import org.sparta.its.domain.concert.entity.ConcertImage;
import org.sparta.its.domain.hall.entity.Hall;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class ConcertRequest {

	@Getter
	@RequiredArgsConstructor
	public static class CreateDto {

		@NotNull(message = "공연장 고유 식발자는 필수값 입니다.")
		private final Long hallId;

		@NotBlank(message = "콘서트 제목은 필수값 입니다.")
		private final String title;

		@NotBlank(message = "가수 이름은 필수값 입니다.")
		private final String singer;

		@NotNull(message = "공연 시작 날짜 필수값 입니다.")
		@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
		private final LocalDateTime startAt;

		@NotNull(message = "공연 시작 날짜 필수값 입니다.")
		@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
		private final LocalDateTime endAt;

		@NotNull(message = "공연 시작 시간 필수값 입니다.")
		@DateTimeFormat(pattern = "HH:mm")
		private final LocalTime runningStartTime;

		@NotNull(message = "공연 시작 날짜 필수값 입니다.")
		@DateTimeFormat(pattern = "HH:mm")
		private final LocalTime runningEndTime;

		@NotNull(message = "공연 가격은 필수값 입니다.")
		private final Integer price;

		@NotNull(message = "images 는 필수값 입니다.")
		private final MultipartFile[] images;

		public Concert toEntity(Hall hall) {
			return Concert.builder()
				.hall(hall)
				.title(title)
				.singer(singer)
				.startAt(startAt)
				.endAt(endAt)
				.runningStartTime(runningStartTime)
				.runningEndTime(runningEndTime)
				.price(price)
				.concertImages(Arrays.stream(images).map(image -> new ConcertImage()).toList())
				.build();
		}
	}
}
