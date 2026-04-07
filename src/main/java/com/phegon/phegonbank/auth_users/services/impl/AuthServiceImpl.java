package com.phegon.phegonbank.auth_users.services.impl;

import com.phegon.phegonbank.account.entity.Account;
import com.phegon.phegonbank.account.services.AccountService;
import com.phegon.phegonbank.auth_users.dtos.LoginRequest;
import com.phegon.phegonbank.auth_users.dtos.LoginResponse;
import com.phegon.phegonbank.auth_users.dtos.RegistrationRequest;
import com.phegon.phegonbank.auth_users.dtos.ResetPasswordRequest;
import com.phegon.phegonbank.auth_users.entity.PasswordResetCode;
import com.phegon.phegonbank.auth_users.entity.User;
import com.phegon.phegonbank.auth_users.repo.PasswordResetCodeRepo;
import com.phegon.phegonbank.auth_users.repo.UserRepo;
import com.phegon.phegonbank.auth_users.services.AuthService;
import com.phegon.phegonbank.auth_users.services.CodeGenerator;
import com.phegon.phegonbank.enums.AccountType;
import com.phegon.phegonbank.enums.Currency;
import com.phegon.phegonbank.exceptions.BadRequestException;
import com.phegon.phegonbank.exceptions.NotFoundException;
import com.phegon.phegonbank.notification.dtos.NotificationDTO;
import com.phegon.phegonbank.notification.services.NotificationService;
import com.phegon.phegonbank.res.Response;
import com.phegon.phegonbank.role.entity.Role;
import com.phegon.phegonbank.role.repo.RoleRepo;
import com.phegon.phegonbank.security.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
        List<Role> roles;

        if (request.getRoles() == null || request.getRoles().isEmpty()) {
            //DEFAULT TO CUSTOMER
            Role defaultRole = roleRepo.findByName("CUSTOMER")
                    .orElseThrow(() -> new NotFoundException("CUSTOMER ROLE NOT FOUND"));

            roles = Collections.singletonList(defaultRole);
        } else {
            roles = request.getRoles().stream()
                    .map(roleName -> roleRepo.findByName(roleName)
                            .orElseThrow(() -> new NotFoundException("ROLE NOT FOUND" + roleName)))
                    .toList();
        }

        if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("Email Already Present");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .active(true)
                .build();

        User savedUser = userRepo.save(user);

        //TODO AUTO GENERATE AN ACCOUNT NUMBER FOR THE USER
        Account savedAccount = accountService.createAccount(AccountType.SAVINGS, savedUser);

        //SEND WELCOME EMAIL
        Map<String, Object> vars = new HashMap<>();
        vars.put("name", savedUser.getFirstName());

        NotificationDTO notificationDTO = NotificationDTO.builder()
                .recipient(savedUser.getEmail())
                .subject("Welcome to Phegon Bank 🎉")
                .templateName("welcome")
                .templateVariables(vars)
                .build();

        notificationService.sendEmail(notificationDTO, savedUser);


        //SEND ACCOUNT CREATION/DETAILS EMAIL
        Map<String, Object> accountVars = new HashMap<>();
        accountVars.put("name", savedUser.getFirstName());
        accountVars.put("accountNumber", savedAccount.getAccountNumber());
        accountVars.put("accountType", AccountType.SAVINGS.name());
        accountVars.put("currency", Currency.USD);

        NotificationDTO accountCreatedEmail = NotificationDTO.builder()
                .recipient(savedUser.getEmail())
                .subject("Your New Bank Account Has Been Created ✅")
                .templateName("account-created")
                .templateVariables(accountVars)
                .build();

        notificationService.sendEmail(accountCreatedEmail, savedUser);
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
