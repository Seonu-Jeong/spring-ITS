package org.sparta.its.domain.hall.entity;

import java.util.List;

import org.hibernate.annotations.DynamicUpdate;

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
public class Hall {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToMany(mappedBy = "hall")
	private List<HallImage> hallImages;

	@Column(nullable = false, unique = true, length = 30)
	private String name;

	@Column(nullable = false, length = 50)
	private String location;

	@Column(nullable = false)
	private Integer capacity;
}