package org.sparta.its.domain.concertimage.dto;

import org.sparta.its.global.s3.ImageFormat;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class ConcertImageRequest {

	@Getter
	@RequiredArgsConstructor
	public static class UpdateDto {

		@NotNull(message = "imageFormat 은 필수값 입니다.")
		private final ImageFormat imageFormat;

		@NotNull(message = "images 는 필수값 입니다.")
		private final MultipartFile[] images;
	}
}
