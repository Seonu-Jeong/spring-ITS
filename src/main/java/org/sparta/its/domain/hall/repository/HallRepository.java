package org.sparta.its.domain.hall.repository;

import org.sparta.its.domain.hall.entity.Hall;
import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.validation.constraints.NotBlank;

public interface HallRepository extends JpaRepository<Hall, Long> {
	boolean existsByName(@NotBlank(message = "name 은 필수입니다.") String name);
}
