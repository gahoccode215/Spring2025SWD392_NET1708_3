package com.swd392.skincare_products_sales_system.service.impl;

import com.swd392.skincare_products_sales_system.dto.response.OrderResponse;
import com.swd392.skincare_products_sales_system.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VNPayPaymentService implements PaymentService {
    private static final String VNPAY_URL = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    private static final String TERMINAL_ID = "M0R9Z6E1";
    private static final String SECRET_KEY = "6Z3AGDLVVDYXEAE3JKBNI6LN2ARZXXST";
    @Override
    public String createPaymentUrl(OrderResponse order) {
        String vnp_TxnRef = String.valueOf(System.currentTimeMillis()); // Mã giao dịch duy nhất
        String vnp_Amount = String.valueOf(order.getTotalAmount().longValue() * 100); // Chuyển đổi sang đơn vị VNPay
        String vnp_CreateDate = getCurrentDate();
        String vnp_ExpireDate = getExpireDate(15);

        Map<String, String> vnp_Params = new TreeMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", TERMINAL_ID);
        vnp_Params.put("vnp_Amount", vnp_Amount);
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh Toan Don Hang #" + order.getOrderId());
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", "https://skynbeauty.vercel.app/vnpay-return"); // URL nhận kết quả
        vnp_Params.put("vnp_IpAddr", "172.19.112.1");
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        // Tạo chữ ký bảo mật HMAC-SHA512
        vnp_Params.remove("vnp_SecureHash"); // Xóa trước khi tạo chữ ký
        String secureHash = hmacSHA512(SECRET_KEY, vnp_Params);
        vnp_Params.put("vnp_SecureHash", secureHash);

        // Tạo URL thanh toán
        return VNPAY_URL + "?" + vnp_Params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
    }

    @Override
    public boolean checkSignature(HttpServletRequest request) {
        Map<String, String> vnp_Params = new TreeMap<>();
        String vnp_SecureHash = request.getParameter("vnp_SecureHash");

        for (String key : request.getParameterMap().keySet()) {
            if (!key.equals("vnp_SecureHash")) {
                vnp_Params.put(key, request.getParameter(key));
            }
        }

        // Tạo chữ ký từ tham số phản hồi
        String generatedSignature = hmacSHA512(SECRET_KEY, vnp_Params);
        return generatedSignature.equalsIgnoreCase(vnp_SecureHash);

    }
    private String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        return formatter.format(new Date());
    }

    private String hmacSHA512(String key, Map<String, String> params) {
        try {
            StringBuilder data = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (!entry.getKey().equals("vnp_SecureHash")) {
                    data.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                }
            }
            data.deleteCharAt(data.length() - 1);

            Mac sha512Hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            sha512Hmac.init(secretKey);

            byte[] hashBytes = sha512Hmac.doFinal(data.toString().getBytes(StandardCharsets.UTF_8));
            return Hex.encodeHexString(hashBytes).toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo chữ ký HMAC SHA512", e);
        }
    }
    private String getExpireDate(int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minutes); // Cộng thêm số phút
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        return formatter.format(calendar.getTime());
    }
}
