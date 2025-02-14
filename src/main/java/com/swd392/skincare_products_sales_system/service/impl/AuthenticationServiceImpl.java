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
import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.exception.AppException;
import com.swd392.skincare_products_sales_system.model.InvalidatedToken;
import com.swd392.skincare_products_sales_system.model.Role;
import com.swd392.skincare_products_sales_system.model.User;
import com.swd392.skincare_products_sales_system.repository.InvalidatedTokenRepository;
import com.swd392.skincare_products_sales_system.repository.RoleRepository;
import com.swd392.skincare_products_sales_system.repository.UserRepository;
import com.swd392.skincare_products_sales_system.service.AuthenticationService;
import com.swd392.skincare_products_sales_system.util.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .gender(request.getGender())
                .birthday(request.getBirthday())
                .status(Status.ACTIVE)
                .build();
        // Lấy Role từ Database gắn vào
        Role customRole = roleRepository.findByName(PredefinedRole.CUSTOMER_ROLE)
                .orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION));
        user.setRole(customRole);
        user.setIsDeleted(false);
        userRepository.save(user);
        return RegisterResponse.builder()
                .username(user.getUsername())
                .gender(user.getGender())
                .birthday(user.getBirthday())
                .build();
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_LOGIN));
        // Kiểm tra mật khẩu có khớp không
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_LOGIN);
        }
        // Trả về Token
        return LoginResponse.builder()
                .token(jwtUtil.generateToken(user)) //Token được generate
                .authenticated(true)
                .build();
    }

    @Override
    @Transactional
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        SignedJWT signedJWT;
        try {
            signedJWT = jwtUtil.verifyToken(request.getToken(), true);
        } catch (ParseException | JOSEException e) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        // Lấy thông tin JWT từ token đã xác thực
        String tokenId = null;
        Date expiryTime = null;
        String username = null;

        try {
            tokenId = signedJWT.getJWTClaimsSet().getJWTID();
            expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            username = signedJWT.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        // Kiểm tra nếu token đã hết hạn
        if (expiryTime.before(new Date())) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        // Lưu token vào bảng invalidated_token để ngừng sử dụng
        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .token(tokenId)
                .expiryTime(expiryTime)
                .build();
        invalidatedTokenRepository.save(invalidatedToken);
        // Tạo lại token mới
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        String newToken = jwtUtil.generateToken(user);
        return RefreshTokenResponse.builder()
                .token(newToken)
                .authenticated(true)
                .build();
    }

    @Override
    @Transactional
    public void logout(LogoutRequest request) {
        SignedJWT signedJWT;
        try {
            signedJWT = jwtUtil.verifyToken(request.getToken(), false);
        } catch (ParseException | JOSEException e) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        // Lấy thông tin từ token
        String tokenId = null;
        Date expiryTime = null;

        try {
            tokenId = signedJWT.getJWTClaimsSet().getJWTID();
            expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        } catch (ParseException e) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        // Kiểm tra nếu token đã hết hạn
        if (expiryTime.before(new Date())) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        // Lưu token vào danh sách invalidated token
        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .token(tokenId)
                .expiryTime(expiryTime)
                .build();
        invalidatedTokenRepository.save(invalidatedToken);
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Lấy username của người dùng hiện tại
        log.info("username: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Kiểm tra mật khẩu cũ có đúng không
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_CHANGE_PASSWORD);
        }

        // Kiểm tra mật khẩu mới và xác nhận mật khẩu có khớp không
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new AppException(ErrorCode.INVALID_CONFIRM_PASSWORD);
        }

        // Mã hóa mật khẩu mới
        String encodedNewPassword = passwordEncoder.encode(request.getNewPassword());

        // Cập nhật mật khẩu mới
        user.setPassword(encodedNewPassword);
        userRepository.save(user);
    }

}
