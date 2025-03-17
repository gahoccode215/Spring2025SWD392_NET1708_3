package com.swd392.skincare_products_sales_system.service.impl;


import com.swd392.skincare_products_sales_system.entity.routine.DailyRoutine;
import com.swd392.skincare_products_sales_system.entity.routine.Routine;
import com.swd392.skincare_products_sales_system.enums.ErrorCode;
import com.swd392.skincare_products_sales_system.enums.RoutineStatusEnum;
import com.swd392.skincare_products_sales_system.exception.AppException;
import com.swd392.skincare_products_sales_system.repository.DailyRoutineRepository;
import com.swd392.skincare_products_sales_system.service.DailyService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DailyRoutineServiceImpl implements DailyService {

    DailyRoutineRepository dailyRoutineRepository;
    @Override
    public DailyRoutine updateDailyRoutine(Long dailyRoutineId) {
        DailyRoutine dailyRoutine = dailyRoutineRepository.findById(dailyRoutineId)
                .orElseThrow(() -> new AppException(ErrorCode.DAILY_ROUTINE_EXISTED));
        dailyRoutine.setRoutineStatus(RoutineStatusEnum.DONE);
        dailyRoutineRepository.save(dailyRoutine);
        return dailyRoutine;
    }
}
