package org.sparta.its.domain.reservation.dto.redis;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TempDataDto {
	private Long concertId;
	private Long seatId;
	private LocalDate date;
	private Long userId;

	public TempDataDto(Long concertId, Long seatId, LocalDate date, Long userId) {
		this.concertId = concertId;
		this.seatId = seatId;
		this.date = date;
		this.userId = userId;
	}
}
