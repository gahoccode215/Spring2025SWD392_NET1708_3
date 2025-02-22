package com.swd392.skincare_products_sales_system.service.impl;

import com.swd392.skincare_products_sales_system.dto.response.OrderItemResponse;
import com.swd392.skincare_products_sales_system.dto.response.OrderResponse;
import com.swd392.skincare_products_sales_system.enums.ErrorCode;
import com.swd392.skincare_products_sales_system.enums.PaymentMethod;
import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.exception.AppException;
import com.swd392.skincare_products_sales_system.model.*;
import com.swd392.skincare_products_sales_system.repository.*;
import com.swd392.skincare_products_sales_system.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {
    AddressRepository addressRepository;
    OrderRepository orderRepository;
    CartRepository cartRepository;
    UserRepository userRepository;
    CartItemRepository cartItemRepository;


    @Override
    @Transactional
    public OrderResponse createOrder(Long cartId, Long addressId, PaymentMethod paymentMethod) {
        User user = getAuthenticatedUser();  // Lấy người dùng đã đăng nhập

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));

        // Kiểm tra phương thức thanh toán hợp lệ
        if (paymentMethod == null) {
            throw new AppException(ErrorCode.INVALID_PAYMENT_METHOD);
        }

        // Tạo đơn hàng và lưu vào database
        Order order = buildOrder(cart, address, paymentMethod);
        Order savedOrder = saveOrderAndItems(order, cart);
        if (paymentMethod == PaymentMethod.COD) {
            clearCart(cart);
        }
        // Tạo OrderResponse từ đơn hàng đã lưu
        return mapToOrderResponse(savedOrder);
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
    }

    @Override
    @Transactional
    public void confirmOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        order.setStatus(Status.PAID);
        orderRepository.save(order);

        // Xóa giỏ hàng sau khi thanh toán thành công
        Cart cart = cartRepository.findByUser(order.getAddress().getUser())
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        clearCart(cart);
    }

    // Tạo đối tượng Order từ Cart và Address
    private Order buildOrder(Cart cart, Address address, PaymentMethod paymentMethod) {
        Order order = Order.builder()
                .totalAmount(cart.getTotalPrice())
                .username(cart.getUser().getUsername())
                .orderDate(LocalDateTime.now())
                .paymentMethod(paymentMethod)
                .shippingFee(0.0)
                .address(address)
                .status(Status.PENDING)
                .build();

        order.setOrderItems(createOrderItemsFromCart(cart, order));
        return order;
    }

    // Chuyển đổi CartItem thành OrderItem
    private List<OrderItem> createOrderItemsFromCart(Cart cart, Order order) {
        return cart.getItems().stream()
                .map(cartItem -> OrderItem.builder()
                        .product(cartItem.getProduct())
                        .quantity(cartItem.getQuantity())
                        .price(cartItem.getPrice())
                        .totalPrice(cartItem.getPrice() * cartItem.getQuantity())
                        .order(order)  // ✅ Gán Order trước khi lưu
                        .build())
                .collect(Collectors.toList());
    }

    // Chuyển đổi Order thành OrderResponse
    private OrderResponse mapToOrderResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getOrderItems().stream()
                .map(item -> OrderItemResponse.builder()
                        .productName(item.getProduct().getName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .totalPrice(item.calculateTotalPrice())
                        .build())
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .orderId(order.getId())
                .totalAmount(order.getTotalAmount())
                .orderInfo("Order placed successfully for " + order.getUsername())
                .username(order.getUsername())
                .orderDate(order.getOrderDate())
                .paymentMethod(order.getPaymentMethod())
                .orderResponseItemList(itemResponses)
                .status(order.getStatus())
                .build();
    }

    private User getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsernameOrThrow(username);
    }

    // Lưu OrderItems sau khi Order đã được lưu
    private Order saveOrderAndItems(Order order, Cart cart) {
        Order savedOrder = orderRepository.save(order);
        List<OrderItem> orderItems = order.getOrderItems();
        savedOrder.setOrderItems(orderItems);
        // Lưu lại Order cùng với OrderItems
        return orderRepository.save(savedOrder);
    }

    private void clearCart(Cart cart) {
        cartItemRepository.deleteAll(cart.getItems()); // Xóa tất cả CartItem
        cart.getItems().clear();
        cart.setTotalPrice(0.0);
        cartRepository.save(cart);
    }

}
