package com.swd392.skincare_products_sales_system.service.impl;

import com.swd392.skincare_products_sales_system.dto.request.SkincareCreateRequest;
import com.swd392.skincare_products_sales_system.dto.request.SkincareUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.SkincareServiceResponse;
import com.swd392.skincare_products_sales_system.enums.*;
import com.swd392.skincare_products_sales_system.exception.AppException;
import com.swd392.skincare_products_sales_system.model.SkincareService;
import com.swd392.skincare_products_sales_system.model.User;
import com.swd392.skincare_products_sales_system.repository.BookingRepository;
import com.swd392.skincare_products_sales_system.repository.SkincareServiceRepository;
import com.swd392.skincare_products_sales_system.repository.UserRepository;
import com.swd392.skincare_products_sales_system.service.SkincareServiceInterface;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SkincareServiceImpl implements SkincareServiceInterface {

    SkincareServiceRepository serviceRepository;
    UserRepository userRepository;
    BookingRepository bookingRepository;





    @Override
    public SkincareServiceResponse createSkincareService(SkincareCreateRequest request) {
        Optional<SkincareService> existingServiceByName = serviceRepository.findSkincareServiceByServiceName(request.getServiceName());
        if (existingServiceByName.isPresent()) {
            throw new AppException(ErrorCode.SERVICENAME_EXISTED);
        }
        SkincareService skincareService = SkincareService.builder()
                .serviceName(request.getServiceName())
                .description(request.getDescription())
                .price(request.getPrice())
                .status(request.getStatus())
                .build();

        serviceRepository.save(skincareService);
        return SkincareServiceResponse.builder()
                .id(skincareService.getId())
                .serviceName(skincareService.getServiceName())
                .description(skincareService.getDescription())
                .price(skincareService.getPrice())
                .status(skincareService.getStatus())
                .build();
    }




    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteService(long skincareId) {
        SkincareService skincareService = serviceRepository.findSkincareServiceById(skincareId)
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_EXIST));
        skincareService.setDeleted(true);
        serviceRepository.save(skincareService);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeStatusService(long skincareId, Status status) {
        SkincareService skincareService = serviceRepository.findSkincareServiceByIdAndIsDeletedIsFalse(skincareId)
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_EXIST));
        if(skincareService.getStatus() == Status.ACTIVE) {
            skincareService.setStatus(Status.INACTIVE);
        }else{
            skincareService.setStatus(Status.ACTIVE);
        }
        serviceRepository.save(skincareService);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SkincareServiceResponse updateSkincareService(SkincareUpdateRequest request, Long skincareServiceId) {
        Optional<SkincareService> existingServiceByName = serviceRepository.findSkincareServiceByServiceName(request.getServiceName());
        SkincareService service = serviceRepository.findSkincareServiceByIdAndIsDeletedIsFalse(skincareServiceId)
                    .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_EXIST));

        if (existingServiceByName.isPresent()) {
            throw new AppException(ErrorCode.SERVICENAME_EXISTED);
        }
           serviceRepository.findSkincareServiceByServiceName(request.getServiceName());

            service.setServiceName(request.getServiceName());
            service.setDescription(request.getDescription());
            service.setPrice(request.getPrice());
            service.setStatus(request.getStatus());

            serviceRepository.save(service);

            return SkincareServiceResponse.builder()
                    .id(service.getId())
                    .serviceName(service.getServiceName())
                    .description(service.getDescription())
                    .price(service.getPrice())
                    .status(service.getStatus())
                    .build();
        }

    @Override
    public SkincareServiceResponse getSkincareServiceById(long skincareId) {
        SkincareService skincareService = serviceRepository.findSkincareServiceByIdAndIsDeletedIsFalse(skincareId).orElseThrow(
                () -> new AppException(ErrorCode.SERVICE_EXISTED)
        );
        return SkincareServiceResponse.builder()
                .id(skincareService.getId())
                .serviceName(skincareService.getServiceName())
                .description(skincareService.getDescription())
                .price(skincareService.getPrice())
                .status(skincareService.getStatus())
                .build();
    }

    @Override
    public List<SkincareService> getAllSkincareService() {
        List<SkincareService> list;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        if (user.getRole().equals(Role.ADMIN) || user.getRole().equals(Role.MANAGER)) {
            list = serviceRepository.findAll();
        } else {
            list = serviceRepository.findAll()
                    .stream()
                    .filter(s -> s.getStatus().equals(Status.ACTIVE) || !s.isDeleted())
                    .toList();
        }
        return list;
    }

}
