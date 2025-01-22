package org.sparta.its.domain.reservation.service;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.sparta.its.domain.reservation.dto.ReservationResponse;
import org.sparta.its.global.exception.ReservationException;
import org.sparta.its.global.exception.errorcode.ReservationErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationServiceFacade {

	private final ReservationService reservationService;
	private final RedissonClient redissonClient;

	@Value("${WAIT_TIME}")
	long waitTime;

	@Value("${LEASE_TIME}")
	long leaseTime;

	/**
	 * 좌석 선택 - Redis 동시성 제어
	 *
	 * @param concertId 콘서트 고유 식별자
	 * @param seatId 좌석 고유 식별자
	 * @param date 콘서트 날짜
	 * @param userId 유저 고유 식별자
	 * @return {@link ReservationResponse.SelectDto}
	 */
	public ReservationResponse.SelectDto selectSeat(Long concertId, Long seatId, LocalDate date, Long userId) {
		RLock lock = redissonClient.getLock(generateLockName(concertId, seatId, date));

		ReservationResponse.SelectDto resultDto = null;

		try {
			boolean acquireLock = lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);

			if (!acquireLock) {
				throw new ReservationException(ReservationErrorCode.TIME_OUT);
			}

			resultDto = reservationService.selectSeat(concertId, seatId, date, userId);
		} catch (InterruptedException e) {
			// controller advice 예외 처리 위임
			throw new RuntimeException(e);
		} finally {
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}

		return resultDto;

	}

	private String generateLockName(Long concertId, Long seatId, LocalDate date) {
		return concertId + "_" + seatId + "_" + date.toString();
	}
}