package org.sparta.its.domain.hall.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sparta.its.domain.hall.entity.Hall;
import org.sparta.its.global.exception.HallException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class HallRepositoryTest {

	@Autowired
	private HallRepository hallRepository;

	private static Hall hall;
	private static Hall savedHall;

	@BeforeEach
	public void initSetting() {
		hall = Hall.builder()
			.name("testetste")
			.capacity(5)
			.location("서울")
			.isOpen(true)
			.build();

		savedHall = hallRepository.save(hall);
	}

	@Test
	public void saveHall() {
		Assertions.assertSame(hall, savedHall);
	}

	@Test
	public void existsByName() {
		boolean isTrue = hallRepository.existsByName("testetste");

		Assertions.assertTrue(isTrue);
	}

	@Test
	public void findByIdOrThrow() {
		assertThrows(HallException.class, () -> {
			hallRepository.findByIdOrThrow(Long.MAX_VALUE);
		});
	}

	@Test
	public void uniqueKey() {

		hall = Hall.builder()
			.name("testetste")
			.capacity(5)
			.location("서울")
			.isOpen(true)
			.build();

		assertThrows(DataIntegrityViolationException.class, () -> {
			hallRepository.save(hall);
		});
	}

}
