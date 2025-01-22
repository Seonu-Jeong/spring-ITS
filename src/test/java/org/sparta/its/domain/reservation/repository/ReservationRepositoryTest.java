package org.sparta.its.domain.reservation.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sparta.its.domain.concert.entity.Concert;
import org.sparta.its.domain.concert.repository.ConcertRepository;
import org.sparta.its.domain.hall.entity.Hall;
import org.sparta.its.domain.hall.entity.Seat;
import org.sparta.its.domain.hall.repository.HallRepository;
import org.sparta.its.domain.hall.repository.SeatRepository;
import org.sparta.its.domain.reservation.entity.Reservation;
import org.sparta.its.domain.reservation.entity.ReservationStatus;
import org.sparta.its.domain.user.entity.Role;
import org.sparta.its.domain.user.entity.User;
import org.sparta.its.domain.user.repository.UserRepository;
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

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ConcertRepository concertRepository;

	@Autowired
	private SeatRepository seatRepository;

	@Autowired
	private HallRepository hallRepository;

	private Concert testConcert;
	private Seat testSeat;
	private Hall testHall;

	@BeforeEach
	void setUp() {
		testSeat = new Seat();
		testHall = new Hall();
		testConcert = new Concert();
	}

	//setup helper method
	private Hall createAndSaveHall() {
		return hallRepository.save(Hall.builder()
			.name("Test Hall")
			.location("Test Location")
			.capacity(100)
			.isOpen(true)
			.build());
	}

	private Seat createAndSaveSeat(Hall hall, int seatNumber) {
		return seatRepository.save(new Seat(hall, seatNumber));
	}

	private Concert createAndSaveConcert(Hall hall, String title, String singer) {
		return concertRepository.save(Concert.builder()
			.hall(hall)
			.title(title)
			.singer(singer)
			.startAt(LocalDate.now().minusDays(1))
			.endAt(LocalDate.now().plusDays(1))
			.runningStartTime(LocalTime.of(19, 0))
			.runningEndTime(LocalTime.of(21, 0))
			.price(11000)
			.build());
	}

	private User createAndSaveUser(String email, String password, String name, String phoneNumber) {
		return userRepository.save(User.builder()
			.email(email)
			.password(password)
			.name(name)
			.phoneNumber(phoneNumber)
			.role(Role.USER)
			.build());
	}

	private Reservation createAndSaveReservation(User user, Concert concert, Seat seat) {
		return reservationRepository.save(Reservation.builder()
			.user(user)
			.concert(concert)
			.seat(seat)
			.status(ReservationStatus.PENDING)
			.concertDate(LocalDate.now())
			.build());
	}

	@Test
	void findByIdOrThrowTest() {
		User testUser = createAndSaveUser(
			"test@email.com",
			"PAssword1234@",
			"testName",
			"01012345656");
		Reservation savedReservation = createAndSaveReservation(testUser, testConcert, testSeat);

		Reservation foundReservation = reservationRepository.findByIdOrThrow(savedReservation.getId());

		assertNotNull(foundReservation);
		assertEquals(testUser.getId(), foundReservation.getUser().getId());
		assertEquals(testConcert.getId(), foundReservation.getConcert().getId());
		assertEquals(testSeat.getId(), foundReservation.getSeat().getId());
	}

	@Test
	void findReservationByConcertInfoTest() {
		//Given
		testHall = createAndSaveHall();
		testConcert = createAndSaveConcert(testHall, "Test", "Test");
		testSeat = createAndSaveSeat(testHall, 2);
		User testUser = createAndSaveUser("test@email.com", "PAssword1234@", "testName", "01012345656");
		Reservation savedReservation = createAndSaveReservation(testUser, testConcert, testSeat);

		//When
		Optional<Reservation> result = reservationRepository
			.findReservationByConcertInfo(
				testSeat,
				testConcert,
				LocalDate.now(),
				ReservationStatus.PENDING);

		//Then
		assertTrue(result.isPresent(), "예약이 존재하지 않습니다");
		Reservation foundReservation = result.get();
		assertEquals(savedReservation.getId(), foundReservation.getId(), "예약 아이디와 일치하지 않습니다");
		assertEquals(testUser.getId(), foundReservation.getUser().getId(), "유저 아이디와 일치하지 않습니다");
		assertEquals(testConcert.getId(), foundReservation.getConcert().getId(), "콘서트 아이디가 일치하지 않습니다");
		assertEquals(testSeat.getId(), foundReservation.getSeat().getId(), "자리 아이디가 일치하지 않습니다");
		assertEquals(LocalDate.now(), foundReservation.getConcertDate(), "콘서트 날짜가 일치하지 않습니다");
		assertEquals(ReservationStatus.PENDING, foundReservation.getStatus(), "예약 상태가 일치하지 않습니다");
	}
}
