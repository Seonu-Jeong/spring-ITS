package org.sparta.its.domain.hall.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.sparta.its.domain.hall.entity.Hall;
import org.sparta.its.domain.hall.entity.HallImage;

import lombok.Builder;
import lombok.Getter;

public class HallResponse {

	@Getter
	public static class CreatDto {
		private final Long hallId;

		private final String hallName;

		private final String location;

		private final Integer capacity;

		private final LocalDateTime createdAt;

		private final List<String> images;

		public CreatDto(Hall savedHall, List<String> publicUrl) {
			this.hallId = savedHall.getId();
			this.hallName = savedHall.getName();
			this.location = savedHall.getLocation();
			this.capacity = savedHall.getCapacity();
			this.createdAt = savedHall.getCreatedAt();
			this.images = publicUrl;
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

	@Getter
	public static class ReadDetailDto {
		private final Long hallId;

		private final String hallName;

		private final String location;

		private final Integer capacity;

		private final LocalDateTime createdAt;

		private final LocalDateTime modifiedAt;

		private final List<String> imageUrls;

		private final Boolean isOpen;

		public ReadDetailDto(Long id, String name, String location, Integer capacity, LocalDateTime createdAt,
			LocalDateTime modifiedAt,
			List<HallImage> imageUrl,
			boolean isOpen) {
			this.hallId = id;
			this.hallName = name;
			this.location = location;
			this.capacity = capacity;
			this.createdAt = createdAt;
			this.modifiedAt = modifiedAt;
			this.imageUrls = imageUrl.stream().map(HallImage::getImageUrl).toList();
			this.isOpen = isOpen;
		}

		public ReadDetailDto(Hall hall) {
			this.hallId = hall.getId();
			this.hallName = hall.getName();
			this.location = hall.getLocation();
			this.capacity = hall.getCapacity();
			this.createdAt = hall.getCreatedAt();
			this.modifiedAt = hall.getModifiedAt();
			this.imageUrls = hall.getHallImages().stream().map(HallImage::getImageUrl).toList();
			this.isOpen = hall.getIsOpen();
		}

	}
}
