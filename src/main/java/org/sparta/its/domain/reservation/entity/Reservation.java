package org.sparta.its.domain.reservation.entity;

import org.hibernate.annotations.DynamicUpdate;
import org.sparta.its.domain.concert.entity.Concert;
import org.sparta.its.domain.hall.entity.Seat;
import org.sparta.its.domain.user.entity.User;
import org.sparta.its.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "reservation")
@NoArgsConstructor
@DynamicUpdate
public class Reservation extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 연관관계
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "concert_id")
	private Concert concert;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "seat_id")
	private Seat seat;

	// 필드
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 8)
	private ReservationStatus status;

	@Builder
	public Reservation(User user, Concert concert, Seat seat, ReservationStatus status) {
		this.user = user;
		this.concert = concert;
		this.seat = seat;
		this.status = status;
	}

	public void completeReservation() {
		this.status = ReservationStatus.COMPLETED;
	}
}
