package org.sparta.its.domain.hallImage.dto;

import org.sparta.its.global.s3.ImageFormat;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class HallImageRequest {

	@AllArgsConstructor
	@Getter
	public static class UpdateImageDto {

		@NotNull(message = "imageFormat 는 필수입니다.")
		private ImageFormat imageFormat;

		@NotNull(message = "images 는 필수입니다.")
		private MultipartFile[] images;

	}
}
