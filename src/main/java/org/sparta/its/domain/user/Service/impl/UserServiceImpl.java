package org.sparta.its.domain.user.Service.impl;

import static org.sparta.its.global.exception.errorcode.UserErrorCode.*;

import org.sparta.its.domain.user.Service.UserService;
import org.sparta.its.domain.user.dto.AuthRequest;
import org.sparta.its.domain.user.dto.AuthResponse;
import org.sparta.its.domain.user.entity.User;
import org.sparta.its.domain.user.repository.UserRepository;
import org.sparta.its.global.exception.UserException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final PasswordEncoder passwordEncoder;

	private final UserRepository userRepository;

	@Override
	public AuthResponse.SignUpDto signUp(AuthRequest.SignUpDto signUpDto) {

		if (userRepository.existsUserByEmail(signUpDto.getEmail())) {
			throw new UserException(ALREADY_EXIST);
		}

		User toSaveUser = signUpDto.toEntity(passwordEncoder);

		User savedUser = userRepository.save(toSaveUser);

		return new AuthResponse.SignUpDto(
			savedUser.getId(),
			savedUser.getEmail(),
			savedUser.getName(),
			savedUser.getRole().toString());
	}

	@Override
	public AuthResponse.LoginDto login(AuthRequest.LoginDto loginDto) {

		User user = userRepository.findUserByEmailAndStatusIsActivatedOrThrow(loginDto.getEmail());

		if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword()))
			throw new UserException(INVALID_LOGIN);

		return new AuthResponse.LoginDto(
			user.getId(),
			user.getName(),
			user.getEmail(),
			user.getRole()
		);
	}
}