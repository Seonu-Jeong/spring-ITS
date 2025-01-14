package org.sparta.its.domain.concertimage.dto;

import org.sparta.its.domain.concertimage.entity.ConcertImage;

import lombok.Builder;
import lombok.Getter;

public class ConcertImageResponse {

	@Getter
	@Builder
	public static class UpdateDto {

		private final Long concertId;

		private final Long concertImageId;

		private final String imageUrl;

		public static UpdateDto toDto(ConcertImage concertImage) {
			return UpdateDto.builder()
				.concertId(concertImage.getConcert().getId())
				.concertImageId(concertImage.getId())
				.imageUrl(concertImage.getImageUrl())
				.build();
		}
		
	}
}
