package org.sparta.its.domain.concert.fake;

import org.sparta.its.domain.hall.entity.Hall;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HallRepository extends JpaRepository<Hall, Long> {

	default Hall findByIdOrThrow(Long hallId) {
		return findById(hallId).orElseThrow(() -> new RuntimeException("Hall not found"));
	}
}
