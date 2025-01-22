package org.sparta.its.domain.user.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.sparta.its.domain.user.entity.Role;
import org.sparta.its.domain.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Test
	void findUserByEmailAndStatusIsActivatedTest() {

		User user = new User(
			"user1@gmail.com",
			"password1",
			"user1",
			"010-1234-5678",
			Role.USER
		);

		userRepository.save(user);

		String email = user.getEmail();

		Optional<User> result = userRepository.findUserByEmailAndStatusIsActivated(email);

		assertTrue(result.isPresent());
		assertEquals(email, result.get().getEmail());
	}

	@Test
	void findUserByIdAndStatusIsActivatedTest() {

		User user = new User(
			"user1@gmail.com",
			"password1",
			"user1",
			"010-1234-5678",
			Role.USER
		);

		User savedUser = userRepository.save(user);

		Optional<User> result = userRepository.findUserByIdAndStatusIsActivated(savedUser.getId());

		assertTrue(result.isPresent());
		assertEquals(user.getId(), result.get().getId());
	}

	@Test
	void existsUserByEmailTest() {

		User user = new User(
			"user1@gmail.com",
			"password1",
			"user1",
			"010-1234-5678",
			Role.USER
		);

		User savedUser = userRepository.save(user);

		Boolean result = userRepository.existsUserByEmail(savedUser.getEmail());

		assertTrue(result);
	}
}