package org.sparta.its.domain.reservation.service;

import static org.sparta.its.global.exception.errorcode.ReservationErrorCode.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.sparta.its.domain.concert.entity.Concert;
import org.sparta.its.domain.concert.repository.ConcertRepository;
import org.sparta.its.domain.hall.entity.Seat;
import org.sparta.its.domain.hall.repository.SeatRepository;
import org.sparta.its.domain.reservation.dto.ReservationResponse;
import org.sparta.its.domain.reservation.dto.redis.TempDataDto;
import org.sparta.its.domain.reservation.entity.Reservation;
import org.sparta.its.domain.reservation.entity.ReservationStatus;
import org.sparta.its.domain.reservation.repository.ReservationRepository;
import org.sparta.its.domain.user.entity.User;
import org.sparta.its.domain.user.repository.UserRepository;
import org.sparta.its.global.exception.ReservationException;
import org.sparta.its.global.exception.UserException;
import org.sparta.its.global.exception.errorcode.ReservationErrorCode;
import org.sparta.its.global.exception.errorcode.UserErrorCode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

/**
 * create on 2025. 02. 02.
 * create by IntelliJ IDEA.
 *
 * 예약 관련 FacadeService.
 *
 * @author Seonu-Jeong, TaeHyeon Kim
 */
@Service
@RequiredArgsConstructor
public class ReservationTestRedisService {

	private final ConcertRepository concertRepository;
	private final UserRepository userRepository;
	private final ReservationRepository reservationRepository;
	private final SeatRepository seatRepository;
	private final RedisTemplate<String, TempDataDto> redisTemplate;

	/**
	 * 네임드 락 기반 좌석 선택
	 *
	 * @param concertId 콘서트 고유 식별자
	 * @param seatId 좌석 고유 식별자
	 * @param date 공연 날짜
	 * @param userId 유저 고유 식별자
	 * @return {@link ReservationResponse.SelectDto}
	 */
	public String lockSelectSeat(Long concertId, Long seatId, LocalDate date, Long userId) {
		// 레디스 키로 활용
		String key = keyGenerator(concertId, seatId, date);

		// 스트링, 오브젝트 자료형 설정
		ValueOperations<String, TempDataDto> stringOperation = redisTemplate.opsForValue();

		// request 가 유효한지 확인
		isValidateRequest(concertId, seatId, date, userId);

		// setIfAbsent : 해당 키가 없을 경우, 키를 만들고 true 반환, 해당 키가 있을 경우, 키를 만들지 않고 false 반환
		Boolean isLocked
			= stringOperation.setIfAbsent(key, new TempDataDto(concertId, seatId, date, userId), 5, TimeUnit.MINUTES);

		// 다중스레드로 접근하더라도 redis 는 싱글스레드 이기 때문에 이렇게 처리해도 동시성 제어 가능
		if (Boolean.FALSE.equals(isLocked)) {
			throw new ReservationException(TIME_OUT);
		}

		return key;
	}

	private void isValidateRequest(Long concertId, Long seatId, LocalDate date, Long userId) {
		// 콘서트 조회
		Concert concert = concertRepository.findByIdOrThrow(concertId);

		// 좌석 조회
		Seat seat = seatRepository.findByIdOrThrow(seatId);

		// 유저 확인
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserException(UserErrorCode.FORBIDDEN_ACCESS));

		// 예약 가능 여부 확인
		Optional<Reservation> existingReservation
			= reservationRepository.findReservationByConcertInfo(seat, concert, date, ReservationStatus.PENDING);

		if (existingReservation.isPresent()) {
			throw new ReservationException(ReservationErrorCode.ALREADY_BOOKED);
		}

		// 콘서트 선택 날짜 검증
		boolean isCorrectConcertDate
			= concert.getStartAt().minusDays(1).isBefore(date)
			&& concert.getEndAt().plusDays(1).isAfter(date);

		if (!isCorrectConcertDate) {
			throw new ReservationException(ReservationErrorCode.NOT_CORRECT_DATE);
		}
	}

	/**
	 * 좌석 선택 완료
	 *
	 * @param key Redis 키 값
	 * @param userId 유저 고유 식별자
	 * @return {@link ReservationResponse.CompleteDto}
	 */
	@Transactional
	public ReservationResponse.CompleteDto completeRedisReservation(String key, Long userId) {
		// 스트링, 오브젝트 자료형 설정
		ValueOperations<String, TempDataDto> dtoOperation = redisTemplate.opsForValue();
		// 좌석 선택에서 반환받은 키 값을 통해  dto 를 들고옴
		TempDataDto tempDataDto = dtoOperation.get(key);

		// 예약과 로그인 사용자 검증
		if (!tempDataDto.getUserId().equals(userId)) {
			throw new UserException(UserErrorCode.FORBIDDEN_ACCESS);
		}

		// 콘서트 조회
		Concert concert = concertRepository.findByIdOrThrow(tempDataDto.getConcertId());

		// 좌석 조회
		Seat seat = seatRepository.findByIdOrThrow(tempDataDto.getSeatId());

		// 유저 확인
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserException(UserErrorCode.FORBIDDEN_ACCESS));

		// 예약 가능 여부 확인
		Optional<Reservation> existingReservation
			= reservationRepository.findReservationByConcertInfo(
			seat,
			concert,
			tempDataDto.getDate(),
			ReservationStatus.PENDING);

		if (existingReservation.isPresent()) {
			throw new ReservationException(ReservationErrorCode.ALREADY_BOOKED);
		}

		// 예약 생성
		Reservation reservation = Reservation.builder()
			.user(user)
			.seat(seat)
			.concert(concert)
			.status(ReservationStatus.COMPLETED)
			.concertDate(tempDataDto.getDate())
			.build();

		reservationRepository.save(reservation);

		// 예약을 끝내면 캐시 삭제 (삭제해주지 않아도 됨)
		redisTemplate.delete(key);

		return ReservationResponse.CompleteDto.toDto(reservation);
	}

	private String keyGenerator(Long concertId, Long seatId, LocalDate date) {
		return concertId + "/" + seatId + "/" + date;
	}

}
