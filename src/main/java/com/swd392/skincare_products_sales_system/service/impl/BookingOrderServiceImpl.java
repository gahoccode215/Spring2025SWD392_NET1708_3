package com.swd392.skincare_products_sales_system.service.impl;

import com.swd392.skincare_products_sales_system.dto.request.booking_order.*;
import com.swd392.skincare_products_sales_system.dto.request.quiz.QuestionRequest;
import com.swd392.skincare_products_sales_system.dto.response.FormResponse;
import com.swd392.skincare_products_sales_system.dto.response.ImageSkinResponse;
import com.swd392.skincare_products_sales_system.enums.BookingStatus;
import com.swd392.skincare_products_sales_system.enums.ErrorCode;
import com.swd392.skincare_products_sales_system.enums.RoleEnum;
import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.exception.AppException;
import com.swd392.skincare_products_sales_system.model.*;
import com.swd392.skincare_products_sales_system.repository.BookingRepository;
import com.swd392.skincare_products_sales_system.repository.ImageSkinRepository;
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

import java.time.LocalDateTime;
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
    private final ImageSkinRepository imageSkinRepository;

    @Override
    public List<User> filterListExpert() {
        List<User> list;
        list = userRepository.findAll()
                .stream()
                .filter(u -> u.getRole().equals(RoleEnum.EXPERT))
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
        if (user.getRole().equals(RoleEnum.CUSTOMER)) {
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
        // Authenticate user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String username = authentication.getName();

        // Fetch user information
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        // Fetch expert information
        User expert = userRepository.findByIdAndIsDeletedFalse(request.getExpertId())
                .orElseThrow(() -> new AppException(ErrorCode.EXPERT_NOT_EXIST));

        // Fetch skincare service information
        SkincareService service = serviceRepository.findById(request.getSkincareServiceId())
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_EXIST));

        // Check expert and service status
        if (expert.getStatus().equals(Status.INACTIVE.name())) {
            throw new AppException(ErrorCode.USER_INACTIVE);
        }
        if (service.getStatus().equals(Status.INACTIVE.name())) {
            throw new AppException(ErrorCode.SERVICE_INACTIVE);
        }

        // Prepare ImageSkin list
        List<ImageSkin> imageSkinList = new ArrayList<>();
        if (request.getImageSkins() != null && !request.getImageSkins().isEmpty()) {
            // Map each ImageSkinRequest to ImageSkin entity
            imageSkinList = request.getImageSkins().stream()
                    .map(imageSkinRequest -> {
                        ImageSkin imageSkin = new ImageSkin();
                        imageSkin.setImage(imageSkinRequest.getImage());
                        imageSkin.setUser(user);
                        imageSkin.setIsDeleted(false);
                        return imageSkin;
                    })
                    .collect(Collectors.toList());
        }

        // Create expert name
        String expertName = expert.getFirstName() + " " + expert.getLastName();

        // Create BookingOrder
        BookingOrder bookingOrder = BookingOrder.builder()
                .note(request.getNote())
                .orderDate(LocalDateTime.now())
                .price(service.getPrice())
                .skinCondition(request.getSkinCondition())
                .status(BookingStatus.PENDING)
                .allergy(request.getAllergy())
                .skinType(request.getSkinType())
                .expertName(expertName)
                .skincareService(service)
                .lastName(request.getLastName())
                .age(request.getAge())
                .firstName(request.getFirstName())
                .user(user)
                .date(LocalDateTime.now())
                .build();

        // Save the BookingOrder to generate its ID
        bookingOrder.setIsDeleted(false);  // Ensure it's not deleted
        bookingRepository.save(bookingOrder);  // Save first to generate ID

        // Now set the bookingOrder for each ImageSkin and save them
        for (ImageSkin imageSkin : imageSkinList) {
            imageSkin.setBookingOrder(bookingOrder);  // Associate ImageSkin with BookingOrder
        }

        // Save ImageSkins into the database
        imageSkinRepository.saveAll(imageSkinList);

        // Build and return the response
        return FormResponse.builder()
                .note(bookingOrder.getNote())
                .skinType(bookingOrder.getSkinType())
                .expertName(bookingOrder.getExpertName())
                .allergy(bookingOrder.getAllergy())
                .bookDate(bookingOrder.getOrderDate())
                .price(bookingOrder.getPrice())
                .skincareService(service.getId())
                .userId(user.getId())
                .skinCondition(request.getSkinCondition())
                .imageSkins(imageSkinList)
                .lastName(request.getLastName())
                .age(request.getAge())
                .firstName(request.getFirstName())
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
                .skinType(request.getSkinType())
                .expertName(booking.getExpertName())
                .skincareService(service)
                .user(booking.getUser())
                .date(booking.getDate())
                .build();
        bookingRepository.save(bookingOrder);
        return FormResponse.builder()
                .note(bookingOrder.getNote())
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
