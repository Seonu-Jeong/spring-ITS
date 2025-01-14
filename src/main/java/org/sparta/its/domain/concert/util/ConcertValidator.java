package org.sparta.its.domain.concert.util;

import java.time.LocalDateTime;

import org.sparta.its.domain.concert.dto.ConcertRequest;
import org.sparta.its.domain.concert.entity.Concert;
import org.sparta.its.global.exception.ConcertException;
import org.sparta.its.global.exception.errorcode.ConcertErrorCode;

public class ConcertValidator {

	public static void validateRunningTime(ConcertRequest.UpdateDto updateDto) {
		if (updateDto.getRunningStartTime() != null && updateDto.getRunningEndTime() != null
			&& updateDto.getRunningStartTime().isAfter(updateDto.getRunningEndTime())) {
			throw new ConcertException(ConcertErrorCode.IS_NOT_AFTER_UPDATE_TIME);
		}
	}

	public static void validateStartAndEndDates(ConcertRequest.UpdateDto updateDto) {
		if (updateDto.getStartAt() != null && updateDto.getEndAt() != null &&
			updateDto.getStartAt().isAfter(updateDto.getEndAt())) {
			throw new ConcertException(ConcertErrorCode.IS_NOT_AFTER_UPDATE_DATE);
		}
	}

	public static void isBeforeToday(ConcertRequest.UpdateDto updateDto) {
		if (updateDto.getStartAt() != null && updateDto.getStartAt().isBefore(LocalDateTime.now()) ||
			(updateDto.getEndAt() != null && updateDto.getEndAt().isBefore(LocalDateTime.now()))) {
			throw new ConcertException(ConcertErrorCode.ALREADY_PASSED);
		}
	}

	public static void validateCrossDates(ConcertRequest.UpdateDto updateDto, Concert concert) {
		if (updateDto.getStartAt() == null && updateDto.getEndAt() != null && concert.getStartAt()
			.isAfter(updateDto.getEndAt())) {
			throw new ConcertException(ConcertErrorCode.IS_NOT_AFTER_UPDATE_DATE);
		}

		if (updateDto.getEndAt() == null && updateDto.getStartAt() != null && updateDto.getStartAt()
			.isAfter(concert.getEndAt())) {
			throw new ConcertException(ConcertErrorCode.IS_NOT_AFTER_UPDATE_DATE);
		}
	}

	public static void validateCrossTimes(ConcertRequest.UpdateDto updateDto, Concert concert) {
		if (updateDto.getRunningStartTime() == null && updateDto.getRunningEndTime() != null
			&& concert.getRunningStartTime()
			.isAfter(updateDto.getRunningEndTime())) {
			throw new ConcertException(ConcertErrorCode.IS_NOT_AFTER_UPDATE_TIME);
		}

		if (updateDto.getRunningEndTime() == null && updateDto.getRunningStartTime() != null
			&& updateDto.getRunningStartTime()
			.isAfter(concert.getRunningEndTime())) {
			throw new ConcertException(ConcertErrorCode.IS_NOT_AFTER_UPDATE_TIME);
		}
	}

	public static void validateCrossTimes(ConcertRequest.CreateDto createDto) {
		if (createDto.getRunningStartTime().isAfter(createDto.getRunningEndTime())) {
			throw new ConcertException(ConcertErrorCode.IS_NOT_AFTER_TIME);
		}
	}

	public static void validateCrossDates(ConcertRequest.CreateDto createDto) {
		if (createDto.getStartAt().isAfter(createDto.getEndAt())) {
			throw new ConcertException(ConcertErrorCode.IS_NOT_AFTER_DATE);
		}

	}

	public static void isBeforeToday(ConcertRequest.CreateDto createDto) {
		if (createDto.getStartAt().isBefore(LocalDateTime.now()) || createDto.getEndAt()
			.isBefore(LocalDateTime.now())) {
			throw new ConcertException(ConcertErrorCode.ALREADY_PASSED);
		}
	}
}

