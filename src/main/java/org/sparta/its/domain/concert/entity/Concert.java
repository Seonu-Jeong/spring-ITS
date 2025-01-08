package org.sparta.its.domain.concert.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.DynamicUpdate;
import org.sparta.its.domain.hall.entity.Hall;
import org.sparta.its.domain.reservation.entity.Reservation;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "concert")
@NoArgsConstructor
@DynamicUpdate
public class Concert {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 연관관계
	@OneToMany(mappedBy = "concert")
	private List<ConcertImage> concertImages = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hall_id")
	private Hall hall;

	@OneToMany(mappedBy = "concert")
	private List<Reservation> reservations = new ArrayList<>();

	// 필드
	@Column(nullable = false, length = 30)
	private String title;

	@Column(nullable = false, length = 30)
	private String singer;

	@Column(nullable = false)
	private LocalDateTime startAt;

	@Column(nullable = false)
	private LocalDateTime endAt;

	@Column(nullable = false)
	private LocalTime runningStartTime;

	@Column(nullable = false)
	private LocalTime runningEndTime;

	@Column(nullable = false)
	private Integer price;

	@Column(nullable = false)
	@ElementCollection
	private List<String> images = new ArrayList<>();

	@Builder
	public Concert(Hall hall, String title, String singer, LocalDateTime startAt, LocalDateTime endAt,
		LocalTime runningStartTime, LocalTime runningEndTime, Integer price, List<ConcertImage> concertImages) {
		this.hall = hall;
		this.title = title;
		this.singer = singer;
		this.startAt = startAt;
		this.endAt = endAt;
		this.runningStartTime = runningStartTime;
		this.runningEndTime = runningEndTime;
		this.price = price;
		this.concertImages = concertImages;
	}

	public void addImage(List<String> image) {
		this.images = image;
	}
}
