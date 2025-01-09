package org.sparta.its.domain.hall.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.sparta.its.domain.hall.entity.Hall;

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

	@Getter
	public static class ReadDto {
		private final Long hallId;

		private final String hallName;

		private final String location;

		private final Integer capacity;

		private final LocalDateTime createdAt;

		private final LocalDateTime modifiedAt;

		private final String imageUrl;

		private final Boolean isOpen;

		public ReadDto(Long id, String name, String location, Integer capacity, LocalDateTime createdAt,
			LocalDateTime modifiedAt,
			String imageUrl,
			boolean isOpen) {
			this.hallId = id;
			this.hallName = name;
			this.location = location;
			this.capacity = capacity;
			this.createdAt = createdAt;
			this.modifiedAt = modifiedAt;
			this.imageUrl = imageUrl;
			this.isOpen = isOpen;
		}
	}
}
