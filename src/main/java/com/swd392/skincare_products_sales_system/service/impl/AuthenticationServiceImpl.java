package com.swd392.skincare_products_sales_system.service.impl;

import java.text.ParseException;
import java.util.*;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import com.swd392.skincare_products_sales_system.constant.PredefinedRole;
import com.swd392.skincare_products_sales_system.dto.request.*;
import com.swd392.skincare_products_sales_system.dto.response.LoginResponse;
import com.swd392.skincare_products_sales_system.dto.response.RefreshTokenResponse;
import com.swd392.skincare_products_sales_system.dto.response.RegisterResponse;
import com.swd392.skincare_products_sales_system.enums.ErrorCode;
import com.swd392.skincare_products_sales_system.exception.AppException;
import com.swd392.skincare_products_sales_system.model.InvalidatedToken;
import com.swd392.skincare_products_sales_system.model.Role;
import com.swd392.skincare_products_sales_system.model.User;
import com.swd392.skincare_products_sales_system.repository.InvalidatedTokenRepository;
import com.swd392.skincare_products_sales_system.repository.RoleRepository;
import com.swd392.skincare_products_sales_system.repository.UserRepository;
import com.swd392.skincare_products_sales_system.service.AuthenticationService;
import com.swd392.skincare_products_sales_system.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    JwtUtil jwtUtil;
    InvalidatedTokenRepository invalidatedTokenRepository;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        //Fetch Role from Database instead of creating new one
        Role userRole = roleRepository.findByName(PredefinedRole.USER_ROLE)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        user.setRoles(Set.of(userRole));
        userRepository.save(user);
        return RegisterResponse.builder()
                .username(user.getUsername())
                .build();
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_LOGIN));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_LOGIN);
        }

        return LoginResponse.builder()
                .token(jwtUtil.generateToken(user))
                .authenticated(true)
                .build();
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request)  {
        SignedJWT signedJWT = null;
        try {
            signedJWT = jwtUtil.verifyToken(request.getToken(), true);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }

        String jit = null;
        try {
            jit = signedJWT.getJWTClaimsSet().getJWTID();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Date expiryTime = null;
        try {
            expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().token(jit).expiryTime(expiryTime).build();

        invalidatedTokenRepository.save(invalidatedToken);

        String username = null;
        try {
            username = signedJWT.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        var user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        var token = jwtUtil.generateToken(user);

        return RefreshTokenResponse.builder().token(token).authenticated(true).build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logout(LogoutRequest request) {
        SignedJWT signedJWT;
        try {
            signedJWT = jwtUtil.verifyToken(request.getToken(), false);
        } catch (ParseException | JOSEException e) {
            throw new RuntimeException("Invalid Token!");
        }
        String tokenId = null;
        try {
            tokenId = signedJWT.getJWTClaimsSet().getJWTID();
            log.info(tokenId);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Date expiryTime = null;
        try {
            expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // Lưu token vào danh sách đã logout
        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .token(tokenId)
                .expiryTime(expiryTime)
                .build();
        invalidatedTokenRepository.save(invalidatedToken);
    }

}
