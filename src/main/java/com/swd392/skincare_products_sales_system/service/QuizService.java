package com.swd392.skincare_products_sales_system.service;

import com.swd392.skincare_products_sales_system.dto.request.quiz.QuizCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.quiz.QuizUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.request.quiz.SubmitQuiz;
import com.swd392.skincare_products_sales_system.dto.response.QuizResponse;
import com.swd392.skincare_products_sales_system.dto.response.ResultResponse;
import com.swd392.skincare_products_sales_system.enums.SkinType;
import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.model.Quiz;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

public interface QuizService {
     QuizResponse createQuiz(@Valid QuizCreationRequest quizRequest);
     void deleteQuiz(long quizId);
     void changeStatusQuiz (long quizId, Status status);
     QuizResponse updateQuiz(@Valid QuizUpdateRequest quizUpdateRequest, Long quizId);
     QuizResponse getQuizById(long quizId);
     ResultResponse submitQuiz(SubmitQuiz submitQuiz, Long quizId );
     SkinType calculateQuizResult(Quiz quiz, Map<Long, Long> answers);
     List<Quiz> getAllQuiz();

}
