package com.swd392.skincare_products_sales_system.service;

import com.swd392.skincare_products_sales_system.dto.request.routine.RoutineCreateRequest;
import com.swd392.skincare_products_sales_system.dto.response.RoutineResponse;
import com.swd392.skincare_products_sales_system.entity.routine.Routine;

import java.util.List;

public interface RoutineService {
    RoutineResponse makeRoutine (RoutineCreateRequest request, Long bookingOrderId);
    RoutineResponse cancelRoutine (RoutineCreateRequest request, Long bookingOrderId);
    RoutineResponse updateRoutine (RoutineCreateRequest request, Long bookingOrderId);
    Routine updateStatusRoutine(Long routineId);
    List<RoutineResponse> getAllRoutines();
    RoutineResponse getRoutineById(Long id);
    List<RoutineResponse> getRoutineOfCustomer();

}
