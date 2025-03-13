package com.swd392.skincare_products_sales_system.service.impl;

import com.swd392.skincare_products_sales_system.enums.ErrorCode;
import com.swd392.skincare_products_sales_system.enums.RoutineStatusEnum;
import com.swd392.skincare_products_sales_system.exception.AppException;
import com.swd392.skincare_products_sales_system.model.User;
import com.swd392.skincare_products_sales_system.model.routine.Step;
import com.swd392.skincare_products_sales_system.repository.StepRepository;
import com.swd392.skincare_products_sales_system.repository.UserRepository;
import com.swd392.skincare_products_sales_system.service.StepService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StepServiceImpl implements StepService {
    StepRepository stepRepository;
    UserRepository userRepository;

    @Override
    public Step updateStep(Long stepId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        Step step = stepRepository.findById(stepId)
                .orElseThrow(() -> new AppException(ErrorCode.STEP_NOT_EXISTED));
        
        LocalDate today = LocalDate.now();
        if (!step.getDailyRoutine().getDate().equals(today)) {
            throw new AppException(ErrorCode.STEP_NOT_TODAY);
        }

        step.setRoutineStatus(RoutineStatusEnum.DONE);
        return stepRepository.save(step);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void updateDailyStepStatus() {
        LocalDate today = LocalDate.now();
        List<Step> todaySteps = stepRepository.findByDailyRoutineDate(today);
        for (Step step : todaySteps) {
            User user = step.getDailyRoutine().getRoutine().getUser();
                boolean isCompleted = step.getLastCompletedDate() != null && step.getLastCompletedDate().equals(today);
            step.setRoutineStatus(isCompleted ? RoutineStatusEnum.DONE : RoutineStatusEnum.NOT_DONE);
            stepRepository.save(step);
        }
    }
}
