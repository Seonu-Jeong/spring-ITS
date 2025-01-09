package org.sparta.its.domain.reservation.entity.service;

import org.sparta.its.domain.reservation.entity.dto.ReservationResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface ReservationService {

	ReservationResponseDto selectSeat(Long concertId, Long seatId);
}
