    package com.swd392.skincare_products_sales_system.service.impl;

import com.swd392.skincare_products_sales_system.dto.request.quiz.*;
import com.swd392.skincare_products_sales_system.dto.response.AnswerResponse;
import com.swd392.skincare_products_sales_system.dto.response.QuestionResponse;
import com.swd392.skincare_products_sales_system.dto.response.QuizResponse;
import com.swd392.skincare_products_sales_system.dto.response.ResultResponse;
import com.swd392.skincare_products_sales_system.entity.user.User;
import com.swd392.skincare_products_sales_system.enums.ErrorCode;
import com.swd392.skincare_products_sales_system.enums.SkinType;
import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.exception.AppException;
import com.swd392.skincare_products_sales_system.entity.*;
import com.swd392.skincare_products_sales_system.entity.quiz.Answer;
import com.swd392.skincare_products_sales_system.entity.quiz.Question;
import com.swd392.skincare_products_sales_system.entity.quiz.Quiz;
import com.swd392.skincare_products_sales_system.entity.quiz.Result;
import com.swd392.skincare_products_sales_system.repository.*;
import com.swd392.skincare_products_sales_system.service.QuizService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuizServiceImpl implements QuizService {

    QuizRepository quizRepository;
    UserRepository userRepository;
    QuestionRepository questionRepository;
    AnswerRepository answerRepository;
    ResultRepository resultRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public QuizResponse createQuiz(QuizCreationRequest quizRequest) {
        if (quizRepository.findQuizByTitle(quizRequest.getTitle()).isPresent()) {
            throw new AppException(ErrorCode.TITLE_EXISTED);
        }
        Quiz quiz = Quiz.builder()
                .title(quizRequest.getTitle())
                .description(quizRequest.getDescription())
                .status(Status.ACTIVE)
                .build();
        quiz.setIsDeleted(false);
        quizRepository.save(quiz);

        for (QuestionRequest questionRequest : quizRequest.getQuestions()) {
            Question question = Question.builder()
                    .title(questionRequest.getTitle())
                    .quiz(quiz)
                    .build();
            question.setIsDeleted(false);
            questionRepository.save(question);

            for (AnswerRequest answerRequest : questionRequest.getAnswers()) {
                Answer answer = Answer.builder()
                        .answerText(answerRequest.getAnswerText())
                        .skinType(answerRequest.getSkinType())
                        .question(question)
                        .build();
                answer.setIsDeleted(false);
                answerRepository.save(answer);
            }
        }
        return QuizResponse.builder()
                .id(quiz.getId())
                .title(quiz.getTitle())
                .description(quiz.getDescription())
                .status(Status.ACTIVE)
                .isDeleted(false)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteQuiz(long quizId) {
        Quiz quiz = quizRepository.findQuizById(quizId)
                .orElseThrow(() -> new AppException(ErrorCode.QUIZ_EXISTED));
        quiz.setIsDeleted(true);
        quiz.setStatus(Status.INACTIVE);
        quizRepository.save(quiz);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeStatusQuiz(long quizId, Status status) {
        Quiz quiz = quizRepository.findSkincareServiceByIdAndIsDeletedIsFalse(quizId)
                .orElseThrow(() -> new AppException(ErrorCode.QUIZ_NOT_EXISTED));
        if (quiz.getStatus() == Status.ACTIVE) {
            quiz.setStatus(Status.INACTIVE);
        } else {
            quiz.setStatus(Status.ACTIVE);
        }
        quizRepository.save(quiz);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public QuizResponse updateQuiz(QuizUpdateRequest quizUpdateRequest, Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new AppException(ErrorCode.QUIZ_NOT_FOUND));

        if (!quiz.getTitle().equals(quizUpdateRequest.getTitle())) {
            if (quizRepository.findQuizByTitle(quizUpdateRequest.getTitle()).isPresent()) {
                throw new AppException(ErrorCode.TITLE_EXISTED);
            }
        }

        quiz.setTitle(quizUpdateRequest.getTitle());
        quiz.setStatus(quizUpdateRequest.getStatus());
        quiz.setDescription(quizUpdateRequest.getDescription());
        quiz.setIsDeleted(false);
        quizRepository.save(quiz);

        for (QuestionRequest questionRequest : quizUpdateRequest.getQuestions()) {
            Question question = questionRepository.findById(questionRequest.getQuestionId())
                    .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));

            question.setTitle(questionRequest.getTitle());
            question.setQuiz(quiz);
            questionRepository.save(question);

            for (AnswerRequest answerRequest : questionRequest.getAnswers()) {
                Answer answer = answerRepository.findById(answerRequest.getAnswerId())
                        .orElseThrow(() -> new AppException(ErrorCode.ANSWER_NOT_FOUND));

                answer.setAnswerText(answerRequest.getAnswerText());
                answer.setQuestion(question);
                answerRepository.save(answer);
            }
        }

        // Tạo danh sách QuestionResponse từ Quiz đã cập nhật
        List<QuestionResponse> questionResponses = quiz.getQuestions().stream()
                .map(question -> {
                    List<AnswerResponse> answerResponses = question.getAnswers().stream()
                            .map(answer -> AnswerResponse.builder()
                                    .answerId(answer.getId())
                                    .answerText(answer.getAnswerText())
                                    .build())
                            .collect(Collectors.toList());

                    return QuestionResponse.builder()
                            .questionId(question.getId())
                            .title(question.getTitle())
                            .answers(answerResponses)
                            .build();
                })
                .collect(Collectors.toList());

        // Trả về QuizResponse với đầy đủ thông tin
        return QuizResponse.builder()
                .id(quiz.getId())
                .title(quiz.getTitle())
                .description(quiz.getDescription())
                .status(quiz.getStatus())
                .question(questionResponses)
                .build();
    }

    @Override
    public QuizResponse getQuizById(long quizId) {
        Quiz quiz = quizRepository.findSkincareServiceByIdAndIsDeletedIsFalse(quizId)
                .orElseThrow(() -> new AppException(ErrorCode.QUIZ_NOT_EXISTED));

        List<QuestionResponse> questionResponses = quiz.getQuestions().stream()
                .map(question -> {
                    List<AnswerResponse> answerResponses = question.getAnswers().stream()
                            .map(answer -> AnswerResponse.builder()
                                    .answerId(answer.getId())
                                    .answerText(answer.getAnswerText())
                                    .skinType(answer.getSkinType())
                                    .isDeleted(answer.getIsDeleted())
                                    .skinType(answer.getSkinType())
                                    .build())
                            .collect(Collectors.toList());

                    return QuestionResponse.builder()
                            .questionId(question.getId())
                            .title(question.getTitle())
                            .isDeleted(question.getIsDeleted())
                            .answers(answerResponses)
                            .build();
                })
                .collect(Collectors.toList());

        return QuizResponse.builder()
                .id(quiz.getId())
                .title(quiz.getTitle())
                .description(quiz.getDescription())
                .status(quiz.getStatus())
                .isDeleted(quiz.getIsDeleted())
                .question(questionResponses)
                .build();
    }

    @Override
    public ResultResponse submitQuiz(SubmitQuiz submitQuiz, Long quizId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new AppException(ErrorCode.QUIZ_NOT_FOUND));

        SkinType result = calculateQuizResult(quiz, submitQuiz.getAnswers());

        Result resultEntity = new Result();
        resultEntity.setQuiz(quiz);
        resultEntity.setSkinType(result);
        resultEntity.setUser(user);
        resultRepository.save(resultEntity);
        ResultResponse resultResponse = new ResultResponse();
        resultResponse.setResult(result.toString());
        return resultResponse;
    }

    @Override
    public SkinType calculateQuizResult(Quiz quiz, Map<Long, Long> answers) {
        Map<SkinType, Long> skinTypeCount = new HashMap<>();
        skinTypeCount.put(SkinType.DRY_SKIN, 0L);
        skinTypeCount.put(SkinType.SENSITIVE_SKIN, 0L);
        skinTypeCount.put(SkinType.OILY_SKIN, 0L);
        skinTypeCount.put(SkinType.NORMAL_SKIN, 0L);

        for (Question question : quiz.getQuestions()) {
            Long answerId = answers.get(question.getId());

            if (answerId != null) {
                Answer answer = answerRepository.findById(answerId)
                        .orElseThrow(() -> new AppException(ErrorCode.ANSWER_NOT_FOUND));

                SkinType skinType = answer.getSkinType();
                if (skinType != null) {
                    skinTypeCount.merge(skinType, 1L, Long::sum);
                }
            }
        }

        SkinType result = skinTypeCount.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(SkinType.SENSITIVE_SKIN);

        if (skinTypeCount.get(SkinType.DRY_SKIN).equals(skinTypeCount.get(SkinType.OILY_SKIN))) {
            return SkinType.COMBINATION_SKIN;
        }
        if (skinTypeCount.get(SkinType.DRY_SKIN).equals(skinTypeCount.get(SkinType.NORMAL_SKIN))) {
            return SkinType.COMBINATION_SKIN;
        }
        if (skinTypeCount.get(SkinType.OILY_SKIN).equals(skinTypeCount.get(SkinType.NORMAL_SKIN))) {
            return SkinType.COMBINATION_SKIN;
        }
        return result;
    }


    @Override
    public List<QuizResponse> getAllQuiz() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        List<Quiz> quizzes;
        if (user.getRole().getId() == 2 || user.getRole().getId() == 6) {
            quizzes = quizRepository.findAll()
                    .stream()
                    .toList();
        } else {
            quizzes = quizRepository.findAll()
                    .stream()
                    .filter(quiz -> !quiz.getIsDeleted() && quiz.getStatus() == Status.ACTIVE)
                    .collect(Collectors.toList());
        }

        return quizzes.stream()
                .map(this::convertToQuizResponse)
                .collect(Collectors.toList());
    }

    private QuizResponse convertToQuizResponse(Quiz quiz) {
        List<QuestionResponse> questionResponses = quiz.getQuestions().stream()
                .map(question -> {
                    List<AnswerResponse> answerResponses = question.getAnswers().stream()
                            .map(answer -> AnswerResponse.builder()
                                    .answerId(answer.getId())
                                    .answerText(answer.getAnswerText())
                                    .skinType(answer.getSkinType())
                                    .isDeleted(answer.getIsDeleted())
                                    .build())
                            .collect(Collectors.toList());

                    return QuestionResponse.builder()
                            .questionId(question.getId())
                            .title(question.getTitle())
                            .isDeleted(question.getIsDeleted())
                            .answers(answerResponses)
                            .build();
                })
                .collect(Collectors.toList());

        return QuizResponse.builder()
                .id(quiz.getId())
                .title(quiz.getTitle())
                .isDeleted(quiz.getIsDeleted())
                .description(quiz.getDescription())
                .status(quiz.getStatus())
                .question(questionResponses)
                .build();
    }
}







