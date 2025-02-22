package com.swd392.skincare_products_sales_system.service.impl;

import com.swd392.skincare_products_sales_system.dto.request.ChangeStatus;
import com.swd392.skincare_products_sales_system.dto.request.FormCreateRequest;
import com.swd392.skincare_products_sales_system.dto.response.FormResponse;
import com.swd392.skincare_products_sales_system.enums.BookingStatus;
import com.swd392.skincare_products_sales_system.enums.ErrorCode;
import com.swd392.skincare_products_sales_system.enums.Role;
import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.exception.AppException;
import com.swd392.skincare_products_sales_system.model.BookingOrder;
import com.swd392.skincare_products_sales_system.model.SkincareService;
import com.swd392.skincare_products_sales_system.model.User;
import com.swd392.skincare_products_sales_system.repository.BookingRepository;
import com.swd392.skincare_products_sales_system.repository.SkincareServiceRepository;
import com.swd392.skincare_products_sales_system.repository.UserRepository;
import com.swd392.skincare_products_sales_system.service.BookingOrderService;
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
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingOrderServiceImpl implements BookingOrderService {

    UserRepository userRepository;
    BookingRepository bookingRepository;
    SkincareServiceRepository serviceRepository;

    @Override
    public List<User> filterListExpert() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        List<User> list = new ArrayList<>();
        list = userRepository.findAll()
                .stream()
                .filter(u -> u.getRole().equals(Role.EXPERT))
                .collect(Collectors.toList());
        return list;
    }

    @Override
    public List<BookingOrder> getBookingOrder() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        List<BookingOrder> list = new ArrayList<>();
        if (user.getRole().equals(Role.CUSTOMER)) {
            list = bookingRepository.findAll()
                    .stream()
                    .filter(bookingOrder -> bookingOrder.getUser().equals(user))
                    .collect(Collectors.toList());
        } else {
            list = bookingRepository.findAll();
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FormResponse bookingAdvise(FormCreateRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        User expert = userRepository.findByIdAndIsDeletedFalse(request.getExpertId())
                .orElse(null);

        SkincareService service = serviceRepository.findById(request.getSkincareServiceId())
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_EXIST));

        if (expert.getStatus().equals(Status.INACTIVE)){
            throw new AppException(ErrorCode.USER_INACTIVE);
        }
        if (service.getStatus().equals(Status.INACTIVE)){
            throw new AppException(ErrorCode.SERVICE_INACTIVE);
        }

        String expertName = (expert != null) ? expert.getUsername() : null;

        BookingOrder bookingOrder = BookingOrder.builder()
                .note(request.getNote())
                .orderDate(LocalDate.now())
                .price(service.getPrice())
                .skinCondition(request.getSkinCondition())
                .status(BookingStatus.PENDING)
                .allergy(request.getAllergy())
                .image(request.getImage())
                .skinType(request.getSkinType())
                .expertName(expertName)
                .skincareService(service)
                .user(user)
                .build();
        bookingRepository.save(bookingOrder);
        return FormResponse.builder()
                .note(bookingOrder.getNote())
                .image(bookingOrder.getImage())
                .skinType(bookingOrder.getSkinType())
                .expertName(bookingOrder.getExpertName())
                .allergy(bookingOrder.getAllergy())
                .bookDate(bookingOrder.getOrderDate())
                .price(bookingOrder.getPrice())
                .skincareService(service.getId())
                .userId(user.getId())
                .status(BookingStatus.PENDING)
                .build();
    }

    @Override
    public BookingOrder changeStatus(ChangeStatus status) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        BookingOrder bookingOrder = bookingRepository.findByIdAndIsDeletedFalse(status.getBookingOrderId())
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_EXIST));

        // Đơn sẽ vào hệ thống chờ Staff giao đơn
        if (bookingOrder.getStatus() == BookingStatus.PAYMENT_SUCCESS){
            bookingOrder.setStatus(BookingStatus.ASSIGNED_EXPERT);
        }
        // Đơn này đc customer choose Expert từ form
        if(bookingOrder.getStatus() == BookingStatus.ASSIGNED_EXPERT){
            bookingOrder.setStatus(BookingStatus.CONTACT_CUSTOMER);
        }
        // Contact với customer
        if(bookingOrder.getStatus() == BookingStatus.CONTACT_CUSTOMER){
            bookingOrder.setStatus(BookingStatus.EXPERT_MAKE_ROUTINE);
        }
        // Export lên routine cho customer cho da
        if(bookingOrder.getStatus() == BookingStatus.EXPERT_MAKE_ROUTINE){
            bookingOrder.setStatus(BookingStatus.EXPERT_MAKE_ROUTINE);
        }

        return bookingOrder;
    }


}
