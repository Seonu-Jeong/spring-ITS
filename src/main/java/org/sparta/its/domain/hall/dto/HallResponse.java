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
}
