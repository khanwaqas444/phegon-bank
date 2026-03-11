package com.phegon.phegonbank.security;

import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class TokenService {

    private SecretKey key;

    private String JWT_SECRET;

    private String EXPIRATION_TIME;
}
