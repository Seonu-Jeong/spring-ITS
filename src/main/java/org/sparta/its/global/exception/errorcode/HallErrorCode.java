package org.sparta.its.global.exception.errorcode;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HallErrorCode {

	NOT_FOUND_HALL(HttpStatus.NOT_FOUND, "공연장 정보를 찾을 수 없습니다.");

	private final HttpStatus httpStatus;
	private final String detail;
}
