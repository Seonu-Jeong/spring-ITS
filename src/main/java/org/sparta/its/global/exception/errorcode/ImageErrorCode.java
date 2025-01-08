package org.sparta.its.global.exception.errorcode;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageErrorCode {

	NO_EXTENSION_FILE(HttpStatus.BAD_REQUEST, "파일 확장자가 없는 파일입니다."),
	NOT_ALLOW_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "해당 확장자는 업로드가 불가능합니다.");

	private final HttpStatus httpStatus;
	private final String detail;
}
