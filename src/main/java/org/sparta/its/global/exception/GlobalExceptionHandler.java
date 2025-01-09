package org.sparta.its.global.exception;

import org.sparta.its.global.exception.response.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = {RuntimeException.class})
	public ResponseEntity<ErrorResponseDto> handleCustomException(RuntimeException exception) {
		ErrorResponseDto responseDto = ErrorResponseDto.builder()
			.code("SERVER_ERROR")
			.massage("서버 내부적인 문제가 발생했습니다.")
			.build();

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
	}

	@ExceptionHandler(value = {UserException.class})
	public ResponseEntity<ErrorResponseDto> handleCustomException(UserException exception) {
		return ErrorResponseDto.toResponseEntity(exception);
	}

	@ExceptionHandler(value = {ConcertException.class})
	public ResponseEntity<ErrorResponseDto> handleCustomException(ConcertException exception) {
		return ErrorResponseDto.toResponseEntity(exception);
	}

	@ExceptionHandler(value = {HallException.class})
	public ResponseEntity<ErrorResponseDto> handleCustomException(HallException exception) {
		return ErrorResponseDto.toResponseEntity(exception);
	}

	@ExceptionHandler(value = {MethodArgumentNotValidException.class})
	public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(
		MethodArgumentNotValidException exception) {
		ErrorResponseDto responseDto = ErrorResponseDto.builder()
			.code("VALIDATION_ERROR")
			.massage(exception.getBindingResult().getAllErrors().get(0).getDefaultMessage())
			.build();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
	}
}