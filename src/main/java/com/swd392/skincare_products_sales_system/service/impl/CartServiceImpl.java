package com.swd392.skincare_products_sales_system.service.impl;

import com.swd392.skincare_products_sales_system.dto.response.CartItemResponse;
import com.swd392.skincare_products_sales_system.dto.response.CartResponse;
import com.swd392.skincare_products_sales_system.enums.ErrorCode;
import com.swd392.skincare_products_sales_system.exception.AppException;
import com.swd392.skincare_products_sales_system.model.Cart;
import com.swd392.skincare_products_sales_system.model.CartItem;
import com.swd392.skincare_products_sales_system.model.Product;
import com.swd392.skincare_products_sales_system.model.User;
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

    @Override
    @Transactional
    public void addProductToCart(String productId, Integer quantity) {
        String username = null;
        try {
            var context = SecurityContextHolder.getContext();
            username = context.getAuthentication().getName();
        } catch (Exception e) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        User user = userRepository.findByUsernameOrThrow(username);
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            Cart cart = getOrCreateCartEntity(user); // Lấy hoặc tạo mới giỏ hàng cho người dùng

            // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
            Optional<CartItem> existingItem = cart.getItems().stream()
                    .filter(item -> item.getProduct().getId().equals(productId))
                    .findFirst();
            if (existingItem.isPresent()) {
                existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
            } else {
                // Thêm sản phẩm mới vào giỏ hàng
                CartItem newItem = new CartItem();
                newItem.setProduct(product);
                newItem.setCart(cart);
                newItem.setPrice(product.getPrice());
                newItem.setQuantity(quantity);
                cart.getItems().add(newItem);
            }
            cartRepository.save(cart); // Lưu giỏ hàng vào cơ sở dữ liệu
        }
    }

    @Override
    public void removeProductFromCart(String productId, String username) {
        return;
    }

    @Override
    @Transactional
    public CartResponse getCart() {
        String username = null;
        try {
            var context = SecurityContextHolder.getContext();
            username = context.getAuthentication().getName();
        } catch (Exception e) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        User user = userRepository.findByUsernameOrThrow(username);

        Cart cart = getOrCreateCartEntity(user); // Trả về đối tượng giỏ hàng

        Double totalPrice = cart.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        // Chuyển đổi CartItem thành CartItemResponse
        List<CartItemResponse> itemResponses = cart.getItems().stream()
                .map(item -> {
                    CartItemResponse response = new CartItemResponse();
                    response.setProductName(item.getProduct().getName());
                    response.setPrice(item.getPrice());
                    response.setQuantity(item.getQuantity());
                    response.setTotalItemPrice(item.getPrice() * item.getQuantity());
                    return response;
                })
                .collect(Collectors.toList());

        // Tạo và trả về CartResponse
        CartResponse cartResponse = new CartResponse();
        cartResponse.setCartId(cart.getId());
        cartResponse.setItems(itemResponses);
        cartResponse.setTotalPrice(totalPrice);
        cartResponse.setUsername(username);
        return cartResponse;
    }


    // Giúp kiểm tra và tạo giỏ hàng mới nếu người dùng chưa có
    private Cart getOrCreateCartEntity(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart); // Lưu giỏ hàng mới vào DB
                });
    }
}
