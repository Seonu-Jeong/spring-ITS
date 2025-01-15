package org.sparta.its.domain.user.dto;

import org.sparta.its.domain.user.entity.User;

import lombok.Builder;
import lombok.Getter;

public class UserResponse {

	@Getter
	@Builder
	public static class UpdateDto {

		private final Long id;

		private final String email;

		private final String name;

		private final String phoneNumber;

		public static UpdateDto toDto(User user) {
			return UpdateDto.builder()
				.id(user.getId())
				.email(user.getEmail())
				.name(user.getName())
				.phoneNumber(user.getPhoneNumber())
				.build();
		}
	}

	@Getter
	@Builder
	public static class DeleteDto {

		private final String message;

		public static DeleteDto toDto(String message) {
			return DeleteDto.builder().message(message).build();
		}
	}
}