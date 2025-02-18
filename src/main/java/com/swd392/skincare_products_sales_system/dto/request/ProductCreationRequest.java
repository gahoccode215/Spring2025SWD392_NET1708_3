package com.swd392.skincare_products_sales_system.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductCreationRequest {
    @NotBlank(message = "product name can not blank")
    String name;
    Double price;
    String description;
    MultipartFile thumbnail;
//    String usageInstruction;
//    LocalDate expiryDate;
//    Long brand_id;
//    Long origin_id;
//    Long skin_type_id;
    String category_id;
//    Set<Long> feature_ids;
}
