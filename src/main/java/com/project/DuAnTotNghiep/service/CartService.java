package com.project.DuAnTotNghiep.service;

import com.project.DuAnTotNghiep.dto.CartDto;
import com.project.DuAnTotNghiep.dto.OrderDto;
import com.project.DuAnTotNghiep.entity.Cart;
import com.project.DuAnTotNghiep.exception.NotFoundException;
import org.aspectj.weaver.ast.Or;
import org.hibernate.criterion.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartService {
//    Page<Cart> carts(Pageable pageable);
    List<CartDto> getAllCart();
    List<CartDto> getAllCartByAccountId();
    void addToCart(CartDto cartDto) throws NotFoundException;

    void updateCart(CartDto cartDto) throws NotFoundException;

    void orderUser(OrderDto orderDto);
    OrderDto orderAdmin(OrderDto orderDto);

    void deleteCart(Long id);
}
