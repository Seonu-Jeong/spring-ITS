package org.sparta.its.domain.reservation.service;

import lombok.RequiredArgsConstructor;
import org.sparta.its.domain.cancelList.entity.CancelList;
import org.sparta.its.domain.cancelList.entity.CancelStatus;
import org.sparta.its.domain.cancelList.service.CancelListService;
import org.sparta.its.domain.concert.entity.Concert;
import org.sparta.its.domain.concert.entity.service.ConcertService;
import org.sparta.its.domain.hall.entity.Seat;
import org.sparta.its.domain.hall.entity.service.SeatService;
import org.sparta.its.domain.reservation.dto.ReservationResponseDto;
import org.sparta.its.domain.reservation.entity.Reservation;
import org.sparta.its.domain.reservation.entity.ReservationStatus;
import org.sparta.its.domain.reservation.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 예약 관련 서비스 구현 클래스
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

	private final ReservationRepository reservationRepository;
	private final SeatService seatService;
	private final ConcertService concertService;
	private final CancelListService cancelListService;

	/**
	 * 좌석 선택
	 *
	 * @param concertId 콘서트 ID
	 * @param seatId    좌석 ID
	 * @return ReservationResponseDto 선택된 좌석 예약 정보
	 */
	@Override
	@Transactional
	public ReservationResponseDto selectSeat(Long concertId, Long seatId) {
		// 콘서트 조회
		Concert concert = concertService.findById(concertId)
			.orElseThrow(() -> new IllegalArgumentException("콘서트를 찾을 수 없습니다"));
		// 좌석 조회
		Seat seat = seatService.findById(seatId)
			.orElseThrow(() -> new IllegalArgumentException("자리를 찾을 수 없습니다"));

		// 예약 가능 여부 확인
		Optional<Reservation> existingReservation = reservationRepository
			.findReservationForSeatAndConcert(seatId, concertId, ReservationStatus.PENDING);

		if (existingReservation.isPresent()) {
			throw new IllegalStateException("이 자리는 이미 예약되었습니다.");
		}

		// 예약 생성
		Reservation reservation = Reservation.builder()
			.seat(seat)
			.concert(concert)
			.status(ReservationStatus.PENDING)
			.build();

		reservationRepository.save(reservation);

		return ReservationResponseDto.fromEntity(reservation);
	}

	/**
	 * 예약 확정
	 *
	 * @param reservationId 예약 ID
	 * @return ReservationResponseDto 확정된 예약 정보
	 */
	@Override
	@Transactional
	public ReservationResponseDto completeReservation(Long reservationId) {
		// 예약 조회
		Reservation reservation = reservationRepository.findById(reservationId)
			.orElseThrow(() -> new IllegalArgumentException("해당 예약을 찾을 수 없습니다"));

		// 예약 상태 변경
		reservation.completeReservation();
		reservationRepository.save(reservation);

		return ReservationResponseDto.fromEntity(reservation);
	}

	/**
	 * 예약 취소
	 *
	 * @param reservationId 예약 ID
	 * @param description   취소 사유
	 */
	@Override
	@Transactional
	public void cancelReservation(Long reservationId, String description) {
		// 예약 조회
		Reservation reservation = reservationRepository.findById(reservationId)
			.orElseThrow(() -> new IllegalArgumentException("해당 예약을 찾을 수 없습니다"));

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

		cancelListService.save(cancelList);
	}
}
