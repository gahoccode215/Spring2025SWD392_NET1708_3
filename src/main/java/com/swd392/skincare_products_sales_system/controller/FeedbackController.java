package com.swd392.skincare_products_sales_system.controller;

import com.swd392.skincare_products_sales_system.dto.request.product.FeedBackCreationRequest;
import com.swd392.skincare_products_sales_system.dto.response.ApiResponse;
import com.swd392.skincare_products_sales_system.dto.response.product.FeedBackResponse;
import com.swd392.skincare_products_sales_system.service.FeedBackService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedbacks")
@Tag(name = "Feedback Controller")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FeedbackController {
    FeedBackService feedBackService;

    @PostMapping("/{productId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ApiResponse<FeedBackResponse> createFeedback(@RequestBody FeedBackCreationRequest request,
                                                        @PathVariable("productId") String productId) {
        FeedBackResponse response = feedBackService.createFeedBack(request, productId);
        return ApiResponse.<FeedBackResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Đánh giá sản phẩm thành công")
                .result(response)
                .build();
    }
}
