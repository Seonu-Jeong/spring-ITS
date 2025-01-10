package org.sparta.its.domain.hall.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.sparta.its.domain.hall.entity.Hall;
import org.sparta.its.domain.hall.entity.HallImage;

import lombok.Builder;
import lombok.Getter;

public class HallResponse {

	@Builder
	@Getter
	public static class CreatDto {
		private final Long hallId;

		private final String hallName;

		private final String location;

		private final Integer capacity;

		private final LocalDateTime createdAt;

		private final List<String> imageUrls;

		public static CreatDto toDto(Hall savedHall, List<String> publicUrl) {
			return CreatDto.builder()
				.hallId(savedHall.getId())
				.hallName(savedHall.getName())
				.location(savedHall.getLocation())
				.capacity(savedHall.getCapacity())
				.createdAt(savedHall.getCreatedAt())
				.imageUrls(publicUrl)
				.build();
		}
	}

	@Builder
	@Getter
	public static class ReadDto {
		private final Long hallId;

		private final String hallName;

		private final String location;

		private final Integer capacity;

		private final LocalDateTime createdAt;

		private final LocalDateTime modifiedAt;

		private final List<String> imageUrls;

		private final Boolean isOpen;

		public static ReadDto toDto(Hall hall) {
			return ReadDto.builder()
				.hallId(hall.getId())
				.hallName(hall.getName())
				.location(hall.getLocation())
				.capacity(hall.getCapacity())
				.createdAt(hall.getCreatedAt())
				.modifiedAt(hall.getModifiedAt())
				.imageUrls(hall.getHallImages().stream().map(HallImage::getImageUrl).toList())
				.isOpen(hall.getIsOpen())
				.build();
		}
	}
}
