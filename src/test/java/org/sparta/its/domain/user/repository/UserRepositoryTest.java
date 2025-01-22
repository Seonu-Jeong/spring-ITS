package org.sparta.its.domain.user.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
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
	@DisplayName("활성된 유저 email로 검색 테스트")
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
		assertSame(user, result.get());
	}

	@Test
	@DisplayName("활성된 유저 id로 검색 테스트")
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
		assertSame(user, result.get());
	}

	@Test
	@DisplayName("유저 이메일 존재 여부 테스트")
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