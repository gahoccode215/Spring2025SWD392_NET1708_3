package com.swd392.skincare_products_sales_system.controller.admin;

import com.swd392.skincare_products_sales_system.dto.response.ApiResponse;
import com.swd392.skincare_products_sales_system.dto.response.DashboardResponse;
import com.swd392.skincare_products_sales_system.service.DashboardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/dashboard")
@Tag(name = "Dashboard Controller")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminDashboardController {
    DashboardService dashboardService;

    @GetMapping
    public ApiResponse<DashboardResponse> getDashboard(){
        return ApiResponse.<DashboardResponse>builder()
                .result(dashboardService.getDashboardData())
                .message("Lấy thông tin dashboard thành công")
                .code(HttpStatus.OK.value())
                .build();
    }
}
