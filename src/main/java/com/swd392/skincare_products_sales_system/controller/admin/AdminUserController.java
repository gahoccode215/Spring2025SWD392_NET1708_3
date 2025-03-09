package com.swd392.skincare_products_sales_system.controller.admin;

import com.swd392.skincare_products_sales_system.dto.request.user.UserCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.user.UserUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.ApiResponse;
import com.swd392.skincare_products_sales_system.dto.response.user.UserPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.user.UserResponse;
import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
@Tag(name = "User Controller")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class  AdminUserController {
    UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Tạo mới tài khoản", description = "API Tạo mới tài khoản")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Tạo mới tài khoản thành công")
                .result(userService.createUser(request))
                .build();
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete a user (ADMIN, MANAGER)", description = "API retrieve an id to delete user")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    ApiResponse<String> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ApiResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .result("User has been deleted")
                .build();
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Update a user (ADMIN, MANAGER)", description = "API retrieve value to change user attribute")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody @Valid UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Update user successfully")
                .result(userService.updateUser(request, userId))
                .build();
    }
    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a user detail (ADMIN, MANAGER)", description = "API retrieve an id to get user")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    ApiResponse<UserResponse> getUser(@PathVariable("userId") String userId) {
        return ApiResponse.<UserResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Get user detail successfully")
                .result(userService.getUser(userId))
                .build();
    }
    @PatchMapping("/change-status/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Change user status (ADMIN, MANAGER)", description = "API to change user status (ACTIVE/INACTIVE)")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ApiResponse<Void> changeUserStatus(@PathVariable String userId, @RequestParam Status status) {
        userService.changeUserStatus(userId, status);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Change status successfully")
                .build();
    }
    @GetMapping()
    @Operation(summary = "Get all users with options: search, pagination, sort, filter", description = "Retrieve all users with search, pagination, sorting, and filtering.")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<UserPageResponse> getAllUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String roleName,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order
    ) {
        return ApiResponse.<UserPageResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Get users successfully")
                .result(userService.getUsers(true, keyword, page, size, roleName, status, sortBy, order))
                .build();
    }
}
