package org.sparta.its.domain.hall.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sparta.its.domain.hall.entity.Hall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class HallQueryDslRepositoryTest {

	@Autowired
	private HallRepository hallRepository;

	@Test
	@DisplayName("공연장명 공연장 장소로 페이징 조회 정상 조회")
	void findByNameAndLocation() {
		String name = "";
		String location = "";
		Pageable pageable = PageRequest.of(0, 2);

		Hall hall1 = new Hall("잠실 경기장", "잠실", 400, true);
		Hall hall2 = new Hall("상암 경기장", "상암", 400, true);
		Hall hall3 = new Hall("수원 경기장", "수원", 400, true);

		hallRepository.save(hall1);
		hallRepository.save(hall2);
		hallRepository.save(hall3);

		Page<Hall> hallPage = hallRepository.findByNameAndLocation(name, location, pageable);

		Assertions.assertThat(hallPage.getTotalElements()).isEqualTo(3);
		Assertions.assertThat(hallPage.getTotalPages()).isEqualTo(2);
		Assertions.assertThat(hallPage.getContent().get(0).getName()).isEqualTo("상암 경기장");
	}
}
