package org.sparta.its.domain.reservation.service;

import java.util.Optional;

import org.sparta.its.domain.cancelList.entity.CancelList;
import org.sparta.its.domain.cancelList.entity.CancelStatus;
import org.sparta.its.domain.cancelList.repository.CancelListRepository;
import org.sparta.its.domain.concert.entity.Concert;
import org.sparta.its.domain.concert.repository.ConcertRepository;
import org.sparta.its.domain.hall.entity.Seat;
import org.sparta.its.domain.hall.repository.SeatRepository;
import org.sparta.its.domain.reservation.entity.Reservation;
import org.sparta.its.domain.reservation.entity.ReservationStatus;
import org.sparta.its.domain.reservation.dto.ReservationResponse;
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

@Service
@RequiredArgsConstructor
public class ReservationService {

	private final ReservationRepository reservationRepository;
	private final SeatRepository seatRepository;
	private final ConcertRepository concertRepository;
	private final UserRepository userRepository;
	private final CancelListRepository cancelListRepository;

	/**
	 * 좌석 선택
	 *
	 * @param concertId 콘서트 아이디
	 * @param seatId 좌석 아이디
	 * @return ReservationResponse.SelectDto 선택된 좌석 예약 정보
	 */
	@Transactional
	public ReservationResponse.SelectDto selectSeat(Long concertId, Long seatId, Long userId) {
		// 콘서트 조회
		Concert concert = concertRepository.findByIdOrThrow(concertId);
		// 좌석 조회
		Seat seat = seatRepository.findByIdOrThrow(seatId);
		// 유저 확인
		User user = userRepository.findById(userId)
			.orElseThrow(()-> new UserException(UserErrorCode.UNAUTHORIZED_ACCESS));
		// 예약 가능 여부 확인
		Optional<Reservation> existingReservation = reservationRepository
			.findReservationForSeatAndConcert(seat, concert, ReservationStatus.PENDING);

		if (existingReservation.isPresent()) {
			throw new ReservationException(ReservationErrorCode.ALREADY_BOOKED);
		}

		// 예약 생성
		Reservation reservation = Reservation.builder()
			.user(user)
			.seat(seat)
			.concert(concert)
			.status(ReservationStatus.PENDING)
			.build();

		reservationRepository.save(reservation);

		return ReservationResponse.SelectDto.toDto(reservation);
	}

	/**
	 * 좌석 선택
	 *
	 * @param concertId 콘서트 아이디
	 * @param seatId 좌석 아이디
	 * @param reservationId 예약 아이디
	 * @param userId 유저 아이디
	 * @return ReservationResponse.SelectDto 선택된 좌석 예약 정보
	 */
	@Transactional
	public ReservationResponse.CompleteDto completeReservation(Long concertId, Long seatId, Long reservationId, Long userId) {
		// 예약 조회
		Reservation reservation = reservationRepository.findById(reservationId)
			.orElseThrow(() -> new ReservationException(ReservationErrorCode.NOT_FOUND_RESERVATION));

		// 예약과 로그인 사용자 검증
		if (!reservation.getUser().getId().equals(userId)) {
			throw new UserException(UserErrorCode.UNAUTHORIZED_ACCESS);
		}

		reservation.completeReservation();

		reservationRepository.save(reservation);

		return ReservationResponse.CompleteDto.toDto(reservation);
	}

	@Transactional
	public ReservationResponse.CancelDto cancelReservation(Long reservationId, String description) {
		// 예약 조회
		Reservation reservation = reservationRepository.findById(reservationId)
			.orElseThrow(() -> new ReservationException(ReservationErrorCode.NOT_FOUND_RESERVATION));

		// 완료된 예약만 취소 가능
		if (reservation.getStatus() != ReservationStatus.COMPLETED) {
			throw new IllegalStateException("완료되지 않은 예약은 취소할 수 없습니다.");
		}

		// 예약 상태를 취소로 변경
		reservation.cancelReservation();
		reservationRepository.save(reservation);

		// 취소 내역 저장
		CancelList cancelList = CancelList.builder()
			.user(reservation.getUser())
			.description(description)
			.status(CancelStatus.REQUESTED)
			.build();

		cancelListRepository.save(cancelList);

		return ReservationResponse.CancelDto.toDto(reservation);
	}
}
