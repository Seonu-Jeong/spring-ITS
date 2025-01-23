package org.sparta.its.domain.reservation.service;

import java.time.LocalDate;
import java.util.Optional;

import org.sparta.its.domain.cancelList.repository.CancelListRepository;
import org.sparta.its.domain.concert.entity.Concert;
import org.sparta.its.domain.concert.repository.ConcertRepository;
import org.sparta.its.domain.hall.entity.Seat;
import org.sparta.its.domain.hall.repository.SeatRepository;
import org.sparta.its.domain.reservation.dto.ReservationResponse;
import org.sparta.its.domain.reservation.entity.Reservation;
import org.sparta.its.domain.reservation.entity.ReservationStatus;
import org.sparta.its.domain.reservation.repository.ReservationRepository;
import org.sparta.its.domain.user.entity.User;
import org.sparta.its.domain.user.repository.UserRepository;
import org.sparta.its.global.exception.ReservationException;
import org.sparta.its.global.exception.UserException;
import org.sparta.its.global.exception.errorcode.ReservationErrorCode;
import org.sparta.its.global.exception.errorcode.UserErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * create on 2025. 01. 25.
 * create by IntelliJ IDEA.
 *
 * 예약 관련 Service.
 *
 * @author Jun Heo
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationLockService {

	private final ReservationRepository reservationRepository;
	private final SeatRepository seatRepository;
	private final ConcertRepository concertRepository;
	private final UserRepository userRepository;
	private final CancelListRepository cancelListRepository;

	/**
	 * 좌석 선택
	 *
	 * @param concertId 콘서트 고유 식별자
	 * @param seatId 좌석 고유 식별자
	 * @return {@link ReservationResponse.SelectDto}
	 */
	public ReservationResponse.SelectDto selectSeatWithInnerLock(Long concertId, Long seatId, LocalDate date,
		Long userId) {
		// 콘서트 조회
		Concert concert = concertRepository.findByIdOrThrow(concertId);

		// 유저 확인
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserException(UserErrorCode.FORBIDDEN_ACCESS));
		Reservation reservation = null;

		Seat seat = seatRepository.findByIdOrThrow(seatId);

		reservation = getReservation(date, user, seat, concert);

		return ReservationResponse.SelectDto.toDto(reservation, date);
	}

	@Transactional
	public Reservation getReservation(LocalDate date, User user, Seat seat, Concert concert) {
		String key = keyGenerator(concert.getId(), seat.getId(), date);
		int lockAcquired = 0;
		Reservation reservation = null;

		try {
			lockAcquired = reservationRepository.getLock(key);
			if (lockAcquired == 0) {
				throw new ReservationException(ReservationErrorCode.ALREADY_BOOKED);
			}

			// 예약 가능 여부 확인
			Optional<Reservation> existingReservation
				= reservationRepository.findReservationByConcertInfo(seat, concert, date, ReservationStatus.PENDING);

			if (existingReservation.isPresent()) {
				throw new ReservationException(ReservationErrorCode.ALREADY_BOOKED);
			}

			reservation = Reservation.builder()
				.user(user)
				.seat(seat)
				.concert(concert)
				.status(ReservationStatus.PENDING)
				.concertDate(date)
				.build();

			reservationRepository.saveAndFlush(reservation);
		} finally {
			if (lockAcquired == 1) {
				reservationRepository.releaseLock(key);
			}
		}
		return reservation;
	}

	private String keyGenerator(Long concertId, Long seatId, LocalDate date) {

		return concertId + "/" + seatId + "/" + date;
	}
}
