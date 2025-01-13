package org.sparta.its.global.exception.errorcode;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConcertImageErrorCode {

	// 404 NOT_FOUND
	NOT_FOUND(HttpStatus.NOT_FOUND, "콘서트 이미지 정보를 찾을 수 없습니다.");

	private final HttpStatus httpStatus;
	private final String detail;
}
