package org.sparta.its.domain.user.entity;

import static org.sparta.its.global.exception.errorcode.UserErrorCode.*;

import org.sparta.its.global.exception.UserException;

public enum Role {
	ADMIN, USER;

	public static Role of(String roleName) throws IllegalArgumentException {

		for (Role role : values()) {
			if (role.name().equals(roleName)) {
				return role;
			}
		}

		throw new UserException(ILLEGAL_ROLE);
	}
}