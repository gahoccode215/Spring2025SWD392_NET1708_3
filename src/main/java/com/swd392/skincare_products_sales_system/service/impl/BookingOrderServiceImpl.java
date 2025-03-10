package com.swd392.skincare_products_sales_system.service.impl;

import com.swd392.skincare_products_sales_system.dto.request.booking_order.*;
import com.swd392.skincare_products_sales_system.dto.response.ExpertResponse;
import com.swd392.skincare_products_sales_system.dto.response.FormResponse;
import com.swd392.skincare_products_sales_system.dto.response.PaymentOrderResponse;
import com.swd392.skincare_products_sales_system.enums.*;
import com.swd392.skincare_products_sales_system.exception.AppException;
import com.swd392.skincare_products_sales_system.model.*;
import com.swd392.skincare_products_sales_system.model.user.User;
import com.swd392.skincare_products_sales_system.repository.*;
import com.swd392.skincare_products_sales_system.service.BookingOrderService;
import com.swd392.skincare_products_sales_system.service.VNPayService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
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
    RoleRepository roleRepository;
    private final ImageSkinRepository imageSkinRepository;

    @Override
    public List<ExpertResponse> filterListExpert() {
        List<User> listUser = userRepository.findAll().stream()
                .filter(user -> user.getRole() != null && user.getRole().getId() == 5)
                .toList();
        List<ExpertResponse> list = new ArrayList<>();
        for (User user : listUser) {
            ExpertResponse expertResponse = ExpertResponse.builder()
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .phone(user.getPhone())
                    .avatar(user.getAvatar())
                    .gender(user.getGender() != null ? user.getGender().name() : null)
                    .role(user.getRole() != null ? user.getRole().getName() : null)
                    .build();
            list.add(expertResponse);
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BookingOrder asignBookingOrder(AsignExpertRequest request, Long bookingOrderId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        BookingOrder bookingOrder = bookingRepository.findByIdAndIsDeletedFalse(bookingOrderId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_EXIST));

        User expert = userRepository.findByIdAndIsDeletedFalse(request.getExpertId())
                .orElseThrow(() -> new AppException(ErrorCode.EXPERT_NOT_EXIST));

        bookingOrder.setExpertName(expert.getId());
        bookingRepository.save(bookingOrder);
        return bookingOrder;
    }

    @Override
    @Transactional
    public PaymentOrderResponse paymentBookingOrder(Long bookingOrderId,  String isAddress) throws UnsupportedEncodingException {
        User user = getAuthenticatedUser();

        BookingOrder bookingOrder = bookingRepository.findByIdAndIsDeletedFalse(bookingOrderId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_EXIST));
        VNPayService service = new VNPayService();
        Float price = bookingOrder.getPrice();
        Double amount = Double.valueOf(price);
        String url = service.createPaymentUrlBookingOrder(bookingOrderId,amount, isAddress);
        bookingRepository.save(bookingOrder);

        return PaymentOrderResponse.builder()
                .bookingOrderId(bookingOrderId)
                .dateTime(LocalDateTime.now())
                .price(price)
                .status(BookingStatus.PAYMENT)
                .build();
    }

    @Override
    public List<BookingOrder> listAllBookingOrder() {
        List<BookingOrder> list = bookingRepository.findAll()
                .stream()
                .toList();
        return list;
    }

    @Override
    public BookingOrder cancelBookingOrder(Long bookingOrderId, String note) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        BookingOrder bookingOrder = bookingRepository.findByIdAndIsDeletedFalse(bookingOrderId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_EXIST));

        bookingOrder.setStatus(BookingStatus.CANCELED);
        bookingOrder.setNote(note);
        return bookingRepository.save(bookingOrder);
    }

    @Override
    public void updateBookingOrderStatus(Long bookingOrderId, boolean isPaid) {
        BookingOrder bookingOrder = bookingRepository.findById(bookingOrderId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_EXIST));

        bookingOrder.setPaymentStatus(isPaid ? PaymentStatus.PAID : PaymentStatus.NOT_PAID);
        bookingOrder.setStatus(BookingStatus.PAYMENT);
        bookingRepository.save(bookingOrder);
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
    public List<BookingOrder> getBookingOrderByExpertId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        List<BookingOrder> list = bookingRepository.findAll()
                .stream()
                .filter(bookingOrder -> bookingOrder.getExpertName().equals(user.getId()))
                .toList();
        return List.of();
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
        String expertName = "";
        if(expert != null){
            if (expert.getStatus().equals(Status.INACTIVE.name())) {
                throw new AppException(ErrorCode.USER_INACTIVE);
            }
            expertName = expert.getId();
        }else {
            expertName = null;
        }
        if (service.getStatus().equals(Status.INACTIVE.name())) {
            throw new AppException(ErrorCode.SERVICE_INACTIVE);
        }

        List<ImageSkin> imageSkinList = new ArrayList<>();
        if (request.getImageSkins() != null && !request.getImageSkins().isEmpty()) {
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
        List<User> list = new ArrayList<>();

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

        bookingOrder.setIsDeleted(false);
        bookingRepository.save(bookingOrder);

        for (ImageSkin imageSkin : imageSkinList) {
            imageSkin.setBookingOrder(bookingOrder);
        }

        imageSkinRepository.saveAll(imageSkinList);

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
    public BookingOrder changeStatus(ChangeStatus status, Long bookingOrderId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        BookingOrder bookingOrder = bookingRepository.findByIdAndIsDeletedFalse(bookingOrderId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_EXIST));

        if(bookingOrder.getStatus() == BookingStatus.ASSIGNED_EXPERT){
            bookingOrder.setResponse(bookingOrder.getResponse());
            bookingOrder.setStatus(BookingStatus.CONTACT_CUSTOMER);
        }
        if(bookingOrder.getStatus() == BookingStatus.CONTACT_CUSTOMER){
            bookingOrder.setStatus(BookingStatus.CUSTOMER_CONFIRM);
        }
        // Kết thúc Tư vấn
        if(bookingOrder.getStatus() == BookingStatus.CONTACT_CUSTOMER){
            bookingOrder.setStatus(BookingStatus.FINISHED);
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


    private User getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsernameOrThrow(username);
    }




}
