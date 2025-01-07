package org.sparta.its.domain.hall.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.DynamicUpdate;
import org.sparta.its.domain.concert.entity.Concert;
import org.sparta.its.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "hall")
@NoArgsConstructor
@DynamicUpdate
public class Hall extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 연관관계
	@OneToMany(mappedBy = "hall")
	private List<HallImage> hallImages = new ArrayList<>();

	@OneToMany(mappedBy = "hall")
	private List<Concert> concerts = new ArrayList<>();

	@OneToMany(mappedBy = "hall")
	private List<Seat> seats = new ArrayList<>();

	// 필드
	@Column(nullable = false, unique = true, length = 30)
	private String name;

	@Column(nullable = false, length = 50)
	private String location;

	@Column(nullable = false)
	private Integer capacity;

	@Column(nullable = false)
	private Boolean isOpen;
}