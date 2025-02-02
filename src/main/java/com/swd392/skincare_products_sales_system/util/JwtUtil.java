package com.swd392.skincare_products_sales_system.util;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.swd392.skincare_products_sales_system.dto.request.IntrospectRequest;
import com.swd392.skincare_products_sales_system.dto.response.IntrospectResponse;
import com.swd392.skincare_products_sales_system.enums.ErrorCode;
import com.swd392.skincare_products_sales_system.exception.AppException;
import com.swd392.skincare_products_sales_system.model.Role;
import com.swd392.skincare_products_sales_system.repository.InvalidatedTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;

    @Value("${jwt.valid-duration}")
    private long VALID_DURATION;

    @Value("${jwt.refreshable-duration}")
    private long REFRESHABLE_DURATION;


    /**
     * Ensures the signing key is at least 64 bytes for HS512 encryption.
     *
     * @return Secure byte array for signing and verification.
     */
    private byte[] getSigningKey() {
        byte[] keyBytes = SIGNER_KEY.getBytes(StandardCharsets.UTF_8);

        if (keyBytes.length < 64) {
            log.warn("SIGNER_KEY is too short! Encoding to Base64 to ensure security.");
            keyBytes = Base64.getEncoder().encode(keyBytes);
        }
        return keyBytes;
    }

    /**
     * Generates a JWT token (Access or Refresh).
     *
     * @param username       User identifier
     * @param roles          User roles (Many-to-Many)
     * @param isRefreshToken True for refresh token, False for access token
     * @return Signed JWT token as String
     */
    public String generateToken(String username, Set<Role> roles, boolean isRefreshToken) {
        try {
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

            // Convert Set<Role> to a List of role names
            List<String> roleNames = roles.stream().map(Role::getName).collect(Collectors.toList());

            JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder()
                    .subject(username)
                    .issuer("swd392.com")
                    .issueTime(new Date())
                    .expirationTime(Date.from(Instant.now().plus(
                            isRefreshToken ? REFRESHABLE_DURATION : VALID_DURATION, ChronoUnit.SECONDS)))
                    .jwtID(UUID.randomUUID().toString())
                    .claim("roles", roleNames) // Store roles as a list
                    .claim("scope", isRefreshToken ? "refresh" : "access");

            SignedJWT signedJWT = new SignedJWT(header, claimsBuilder.build());
            signedJWT.sign(new MACSigner(getSigningKey()));

            log.info("Generated {} token for user: {} with roles: {}",
                    isRefreshToken ? "Refresh" : "Access", username, roleNames);
            return signedJWT.serialize();
        } catch (JOSEException e) {
            log.error("Token generation failed for user: {}", username, e);
            throw new RuntimeException("Cannot create token", e);
        }
    }


    /**
     * Extracts the username from a JWT token.
     *
     * @param token JWT string
     * @return Extracted username
     */
    public String extractUsername(String token) {
        try {
            return SignedJWT.parse(token).getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            log.error("Failed to extract username from token", e);
            throw new RuntimeException("Invalid token format", e);
        }
    }

    /**
     * Extracts the role from a JWT token.
     *
     * @param token JWT string
     * @return Extracted user role
     */
    public String extractRole(String token) {
        try {
            return SignedJWT.parse(token).getJWTClaimsSet().getStringClaim("role");
        } catch (ParseException e) {
            log.error("Failed to extract role from token", e);
            throw new RuntimeException("Invalid token format", e);
        }
    }

    /**
     * Verifies and parses a JWT token.
     *
     * @param token     JWT string
     * @param isRefresh If true, verifies as a refresh token; otherwise, verifies as an access token.
     * @return SignedJWT object if valid
     * @throws JOSEException  Invalid signature
     * @throws ParseException Invalid format
     */
    public SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(getSigningKey());

        boolean verified = signedJWT.verify(verifier);
        boolean correctScope = isRefresh
                ? "refresh".equals(signedJWT.getJWTClaimsSet().getStringClaim("scope"))
                : "access".equals(signedJWT.getJWTClaimsSet().getStringClaim("scope"));

        if (!verified || !correctScope || signedJWT.getJWTClaimsSet().getExpirationTime().before(new Date())) {
            log.warn("Token verification failed (Invalid or expired token)");
            throw new RuntimeException("Invalid or expired token");
        }

        log.info("{} token verified successfully", isRefresh ? "Refresh" : "Access");
        return signedJWT;
    }

    /**
     * Generates a refresh token.
     *
     * @param username User identifier
     * @return Refresh token as String
     */
    public String generateRefreshToken(String username) {
        try {
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

            JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                    .subject(username)
                    .issuer("swd392.com")
                    .issueTime(new Date())
                    .expirationTime(Date.from(Instant.now().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS)))
                    .jwtID(UUID.randomUUID().toString())
                    .claim("scope", "refresh") // Mark as refresh token
                    .build();

            SignedJWT signedJWT = new SignedJWT(header, jwtClaimsSet);
            signedJWT.sign(new MACSigner(getSigningKey()));

            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("Cannot create refresh token", e);
        }
    }
    public String decodeToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Invalid Token", e);
        }
    }
    public IntrospectResponse introspect(IntrospectRequest request) {
        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }

        return IntrospectResponse.builder().valid(isValid).build();
    }
}
