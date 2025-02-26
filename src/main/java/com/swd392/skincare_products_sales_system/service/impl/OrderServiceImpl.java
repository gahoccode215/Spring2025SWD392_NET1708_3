package com.swd392.skincare_products_sales_system.service.impl;

import com.swd392.skincare_products_sales_system.dto.response.OrderItemResponse;
import com.swd392.skincare_products_sales_system.dto.response.OrderResponse;
import com.swd392.skincare_products_sales_system.enums.*;
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
    OrderItemRepository orderItemRepository;

    @Override
    @Transactional
    public OrderResponse createOrder(Long cartId, Long addressId, PaymentMethod paymentMethod) {
        User user = getAuthenticatedUser();

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));

        if (paymentMethod == null) {
            throw new AppException(ErrorCode.INVALID_PAYMENT_METHOD);
        }

        Order order = buildOrder(cart, address, paymentMethod);
        order = orderRepository.save(order); // LƯU vào DB trước (để có ID)

        List<OrderItem> orderItems = createOrderItemsFromCart(cart, order);
        orderItemRepository.saveAll(orderItems); // Lưu danh sách OrderItems

        order.setOrderItems(orderItems);
        orderRepository.save(order);

        if (paymentMethod == PaymentMethod.COD) {
            clearCart(cart);
        }

        return mapToOrderResponse(order);
    }

    @Override
    @Transactional
    public void updateOrderStatus(Long orderId, boolean isPaid) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        order.setPaymentStatus(isPaid ? PaymentStatus.PAID : PaymentStatus.NOT_PAID);
        order.setStatus(OrderStatus.PENDING);
        orderRepository.save(order);

        if (isPaid) {
            Cart cart = cartRepository.findByUser(order.getAddress().getUser())
                    .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
            clearCart(cart);
        }
    }

    private Order buildOrder(Cart cart, Address address, PaymentMethod paymentMethod) {
        return Order.builder()
                .totalAmount(cart.getTotalPrice())
                .username(cart.getUser().getUsername())
                .orderDate(LocalDateTime.now())
                .paymentMethod(paymentMethod)
                .shippingFee(0.0)
                .address(address)
                .paymentStatus(PaymentStatus.NOT_PAID)
                .status(OrderStatus.PENDING)
                .build();
    }

    private List<OrderItem> createOrderItemsFromCart(Cart cart, Order order) {
        return cart.getItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setProduct(cartItem.getProduct());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(cartItem.getPrice());
                    orderItem.setTotalPrice(cartItem.getPrice() * cartItem.getQuantity());
                    return orderItem;
                })
                .collect(Collectors.toList());
    }

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

    private void clearCart(Cart cart) {
        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();
        cart.setTotalPrice(0.0);
        cartRepository.save(cart);
    }

}
