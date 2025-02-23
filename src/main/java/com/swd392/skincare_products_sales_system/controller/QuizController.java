package com.swd392.skincare_products_sales_system.controller;

import com.swd392.skincare_products_sales_system.dto.request.QuizCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.QuizUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.request.SubmitQuiz;
import com.swd392.skincare_products_sales_system.dto.response.ApiResponse;
import com.swd392.skincare_products_sales_system.dto.response.QuizResponse;
import com.swd392.skincare_products_sales_system.dto.response.ResultResponse;
import com.swd392.skincare_products_sales_system.model.Quiz;
import com.swd392.skincare_products_sales_system.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@RequestMapping("/quizs")
@RequiredArgsConstructor
@Tag(name = "Quiz Controller")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuizController {

    QuizService service;

    @GetMapping("/{quizId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get an existing quiz",
            description = "API to get an existing quiz by providing the quiz ID and updated attributes such as title, questions, and answers."
    )
    public ApiResponse<QuizResponse> getQuizById(@PathVariable Long quizId) {
        return ApiResponse.<QuizResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Get Quiz successfully")
                .result(service.getQuizById(quizId))
                .build();
    }

    @PostMapping("/submit/{quizId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Submit an quiz",
            description = "API to get an existing quiz by providing the quiz ID and updated attributes such as title, questions, and answers."
    )

    public ApiResponse<ResultResponse> submitQuiz(@Valid @PathVariable Long quizId, @RequestBody SubmitQuiz submitQuiz) {
        return ApiResponse.<ResultResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Submit Quiz successfully")
                .result(service.submitQuiz(submitQuiz, quizId))
                .build();
    }

    @GetMapping("/getQuizAll")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get All quiz",
            description = "Get all quiz (All Role)"
    )
    public ApiResponse<List<Quiz>> getAllQuiz() {
        return ApiResponse.<List<Quiz>>builder()
                .code(HttpStatus.OK.value())
                .result(service.getAllQuiz())
                .message("Get all quiz successfully")
                .build();
    }

}
