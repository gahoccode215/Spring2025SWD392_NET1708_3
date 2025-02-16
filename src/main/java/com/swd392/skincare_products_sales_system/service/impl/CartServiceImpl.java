package com.swd392.skincare_products_sales_system.service.impl;

import com.swd392.skincare_products_sales_system.constant.PredefinedRole;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
        if(!(user.getRole().getName().equals(PredefinedRole.CUSTOMER_ROLE))){
            throw new AppException(ErrorCode.FORBIDDEN);
        }

//        UserResponse userResponse = userMapper.toUserResponse(user);

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

    @Override
    public Cart getCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        if(!(user.getRole().getName().equals(PredefinedRole.CUSTOMER_ROLE))){
            throw new AppException(ErrorCode.FORBIDDEN);
        }
        return getOrCreateCart(user);
    }

    @Override
    @Transactional
    public void removeProductsFromCart(List<String> productIds) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        // Kiểm tra quyền của người dùng
        if(!(user.getRole().getName().equals(PredefinedRole.CUSTOMER_ROLE))){
            throw new AppException(ErrorCode.FORBIDDEN);
        }

        // Tìm giỏ hàng của người dùng
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
        productIds = productIds.stream().filter(Objects::nonNull).collect(Collectors.toList());
        // Lấy danh sách các sản phẩm cần xóa trong giỏ hàng
        List<CartItem> itemsToRemove = cartItemRepository.findByCartIdAndProductIdIn(cart.getId(), productIds);

        log.info("{}", itemsToRemove);
        // Nếu không có sản phẩm nào cần xóa
        if (itemsToRemove.isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_NOT_EXISTED_IN_CART);
        }
        cartItemRepository.flush();

        // Xóa các sản phẩm ra khỏi giỏ hàng
        cartItemRepository.deleteAll(itemsToRemove);

        // Cập nhật tổng giá trị giỏ hàng sau khi xóa các sản phẩm
        cart.updateTotalPrice();  // Hãy chắc chắn rằng phương thức `updateTotalPrice` không tham chiếu đến các đối tượng đã bị xóa
        cartRepository.save(cart);
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
