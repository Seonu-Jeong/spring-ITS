package org.sparta.its.domain.hall.dto;

import org.sparta.its.domain.hall.entity.Hall;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class HallRequest {

	@AllArgsConstructor
	@Getter
	public static class CreateDto {

		@NotBlank(message = "name 은 필수입니다.")
		private String name;

		@NotBlank(message = "location 은 필수입니다.")
		private String location;

		@NotNull(message = "capacity 은 필수입니다.")
		@Positive(message = "수용 인원은 1명 이상으로 적어주세요")
		private Integer capacity;

		@NotNull(message = "images 는 필수입니다.")
		private MultipartFile[] images;

		public Hall toEntity() {
			return Hall.builder()
				.name(name)
				.location(location)
				.capacity(capacity)
				.isOpen(true)
				.build();
		}
	}
}
