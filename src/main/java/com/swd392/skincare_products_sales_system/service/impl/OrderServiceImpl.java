package com.swd392.skincare_products_sales_system.service.impl;

import com.swd392.skincare_products_sales_system.constant.PredefinedRole;
import com.swd392.skincare_products_sales_system.dto.request.DeliveryRequest;
import com.swd392.skincare_products_sales_system.dto.response.UpdatedResponse;
import com.swd392.skincare_products_sales_system.dto.response.order.OrderItemResponse;
import com.swd392.skincare_products_sales_system.dto.response.order.OrderPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.order.OrderResponse;
import com.swd392.skincare_products_sales_system.enums.*;
import com.swd392.skincare_products_sales_system.exception.AppException;
import com.swd392.skincare_products_sales_system.model.*;
import com.swd392.skincare_products_sales_system.model.cart.Cart;
import com.swd392.skincare_products_sales_system.model.order.Order;
import com.swd392.skincare_products_sales_system.model.order.OrderItem;
import com.swd392.skincare_products_sales_system.model.product.Batch;
import com.swd392.skincare_products_sales_system.model.product.Product;
import com.swd392.skincare_products_sales_system.repository.*;
import com.swd392.skincare_products_sales_system.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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
    BatchRepository batchRepository;
    ProductRepository productRepository;

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
        order = orderRepository.save(order);
        List<OrderItem> orderItems = createOrderItemsFromCart(cart, order);
        orderItems.forEach(orderItem -> {
            Product product = productRepository.findByIdAndIsDeletedFalse(orderItem.getProduct().getId()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        });
        orderItemRepository.saveAll(orderItems);
        order.setOrderItems(orderItems);
        orderRepository.save(order);
        clearCart(cart);
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

    @Override
    public OrderPageResponse getOrdersByCustomer(int page, int size) {
        User user = getAuthenticatedUser();
        if (page > 0) page -= 1;
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = orderRepository.findAllByFilters(user.getUsername(), pageable);
        List<OrderResponse> orderResponses = orders.getContent().stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
        OrderPageResponse response = new OrderPageResponse();
        response.setOrderResponseList(orderResponses);
        response.setTotalElements(orders.getTotalElements());
        response.setTotalPages(orders.getTotalPages());
        response.setPageNumber(orders.getNumber());
        response.setPageSize(orders.getSize());

        return response;
    }

    @Override
    public OrderPageResponse getOrdersByAdmin(int page, int size) {
        User user = getAuthenticatedUser();

        if (page > 0) page -= 1;
        Pageable pageable = PageRequest.of(page, size);
        if (user.getRole().getName().equals(PredefinedRole.STAFF)) {
            Page<Order> orders = orderRepository.findAll(pageable);
            List<OrderResponse> orderResponses = orders.getContent().stream()
                    .map(this::mapToOrderResponse)  // FIX lỗi orderResponseItemList = null
                    .collect(Collectors.toList());
            OrderPageResponse response = new OrderPageResponse();
            response.setOrderResponseList(orderResponses);
            response.setTotalElements(orders.getTotalElements());
            response.setTotalPages(orders.getTotalPages());
            response.setPageNumber(orders.getNumber());
            response.setPageSize(orders.getSize());
            return response;
        }
        if (user.getRole().getName().equals(PredefinedRole.DELIVERY)) {
            Page<Order> orders = orderRepository.findAllByFiltersDelivery(pageable);
            List<OrderResponse> orderResponses = orders.getContent().stream()
                    .map(this::mapToOrderResponse)  // FIX lỗi orderResponseItemList = null
                    .collect(Collectors.toList());
            OrderPageResponse response = new OrderPageResponse();
            response.setOrderResponseList(orderResponses);
            response.setTotalElements(orders.getTotalElements());
            response.setTotalPages(orders.getTotalPages());
            response.setPageNumber(orders.getNumber());
            response.setPageSize(orders.getSize());
            return response;
        }
        Page<Order> orders = orderRepository.findAll(pageable);
        List<OrderResponse> orderResponses = orders.getContent().stream()
                .map(this::mapToOrderResponse)  // FIX lỗi orderResponseItemList = null
                .collect(Collectors.toList());
        OrderPageResponse response = new OrderPageResponse();
        response.setOrderResponseList(orderResponses);
        response.setTotalElements(orders.getTotalElements());
        response.setTotalPages(orders.getTotalPages());
        response.setPageNumber(orders.getNumber());
        response.setPageSize(orders.getSize());
        return response;
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        return mapToOrderResponse(order);
    }

    @Override
    @Transactional
    public void confirmOrder(Long id, OrderStatus orderStatus) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        if(orderStatus == OrderStatus.PROCESSING){
            order.getOrderItems().forEach(orderItem -> {
                List<Batch> batchList = batchRepository.findAllByProductId(orderItem.getProduct().getId());
                int quantityDu = 0;
                for(Batch batch : batchList){
                    if(orderItem.getQuantity() > batch.getQuantity()){
                        quantityDu = orderItem.getQuantity() - batch.getQuantity();
                        batch.setQuantity(batch.getQuantity() - orderItem.getQuantity() - quantityDu);
                        batch.setOrderItem(orderItem);
                    }else{
                        batch.setQuantity(batch.getQuantity() - orderItem.getQuantity());
                        batch.setOrderItem(orderItem);
                    }
                    batchRepository.save(batch);
                    if(quantityDu == 0)
                        break;
                }
            });
            User user = getAuthenticatedUser();
            order.setStatus(orderStatus);
            order.setUpdatedAt(LocalDateTime.now());
            order.setUpdatedBy(user.getUsername());
        }
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void deliveringOrder(Long id, OrderStatus orderStatus) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        User user = getAuthenticatedUser();
        order.setStatus(orderStatus);
        order.setUpdatedAt(LocalDateTime.now());
        order.setUpdatedBy(user.getUsername());
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void deliveryOrder(Long id, OrderStatus orderStatus, DeliveryRequest request) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        User user = getAuthenticatedUser();
        if (orderStatus.equals(OrderStatus.DELIVERING_FAIL)) {
            order.getOrderItems().forEach(orderItem -> {
                List<Batch> batchList = batchRepository.findAllByProductId(orderItem.getProduct().getId());
                for(Batch batch : batchList){
                    if(batch.getOrderItem() != null){
                        if (batch.getOrderItem().getId().equals(orderItem.getId())){
                            batch.setQuantity(batch.getQuantity() + orderItem.getQuantity());
                        }
                    }
                }
            });
        }
        user.setPoint((int) Math.round(order.getTotalAmount() / 1000));
        order.setStatus(orderStatus);
        order.setUpdatedAt(LocalDateTime.now());
        order.setUpdatedBy(user.getUsername());
        if(request.getImage() != null){
            order.setImageOrderSuccess(request.getImage());
        }
        orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
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
                        .thumbnailProduct(item.getProduct().getThumbnail())
                        .build())
                .collect(Collectors.toList());
        UpdatedResponse updatedResponse = new UpdatedResponse();
        if (order.getUpdatedBy() != null) {
            User user = userRepository.findByUsernameOrThrow(order.getUpdatedBy());
            updatedResponse.setUpdatedAt(order.getUpdatedAt());
            updatedResponse.setUpdatedBy(user);
        }

        return OrderResponse.builder()
                .orderId(order.getId())
                .totalAmount(order.getTotalAmount())
                .orderInfo("Order placed successfully for " + order.getUsername())
                .username(order.getUsername())
                .orderDate(order.getOrderDate())
                .paymentMethod(order.getPaymentMethod())
                .orderResponseItemList(itemResponses)
                .address(order.getAddress())
                .paymentStatus(order.getPaymentStatus())
                .status(order.getStatus())
                .updatedResponse(updatedResponse)
                .imageOrderSuccess(order.getImageOrderSuccess())
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
