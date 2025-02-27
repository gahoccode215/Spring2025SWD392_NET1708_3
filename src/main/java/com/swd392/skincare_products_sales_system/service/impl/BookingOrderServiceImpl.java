package com.swd392.skincare_products_sales_system.service.impl;

import com.swd392.skincare_products_sales_system.dto.request.booking_order.AsignExpertRequest;
import com.swd392.skincare_products_sales_system.dto.request.booking_order.ChangeStatus;
import com.swd392.skincare_products_sales_system.dto.request.booking_order.FormCreateRequest;
import com.swd392.skincare_products_sales_system.dto.request.booking_order.FormUpdateRequest;
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

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
        List<User> list;
        list = userRepository.findAll()
                .stream()
                .filter(u -> u.getRole().equals(Role.EXPERT))
                .collect(Collectors.toList());
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BookingOrder asignBookingOrder(AsignExpertRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        BookingOrder bookingOrder = bookingRepository.findByIdAndIsDeletedFalse(request.getBookingOrderId())
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_EXIST));

        User expert = userRepository.findByIdAndIsDeletedFalse(request.getExpertId())
                .orElseThrow(() -> new AppException(ErrorCode.EXPERT_NOT_EXIST));

        bookingOrder.setExpertName(expert.getFirstName()+" "+expert.getLastName());
        bookingRepository.save(bookingOrder);
        return bookingOrder;
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
        List<BookingOrder> list ;
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

        String expertName = (expert != null) ? expert.getFirstName() + "" + expert.getLastName() : null;

        BookingOrder bookingOrder = BookingOrder.builder()
                .note(request.getNote())
                .orderDate(LocalDateTime.now())
                .price(service.getPrice())
                .skinCondition(request.getSkinCondition())
                .status(BookingStatus.PENDING)
                .allergy(request.getAllergy())
                .image(request.getImage())
                .skinType(request.getSkinType())
                .expertName(expertName)
                .skincareService(service)
                .user(user)
                .date(LocalDateTime.now())
                .build();
        bookingOrder.setIsDeleted(false);
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
                .date(bookingOrder.getDate())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FormResponse updateBookingAdvise(FormUpdateRequest request, Long bookingOrderId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        //////////////////////////////////// ///////////////////////////////
        SkincareService service = serviceRepository.findById(request.getSkincareServiceId())
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_EXIST));

        BookingOrder booking = bookingRepository.findByIdAndIsDeletedFalse(bookingOrderId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_ORDER_NOT_EXIST));

        if (service.getStatus().equals(Status.INACTIVE)){
            throw new AppException(ErrorCode.SERVICE_INACTIVE);
        }

        BookingOrder bookingOrder = BookingOrder.builder()
                .note(request.getNote())
                .orderDate(booking.getOrderDate())
                .price(service.getPrice())
                .skinCondition(request.getSkinCondition())
                .status(BookingStatus.CONTACT_CUSTOMER)
                .allergy(request.getAllergy())
                .image(request.getImage())
                .skinType(request.getSkinType())
                .expertName(booking.getExpertName())
                .skincareService(service)
                .user(booking.getUser())
                .date(booking.getDate())
                .build();
        bookingRepository.save(bookingOrder);
        return FormResponse.builder()
                .note(bookingOrder.getNote())
                .image(bookingOrder.getImage())
                .skinType(bookingOrder.getSkinType())
                .expertName(bookingOrder.getExpertName())
                .allergy(bookingOrder.getAllergy())
                .bookDate(booking.getOrderDate())
                .price(bookingOrder.getPrice())
                .skincareService(service.getId())
                .userId(booking.getUser().getId())
                .status(BookingStatus.CONTACT_CUSTOMER)
                .date(booking.getDate())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
        // Contact với customer
        if(bookingOrder.getStatus() == BookingStatus.ASSIGNED_EXPERT){
            bookingOrder.setStatus(BookingStatus.CONTACT_CUSTOMER);
        }
        //Chia 2 luong neu tiep tuc mua liệu trình
        // Khách hàng comfirm mua lộ trình
        if(bookingOrder.getStatus() == BookingStatus.CONTACT_CUSTOMER){
            bookingOrder.setStatus(BookingStatus.CUSTOMER_CONFIRM);
        }
        // Kết thúc Tư vấn
        if(bookingOrder.getStatus() == BookingStatus.CONTACT_CUSTOMER){
            bookingOrder.setStatus(BookingStatus.FINISHED);
        }
        //Làm đơn cho thằng Customer mua lộ trình
        if(bookingOrder.getStatus() == BookingStatus.CUSTOMER_CONFIRM){
            bookingOrder.setStatus(BookingStatus.PENDING_CONFIRM);
        }
        if (bookingOrder.getStatus() == BookingStatus.PENDING_CONFIRM){
            bookingOrder.setStatus(BookingStatus.PAYMENT_ROUTINE);
        }
        if (bookingOrder.getStatus() == BookingStatus.PAYMENT_ROUTINE){
            bookingOrder.setStatus(BookingStatus.EXPERT_MAKE_ROUTINE);
        }
        // Export lên routine cho customer cho da
        if(bookingOrder.getStatus() == BookingStatus.EXPERT_MAKE_ROUTINE){
            bookingOrder.setStatus(BookingStatus.IN_PROGRESS_ROUTINE);
        }
        //Sau khi hoan thanh xong lo trinh thi end
        if(bookingOrder.getStatus() == BookingStatus.IN_PROGRESS_ROUTINE){
            bookingOrder.setStatus(BookingStatus.FINISHED);
        }
        bookingRepository.save(bookingOrder);
        return bookingOrder;
    }


}
