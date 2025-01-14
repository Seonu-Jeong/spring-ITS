package org.sparta.its.domain.concert.util;

import java.time.LocalDate;
import java.time.LocalTime;

import org.sparta.its.domain.concert.dto.ConcertRequest;
import org.sparta.its.domain.concert.entity.Concert;
import org.sparta.its.global.exception.ConcertException;
import org.sparta.its.global.exception.errorcode.ConcertErrorCode;

public class ConcertValidator {

	public static void startTimeIsAfterEndTimeWithUpdate(LocalTime startTime, LocalTime endTime) {
		if (startTime != null && endTime != null
			&& startTime.isAfter(endTime)) {
			throw new ConcertException(ConcertErrorCode.IS_NOT_AFTER_UPDATE_TIME);
		}
	}

	public static void startAtIsAfterEndAtWithUpdate(LocalDate startAt, LocalDate endAt) {
		if (startAt != null && endAt != null &&
			startAt.isAfter(endAt)) {
			throw new ConcertException(ConcertErrorCode.IS_NOT_AFTER_UPDATE_DATE);
		}
	}

	public static void startAtIsAfterEndAtWithNullCheck(LocalDate startAt, LocalDate endAt) {
		if (startAt != null && endAt != null &&
			startAt.isAfter(endAt)) {
			throw new ConcertException(ConcertErrorCode.IS_NOT_AFTER_DATE);
		}
	}

	public static void isBeforeTodayWithNullCheck(LocalDate startAt, LocalDate endAt) {
		if (startAt != null && startAt.isBefore(LocalDate.now()) ||
			(endAt != null && endAt.isBefore(LocalDate.now()))) {
			throw new ConcertException(ConcertErrorCode.ALREADY_PASSED);
		}
	}

	public static void compareDatesUpdateDtoToConcert(ConcertRequest.UpdateDto updateDto, Concert concert) {
		if (updateDto.getStartAt() == null && updateDto.getEndAt() != null && concert.getStartAt()
			.isAfter(updateDto.getEndAt())) {
			throw new ConcertException(ConcertErrorCode.IS_NOT_AFTER_UPDATE_DATE);
		}

		if (updateDto.getEndAt() == null && updateDto.getStartAt() != null && updateDto.getStartAt()
			.isAfter(concert.getEndAt())) {
			throw new ConcertException(ConcertErrorCode.IS_NOT_AFTER_UPDATE_DATE);
		}
	}

	public static void compareTimesUpdateDtoToConcert(ConcertRequest.UpdateDto updateDto, Concert concert) {
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

	public static void startTimeIsAfterEndTime(LocalTime startTime, LocalTime endTime) {
		if (startTime.isAfter(endTime)) {
			throw new ConcertException(ConcertErrorCode.IS_NOT_AFTER_TIME);
		}
	}

	public static void startAtIsAfterEndAt(LocalDate startAt, LocalDate endAt) {
		if (startAt.isAfter(endAt)) {
			throw new ConcertException(ConcertErrorCode.IS_NOT_AFTER_DATE);
		}

	}

	public static void isBeforeToday(LocalDate startAt, LocalDate endAt) {
		if (startAt.isBefore(LocalDate.now()) || endAt.isBefore(LocalDate.now())) {
			throw new ConcertException(ConcertErrorCode.ALREADY_PASSED);
		}
	}
}

