package com.swd392.skincare_products_sales_system.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class PostmarkService {
    private static final Logger log = LoggerFactory.getLogger(PostmarkService.class);
    @Value("${postmark.api.key}")
    private String apiKey;

    @Value("${postmark.from.address}")
    private String fromAddress;

    private final RestTemplate restTemplate;

    public PostmarkService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendEmail(String toAddress, String subject, String body) {
        String url = "https://api.postmarkapp.com/email";

        log.info("Postmark API Key: {}", apiKey);
        log.info("From Address: {}", fromAddress);
        log.info("Sending email to: {}", toAddress);

        // Tạo headers cho HTTP Request
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("X-Postmark-Server-Token", apiKey);
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

        // Tạo nội dung email
        Map<String, Object> email = new HashMap<>();
        email.put("From", fromAddress);
        email.put("To", toAddress);
        email.put("Subject", subject);
        email.put("HtmlBody", body);
        email.put("TextBody", "This is a plain text version of the email.");
        email.put("MessageStream", "outbound"); // Dành cho email giao dịch (Transactional)

        // Gửi email
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(email, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Email sent successfully to: {}", toAddress);
            } else {
                throw new RuntimeException("Failed to send email. Response: " + response.getBody());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email due to exception: " + e.getMessage());
        }
    }

}
