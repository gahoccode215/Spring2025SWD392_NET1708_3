
package com.swd392.skincare_products_sales_system.service.impl;

import com.swd392.skincare_products_sales_system.dto.request.booking_order.*;
import com.swd392.skincare_products_sales_system.dto.response.BookingOrderResponse;
import com.swd392.skincare_products_sales_system.dto.response.ExpertResponse;
import com.swd392.skincare_products_sales_system.dto.response.FormResponse;
import com.swd392.skincare_products_sales_system.dto.response.PaymentOrderResponse;
import com.swd392.skincare_products_sales_system.entity.product.Product;
import com.swd392.skincare_products_sales_system.entity.user.User;
import com.swd392.skincare_products_sales_system.enums.*;
import com.swd392.skincare_products_sales_system.exception.AppException;
import com.swd392.skincare_products_sales_system.entity.*;
import com.swd392.skincare_products_sales_system.entity.booking.BookingOrder;
import com.swd392.skincare_products_sales_system.entity.booking.ImageSkin;
import com.swd392.skincare_products_sales_system.entity.booking.ProcessBookingOrder;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingOrderServiceImpl implements BookingOrderService {

    UserRepository userRepository;
    BookingRepository bookingRepository;
    SkincareServiceRepository serviceRepository;
    ProcessBookingOrderRepository processBookingOrderRepository;
    ImageSkinRepository imageSkinRepository;
    ProductRepository productRepository;
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
        ProcessBookingOrder processBookingOrder = ProcessBookingOrder.builder()
                .user(user)
                .bookingOrder(bookingOrder)
                .status(BookingStatus.ASSIGNED_EXPERT)
                .build();
        processBookingOrder.setIsDeleted(false);
        processBookingOrderRepository.save(processBookingOrder);
        bookingRepository.save(bookingOrder);
        return bookingOrder;
    }

    @Override
    @Transactional
    public PaymentOrderResponse paymentBookingOrder(Long bookingOrderId,  String isAddress) throws UnsupportedEncodingException {
        User user = getAuthenticatedUser();

        BookingOrder bookingOrder = bookingRepository.findByIdAndIsDeletedFalse(bookingOrderId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_EXIST));
        if(!checkValidDatePayment(bookingOrderId)){
            throw new AppException(ErrorCode.BOOKING_TIME_OUT);
        }
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
        ProcessBookingOrder processBookingOrder = ProcessBookingOrder.builder()
                .bookingOrder(bookingOrder)
                .user(user)
                .time(LocalDateTime.now())
                .status(BookingStatus.CANCELED)
                .build();
        processBookingOrder.setIsDeleted(false);
        processBookingOrderRepository.save(processBookingOrder);
        return bookingRepository.save(bookingOrder);
    }

    @Override
    @Transactional
    public String updateBookingOrderStatus(PaymentBack paymentBack) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        BookingOrder bookingOrder = bookingRepository.findById(paymentBack.getBookingOrderId())
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_EXIST));
        bookingOrder.setPaymentStatus(paymentBack.getIsPaid() ? PaymentStatus.PAID : PaymentStatus.NOT_PAID);
        if(bookingOrder.getExpertName() != null){
            bookingOrder.setStatus(BookingStatus.ASSIGNED_EXPERT);
            ProcessBookingOrder processBookingOrder = ProcessBookingOrder.builder()
                    .user(user)
                    .bookingOrder(bookingOrder)
                    .status(BookingStatus.PAYMENT)
                    .time(LocalDateTime.now())
                    .build();
            processBookingOrder.setIsDeleted(false);
            processBookingOrderRepository.save(processBookingOrder);

            ProcessBookingOrder processBookingOrder1 = ProcessBookingOrder.builder()
                    .user(user)
                    .bookingOrder(bookingOrder)
                    .status(BookingStatus.ASSIGNED_EXPERT)
                    .time(LocalDateTime.now())
                    .build();
            processBookingOrder1.setIsDeleted(false);
            processBookingOrderRepository.save(processBookingOrder1);
            return "Đã cập nhật trạngt thái đơn thành công";
        }
        bookingOrder.setStatus(BookingStatus.PAYMENT);
        bookingRepository.save(bookingOrder);
        ProcessBookingOrder processBookingOrder = ProcessBookingOrder.builder()
                .user(user)
                .bookingOrder(bookingOrder)
                .status(BookingStatus.PAYMENT)
                .time(LocalDateTime.now())
                .build();
        processBookingOrder.setIsDeleted(false);
        processBookingOrderRepository.save(processBookingOrder);
        return "Đơn của bạn đã đươc cập nhập";
    }

    @Override
    public BookingOrderResponse getBookingOrderById(Long bookingOrderId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        BookingOrder bookingOrder = bookingRepository.findById(bookingOrderId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_EXIST));
        List<ImageSkin> imageSkins = imageSkinRepository.findAllByBookingOrderId(bookingOrderId);

        BookingOrderResponse bookingOrderResponse = new BookingOrderResponse();
        bookingOrderResponse.setBookingOrder(bookingOrder);
        bookingOrderResponse.setImageSkin(imageSkins);
        return bookingOrderResponse;
    }

    @Override
    public List<Product> getProducts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        List<Product> list = productRepository.findAll()
                .stream()
                .filter(e -> !e.getIsDeleted() && e.getStatus().equals(Status.ACTIVE))
                .toList();

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
        List<BookingOrder> list = bookingRepository.findByUserAndIsDeletedFalse(user);
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

        return bookingRepository.findAll()
                .stream()
                .filter(bookingOrder -> Objects.equals(bookingOrder.getExpertName(), user.getId()))
                .toList();
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

        if (!checkValidSpam()){
            throw new AppException(ErrorCode.BOOKING_VALID_SPAM);
        }
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
            if(!checkTimeOfExpert(expertName,request.getBookDate())){
                throw new AppException(ErrorCode.EXPERT_TIME_SLOT_UNAVAILABLE);
            }
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
                .paymentStatus(PaymentStatus.NOT_PAID)
                .date(LocalDateTime.now())
                .serviceName(service.getServiceName())
                .build();
        bookingOrder.setIsDeleted(false);
        bookingRepository.save(bookingOrder);

        for (ImageSkin imageSkin : imageSkinList) {
            imageSkin.setBookingOrder(bookingOrder);
        }

        imageSkinRepository.saveAll(imageSkinList);

        ProcessBookingOrder processBookingOrder = ProcessBookingOrder.builder()
                .bookingOrder(bookingOrder)
                .user(user)
                .status(BookingStatus.PENDING)
                .time(LocalDateTime.now())
                .build();
        processBookingOrder.setIsDeleted(false);
        processBookingOrderRepository.save(processBookingOrder);
        return FormResponse.builder()
                .bookingOrderId(bookingOrder.getId())
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
                .serviceName(service.getServiceName())
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

        ProcessBookingOrder processBookingOrder = ProcessBookingOrder.builder()
                .bookingOrder(booking)
                .user(user)
                .status(BookingStatus.EXPERT_UPDATE_ORDER)
                .time(LocalDateTime.now())
                .build();
        processBookingOrder.setIsDeleted(false);
        processBookingOrderRepository.save(processBookingOrder);

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
                .serviceName(service.getServiceName())
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
                .serviceName(service.getServiceName())
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
            bookingOrder.setResponse(status.getResponse());
        }
        ProcessBookingOrder processBookingOrder = ProcessBookingOrder.builder()
                .bookingOrder(bookingOrder)
                .user(user)
                .status(bookingOrder.getStatus())
                .time(LocalDateTime.now())
                .build();
        processBookingOrder.setIsDeleted(false);
        processBookingOrderRepository.save(processBookingOrder);
        bookingOrder.setStatus(status.getStatus());
        bookingRepository.save(bookingOrder);
        return bookingOrder;
    }


    private User getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsernameOrThrow(username);
    }

    private Boolean checkValidSpam() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        LocalDateTime check = LocalDateTime.now();
        LocalDate currentDate = check.toLocalDate();

        long bookingCount = bookingRepository.countByUserAndOrderDateBetween(user,
                currentDate.atStartOfDay(), currentDate.atTime(23, 59, 59));

        return bookingCount <= 2;
    }

    private Boolean checkValidDatePayment(long bookingOrderId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        LocalDate currentDate = LocalDate.now();
        BookingOrder bookingOrder = bookingRepository.findById(bookingOrderId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_EXIST));
        if (currentDate.isAfter(bookingOrder.getDate().toLocalDate())) {
            throw new AppException(ErrorCode.BOOKING_TIME_OUT);
        }
        return true;
    }

    private Boolean checkTimeOfExpert(String expertId, LocalDateTime bookDate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        User expert = userRepository.findByIdAndIsDeletedFalse(expertId)
                .orElseThrow(() -> new AppException(ErrorCode.EXPERT_NOT_EXIST));

        LocalDateTime endTime = bookDate.plusHours(1);

        List<BookingOrder> list = bookingRepository.findAll()
                .stream()
                .filter(bookingOrder -> bookingOrder.getExpertName().equals(user.getId()))
                .toList();

        for (BookingOrder existingBooking : list) {
            LocalDateTime existingStartTime = existingBooking.getOrderDate();
            LocalDateTime existingEndTime = existingStartTime.plusHours(1);

            if (bookDate.isBefore(existingEndTime) && endTime.isAfter(existingStartTime)) {
                throw new AppException(ErrorCode.EXPERT_TIME_SLOT_UNAVAILABLE);
            }
        }

        return true;
    }




}
