package org.sparta.its.domain.concert.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.sparta.its.domain.concert.entity.Concert;
import org.sparta.its.domain.concert.entity.ConcertImage;
import org.sparta.its.domain.hall.entity.Hall;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;

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

		public Concert toEntity(Hall hall, List<MultipartFile> images) {
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

}
