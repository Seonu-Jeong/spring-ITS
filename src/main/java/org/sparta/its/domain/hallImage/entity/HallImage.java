package org.sparta.its.domain.hallImage.entity;

import org.sparta.its.domain.hall.entity.Hall;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "hall_image")
@NoArgsConstructor
public class HallImage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 연관관계
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hall_id")
	private Hall hall;

	// 필드
	@Column(nullable = false, length = 255)
	private String imageUrl;

	public HallImage(Hall hall, String imageUrl) {
		this.hall = hall;
		this.imageUrl = imageUrl;
	}

	public void updateUrl(String publicUrl) {
		this.imageUrl = publicUrl;
	}
}
