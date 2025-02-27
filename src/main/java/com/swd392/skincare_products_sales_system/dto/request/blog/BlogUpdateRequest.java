package com.swd392.skincare_products_sales_system.dto.request.blog;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.swd392.skincare_products_sales_system.enums.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlogUpdateRequest {
    @NotNull(message = "Content cannot be null")
    @Size(min = 5)
    String content;
    @NotNull(message = "BlogName cannot be null")
    @Size(min = 5)
    String blogName;
    @Enumerated(EnumType.STRING)
    Status status;
    LocalDate date;
    @NotNull(message = "Image cannot be null")
    String image;
    @NotNull
    @Size(min = 5)
    String description;
}
