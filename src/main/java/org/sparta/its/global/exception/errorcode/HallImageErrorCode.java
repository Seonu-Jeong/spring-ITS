package org.sparta.its.global.exception.errorcode;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HallImageErrorCode {

	// 400 BAD REQUEST

	// 404 NOT FOUND
	NOT_FOUND_HALL_IMAGE(HttpStatus.NOT_FOUND, "공연장 이미지를 찾을 수 없습니다.");

	private final HttpStatus httpStatus;
	private final String detail;
}
