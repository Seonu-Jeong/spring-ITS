package org.sparta.its.domain.reservation.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.sparta.its.domain.concert.entity.Concert;
import org.sparta.its.domain.hall.entity.Seat;
import org.sparta.its.domain.reservation.entity.Reservation;
import org.sparta.its.domain.reservation.entity.ReservationStatus;
import org.sparta.its.domain.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)  // 스프링 컨텍스트 리프레시
class ReservationRepositoryTest {

	@Autowired
	private ReservationRepository reservationRepository;

	@BeforeEach
	void setUp() {
		Reservation reservation = Reservation.builder()
			.user(Mockito.mock(User.class))
			.concert(Mockito.mock(Concert.class))
			.seat(Mockito.mock(Seat.class))
			.status(ReservationStatus.PENDING)
			.concertDate(LocalDate.now())
			.build();

		reservationRepository.save(reservation);
	}

	@Test
	void findByIdOrThrowTest() {
		Reservation reservation = reservationRepository.findByIdOrThrow(1L);

		assertNotNull(reservation);
	}
}
