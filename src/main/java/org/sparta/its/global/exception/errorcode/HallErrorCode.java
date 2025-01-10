package org.sparta.its.global.exception.errorcode;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HallErrorCode {

	// 400 BAD REQUEST

	// 404 NOT FOUND
	NOT_FOUND_HALL(HttpStatus.NOT_FOUND, "공연장을 찾을 수 없습니다.");

	private final HttpStatus httpStatus;
	private final String detail;
}
