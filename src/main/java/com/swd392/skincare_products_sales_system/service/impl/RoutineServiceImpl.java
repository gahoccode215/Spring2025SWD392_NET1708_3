package com.swd392.skincare_products_sales_system.service.impl;

import com.swd392.skincare_products_sales_system.dto.request.routine.DailyRoutineRequest;
import com.swd392.skincare_products_sales_system.dto.request.routine.RoutineCreateRequest;
import com.swd392.skincare_products_sales_system.dto.request.routine.StepRequest;
import com.swd392.skincare_products_sales_system.dto.response.DailyRoutineResponse;
import com.swd392.skincare_products_sales_system.dto.response.RoutineResponse;
import com.swd392.skincare_products_sales_system.dto.response.StepResponse;
import com.swd392.skincare_products_sales_system.enums.BookingStatus;
import com.swd392.skincare_products_sales_system.enums.ErrorCode;
import com.swd392.skincare_products_sales_system.enums.RoutineStatusEnum;
import com.swd392.skincare_products_sales_system.exception.AppException;
import com.swd392.skincare_products_sales_system.model.*;
import com.swd392.skincare_products_sales_system.repository.*;
import com.swd392.skincare_products_sales_system.service.RoutineService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoutineServiceImpl implements RoutineService {

    BookingRepository bookingRepository;
    UserRepository userRepository;
    RoutineRepository routineRepository;
    StepRepository stepRepository;
    DailyRoutineRepository dailyRoutineRepository;
    ProductRepository productRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoutineResponse makeRoutine(RoutineCreateRequest request, Long bookingOrderId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        BookingOrder bookingOrder = bookingRepository.findByIdAndIsDeletedFalse(bookingOrderId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_EXIST));

        Routine routine = Routine.builder()
                .description(request.getDescription())
                .routineName(request.getRoutineName())
                .routineStatus(RoutineStatusEnum.PROCESSING)
                .startDate(request.getStartDate().toLocalDate())
                .endDate(request.getEndDate().toLocalDate())
                .user(user)
                .build();

        bookingOrder.setStatus(BookingStatus.IN_PROGRESS_ROUTINE);
        bookingOrder.setRoutine(routine);
        bookingRepository.save(bookingOrder);

        routineRepository.save(routine);

        List<DailyRoutine> dailyRoutines = new ArrayList<>();

        for (DailyRoutineRequest dailyRoutineRequest : request.getDailyRoutines()) {
            DailyRoutine dailyRoutine = DailyRoutine.builder()
                    .routine(routine)
                    .date(dailyRoutineRequest.getDate())
                    .routineStatus(routine.getRoutineStatus())
                    .build();

            dailyRoutine.setIsDeleted(false);
            List<Step> steps = new ArrayList<>();
            dailyRoutine.setSteps(steps);

            dailyRoutineRepository.save(dailyRoutine);

            if (dailyRoutineRequest.getSteps() != null) {
                for (StepRequest stepRequest : dailyRoutineRequest.getSteps()) {
                    Step step = Step.builder()
                            .stepNumber(stepRequest.getStepNumber())
                            .timeOfDay(stepRequest.getTimeOfDay())
                            .action(stepRequest.getAction())
                            .description(stepRequest.getDescription())
                            .routineStatus(routine.getRoutineStatus())
                            .dailyRoutine(dailyRoutine)
                            .product(stepRequest.getProductId() != null
                                    ? productRepository.findById(stepRequest.getProductId())
                                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED))
                                    : null)
                            .build();

                    step.setIsDeleted(false);
                    stepRepository.save(step);
                    steps.add(step);
                }
            }

            dailyRoutines.add(dailyRoutine);
        }
        routine.setDailyRoutines(dailyRoutines);
        return convertToRoutineResponse(routine);
    }


    private RoutineResponse convertToRoutineResponse(Routine routine) {
        RoutineResponse response = new RoutineResponse();
        response.setId(routine.getId());
        response.setRoutineName(routine.getRoutineName());
        response.setDescription(routine.getDescription());
        response.setStartDate(routine.getStartDate());
        response.setEndDate(routine.getEndDate());
        response.setRoutineStatus(routine.getRoutineStatus());
        response.setUserId(routine.getUser().getId());

        List<DailyRoutineResponse> dailyRoutineResponses = routine.getDailyRoutines().stream()
                .map(dr -> {
                    DailyRoutineResponse drResponse = new DailyRoutineResponse();
                    drResponse.setId(dr.getId());
                    drResponse.setDate(dr.getDate());
                    drResponse.setRoutineStatus(dr.getRoutineStatus());
                    drResponse.setSteps(dr.getSteps().stream()
                            .map(step -> {
                                StepResponse stepResponse = new StepResponse();
                                stepResponse.setId(step.getId());
                                stepResponse.setStepNumber(step.getStepNumber());
                                stepResponse.setTimeOfDay(step.getTimeOfDay());
                                stepResponse.setAction(step.getAction());
                                stepResponse.setDescription(step.getDescription());
                                stepResponse.setProductId(step.getProduct() != null ? step.getProduct().getId() : null);
                                return stepResponse;
                            }).collect(Collectors.toList()));
                    return drResponse;
                }).collect(Collectors.toList());

        response.setDailyRoutines(dailyRoutineResponses);
        return response;
    }


    @Override
    public RoutineResponse cancelRoutine(RoutineCreateRequest request, Long bookingOrderId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
//        String username = authentication.getName();
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
//
//        BookingOrder bookingOrder = bookingRepository.findByIdAndIsDeletedFalse(bookingOrderId)
//                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_EXIST));
//
//        Routine routine = routineRepository.findByIdAndIsDeletedFalse()
//                .orElseThrow(() -> new AppException(ErrorCode.EXPERT_NOT_EXIST));
        return null;
    }

    @Override
    public RoutineResponse updateRoutine(RoutineCreateRequest request, Long bookingOrderId) {
        return null;
    }
}
