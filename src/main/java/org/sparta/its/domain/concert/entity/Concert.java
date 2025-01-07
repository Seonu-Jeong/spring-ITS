package org.sparta.its.domain.concert.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
}