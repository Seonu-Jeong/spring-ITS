package org.sparta.its.global.exception.errorcode;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserErrorCode {

	EXAMPLE(HttpStatus.BAD_REQUEST, "예시");

	private final HttpStatus httpStatus;
	private final String detail;
}