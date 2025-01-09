package org.sparta.its.global.exception.errorcode;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConcertErrorCode {

	//400 BAD_REQUEST
	IS_NOT_AFTER_TIME(HttpStatus.BAD_REQUEST, "콘서트 시작시간은 종료시간 보다 늦을 수 없습니다."),

	IS_NOT_AFTER_DATE(HttpStatus.BAD_REQUEST, "콘서트 시작 날짜는 종료 날짜 보다 늦을 수 없습니다.");

	private final HttpStatus httpStatus;
	private final String detail;
}
