package org.sparta.its.domain.user.dto;

import org.sparta.its.domain.user.entity.Role;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class AuthResponse {

	@Getter
	@RequiredArgsConstructor
	public static class SignUpDto {

		private final Long id;

		private final String name;

		private final String email;

		private final String role;
	}

	@Getter
	@RequiredArgsConstructor
	public static class LoginDto {

		private final Long id;

		private final String name;

		private final String email;

		private final Role role;
	}

	@Getter
	@RequiredArgsConstructor
	public static class LogoutDto {

		private final Long id;

		private final String name;
	}
}