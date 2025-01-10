package org.sparta.its.global.exception.errorcode;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReservationErrorCode {

	// 400 BAD REQUEST

	// 403 FORBIDDEN
	ALREADY_BOOKED(HttpStatus.FORBIDDEN, "이 자리는 이미 예약되었습니다.");

	private final HttpStatus httpStatus;
	private final String detail;
}
