package com.swd392.skincare_products_sales_system.controller.admin;

import com.swd392.skincare_products_sales_system.dto.request.product.BatchCreationRequest;
import com.swd392.skincare_products_sales_system.dto.response.*;
import com.swd392.skincare_products_sales_system.dto.response.product.BatchPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.product.BatchResponse;
import com.swd392.skincare_products_sales_system.service.BatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/batchs")
@RequiredArgsConstructor
@Tag(name = "Batch Controller")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminBatchController {
    BatchService batchService;

    @PostMapping
    @Operation(summary = "Import batch (ADMIN, MANAGER)", description = "API retrieve attribute to import batch")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<BatchResponse> importBatch(@RequestBody @Valid BatchCreationRequest request)
    {

        return ApiResponse.<BatchResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("import batch successfully")
                .result(batchService.createBatch(request))
                .build();
    }
    @GetMapping()
    @Operation(summary = "Get all batches with options(ADMIN, MANAGER)  ", description = "Retrieve all batches with search, pagination, sorting, and filtering.")
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ApiResponse<BatchPageResponse> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<BatchPageResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Get batches successfully")
                .result(batchService.getAllBatches(page, size))
                .build();
    }
}
