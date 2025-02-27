package com.swd392.skincare_products_sales_system.controller.admin;


import com.swd392.skincare_products_sales_system.dto.request.quiz.QuizCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.quiz.QuizUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.ApiResponse;
import com.swd392.skincare_products_sales_system.dto.response.QuizResponse;
import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/quizs")
@RequiredArgsConstructor
@Tag(name = "Quiz Controller")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class AdminQuizController {

    QuizService service;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a new quiz",
            description = "API to create a new quiz by providing required attributes such as title, questions, and answers."
    )
    public ApiResponse<QuizResponse> createQuiz(@RequestBody @Valid QuizCreationRequest request) {
        return ApiResponse.<QuizResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Quiz created successfully")
                .result(service.createQuiz(request))
                .build();
    }

    @PutMapping("/{quizId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Update a new quiz",
            description = "API to update a quiz by providing required attributes such as title, questions, and answers."
    )
    public ApiResponse<QuizResponse> updateQuiz(@RequestBody @Valid QuizUpdateRequest request, @PathVariable Long quizId ) {
        return ApiResponse.<QuizResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Quiz updated successfully")
                .result(service.updateQuiz(request,quizId))
                .build();
    }

    @DeleteMapping("/{quizId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Delete an existing quiz",
            description = "API to delete an existing quiz by providing the quiz ID."
    )
    public ApiResponse<QuizResponse> deleteQuiz(@PathVariable Long quizId) {
        service.deleteQuiz(quizId);
        return ApiResponse.<QuizResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Quiz deleted successfully")
                .build();
    }

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

    @PatchMapping("/changeStatus/{quizId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Change status",
            description = "Change a status"
    )
    public ApiResponse<Void> changeQuizStatus(@PathVariable Long quizId, @RequestParam Status status) {
        service.changeStatusQuiz(quizId, status);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Change status quiz successfully")
                .build();
    }



}
