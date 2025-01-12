package org.sparta.its.domain.hall.dto.hallImage;

import org.sparta.its.domain.hall.entity.HallImage;

import lombok.Builder;
import lombok.Getter;

public class HallImageResponse {

	@Builder
	@Getter
	public static class UpdateDto {
		private final Long hallId;

		private final Long hallImageId;

		private final String imageUrl;

		public static HallImageResponse.UpdateDto toDto(HallImage hallImage) {
			return UpdateDto.builder()
				.hallId(hallImage.getHall().getId())
				.hallImageId(hallImage.getId())
				.imageUrl(hallImage.getImageUrl())
				.build();
		}
	}
}
