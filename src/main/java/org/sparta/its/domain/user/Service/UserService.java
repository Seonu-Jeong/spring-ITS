package org.sparta.its.domain.user.Service;

import org.sparta.its.domain.user.dto.AuthRequest;
import org.sparta.its.domain.user.dto.AuthResponse;

public interface UserService {

	public AuthResponse.SignUpDto signUp(AuthRequest.SignUpDto signUpDto);

	public AuthResponse.LoginDto login(AuthRequest.LoginDto loginDto);
}