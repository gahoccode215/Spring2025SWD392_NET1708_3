package com.swd392.skincare_products_sales_system.service.impl;

import com.swd392.skincare_products_sales_system.dto.response.UserResponse;
import com.swd392.skincare_products_sales_system.enums.ErrorCode;
import com.swd392.skincare_products_sales_system.exception.AppException;
import com.swd392.skincare_products_sales_system.mapper.UserMapper;
import com.swd392.skincare_products_sales_system.model.Cart;
import com.swd392.skincare_products_sales_system.model.CartItem;
import com.swd392.skincare_products_sales_system.model.Product;
import com.swd392.skincare_products_sales_system.model.User;
import com.swd392.skincare_products_sales_system.repository.CartItemRepository;
import com.swd392.skincare_products_sales_system.repository.CartRepository;
import com.swd392.skincare_products_sales_system.repository.ProductRepository;
import com.swd392.skincare_products_sales_system.repository.UserRepository;
import com.swd392.skincare_products_sales_system.service.CartService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartServiceImpl implements CartService {
    CartRepository cartRepository;
    CartItemRepository cartItemRepository;
    ProductRepository productRepository;
    UserRepository userRepository;
    UserMapper userMapper;

    @Override
    @Transactional
    public Cart addProductToCart(String productId, Integer quantity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        UserResponse userResponse = userMapper.toUserResponse(user);

        Cart cart = getOrCreateCart(user);

        // Lấy sản phẩm từ ID
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        // Kiểm tra nếu sản phẩm đã có trong giỏ hàng
        Optional<CartItem> existingItem = cartItemRepository.findByCartAndProduct(cart, product);
        if (existingItem.isPresent()) {
            // Nếu có, cập nhật số lượng
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            // Nếu chưa có, thêm sản phẩm mới vào giỏ
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setPrice(product.getPrice());  // Gán giá sản phẩm tại thời điểm thêm vào giỏ
            cartItemRepository.save(newItem);
        }

        // Cập nhật tổng giá trị giỏ hàng
        cart.updateTotalPrice();
        cartRepository.save(cart);

        return cart;
    }
    private Cart getOrCreateCart(User user) {
        // Tìm giỏ hàng của người dùng
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }
}
