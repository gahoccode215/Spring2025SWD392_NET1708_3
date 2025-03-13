package com.swd392.skincare_products_sales_system.service.impl;

import com.swd392.skincare_products_sales_system.dto.response.cart.CartItemResponse;
import com.swd392.skincare_products_sales_system.dto.response.cart.CartResponse;
import com.swd392.skincare_products_sales_system.enums.ErrorCode;
import com.swd392.skincare_products_sales_system.exception.AppException;
import com.swd392.skincare_products_sales_system.entity.cart.Cart;
import com.swd392.skincare_products_sales_system.entity.cart.CartItem;
import com.swd392.skincare_products_sales_system.entity.product.Product;
import com.swd392.skincare_products_sales_system.entity.User;
import com.swd392.skincare_products_sales_system.repository.CartItemRepository;
import com.swd392.skincare_products_sales_system.repository.CartRepository;
import com.swd392.skincare_products_sales_system.repository.ProductRepository;
import com.swd392.skincare_products_sales_system.repository.UserRepository;
import com.swd392.skincare_products_sales_system.service.CartService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartServiceImpl implements CartService {

    CartRepository cartRepository;
    ProductRepository productRepository;
    UserRepository userRepository;
    CartItemRepository cartItemRepository;


    @Override
    @Transactional
    public void addProductToCart(String productId, Integer quantity) {
        User user = getAuthenticatedUser();
        Product product = getProductById(productId);
        Cart cart = getOrCreateCartEntity(user);
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();
        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
        } else {
            addNewItemToCart(cart, product, quantity);
        }
        cart.updateTotalPrice();
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void removeProductFromCart(List<String> productIds) {
        User user = getAuthenticatedUser();
        Cart cart = getOrCreateCartEntity(user);
        List<Product> products = productRepository.findAllById(productIds);
        if (products.size() != productIds.size()) {
            throw new AppException(ErrorCode.PRODUCT_NOT_EXISTED);
        }
        List<CartItem> itemsToRemove = cart.getItems().stream()
                .filter(cartItem -> productIds.contains(cartItem.getProduct().getId()))
                .toList();
        cart.getItems().removeAll(itemsToRemove);
        cartItemRepository.deleteAll(itemsToRemove);
        cart.updateTotalPrice();
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public CartResponse getCart() {
        User user = getAuthenticatedUser();
        Cart cart = getOrCreateCartEntity(user);
        Double totalPrice = calculateTotalPrice(cart);
        List<CartItemResponse> itemResponses = convertCartItemsToResponses(cart);
        return createCartResponse(cart, itemResponses, totalPrice);
    }

    @Override
    @Transactional
    public void updateProductQuantityInCart(String productId, Integer quantity) {
        User user = getAuthenticatedUser();
        Cart cart = getOrCreateCartEntity(user);
        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        if (quantity > 0) {
            cartItem.setQuantity(quantity);
        } else {
            throw new AppException(ErrorCode.INVALID_QUANTITY);
        }
        cart.updateTotalPrice();
        cartRepository.save(cart);
    }

    private User getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsernameOrThrow(username);
    }

    private Product getProductById(String productId) {
        return productRepository.findByIdAndIsDeletedFalseAndStatus(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
    }

    private Cart getOrCreateCartEntity(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> createNewCart(user));
    }

    private Cart createNewCart(User user) {
        Cart newCart = Cart.builder().user(user).items(new HashSet<>()).build(); // Khởi tạo items là HashSet
        return cartRepository.save(newCart);
    }

    private void addNewItemToCart(Cart cart, Product product, Integer quantity) {
        CartItem newItem = CartItem.builder()
                .product(product)
                .cart(cart)
                .price(product.getPrice())
                .quantity(quantity)
                .build();
        cart.getItems().add(newItem);
    }

    private Double calculateTotalPrice(Cart cart) {
        return cart.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    private List<CartItemResponse> convertCartItemsToResponses(Cart cart) {
        return cart.getItems().stream()
                .map(item -> CartItemResponse.builder()
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .price(item.getPrice())
                        .quantity(item.getQuantity())
                        .thumbnail(item.getProduct().getThumbnail())
                        .totalItemPrice(item.getPrice() * item.getQuantity())
                        .build())
                .collect(Collectors.toList());
    }

    private CartResponse createCartResponse(Cart cart, List<CartItemResponse> itemResponses, Double totalPrice) {
        return CartResponse.builder()
                .cartId(cart.getId())
                .username(cart.getUser().getUsername())
                .items(itemResponses)
                .totalPrice(totalPrice)
                .build();
    }

}
