package com.phegon.phegonbank.auth_users.services.impl;

import com.phegon.phegonbank.auth_users.dtos.LoginRequest;
import com.phegon.phegonbank.auth_users.dtos.LoginResponse;
import com.phegon.phegonbank.auth_users.dtos.RegistrationRequest;
import com.phegon.phegonbank.auth_users.dtos.ResetPasswordRequest;
import com.phegon.phegonbank.auth_users.repo.UserRepo;
import com.phegon.phegonbank.auth_users.services.AuthService;
import com.phegon.phegonbank.notification.services.NotificationService;
import com.phegon.phegonbank.res.Response;
import com.phegon.phegonbank.role.repo.RoleRepo;
import com.phegon.phegonbank.security.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {


    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final NotificationService notificationService;


    @Override
    public Response<String> register(RegistrationRequest request) {
        return null;
    }

    @Override
    public Response<LoginResponse> login(LoginRequest loginRequest) {
        return null;
    }

    @Override
    public Response<?> forgetPassword(String email) {
        return null;
    }

    @Override
    public Response<?> updatePasswordViaResetCode(ResetPasswordRequest resetPasswordRequest) {
        return null;
    }
}
