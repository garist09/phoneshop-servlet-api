package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.ArrayListOrderDao;
import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.service.OrderService;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService {
    private static OrderService instance;
    OrderDao orderDao;

    private OrderServiceImpl() {
        orderDao = ArrayListOrderDao.getInstance();
    }

    public static OrderService getInstance() {
        if (Objects.isNull(instance)) {
            instance = new OrderServiceImpl();
        }
        return instance;
    }

    @Override
    public Order createOrderFromCart(Cart cart) {
        Order order = new Order();
        order.setCartItemList(cart.getCartItems().stream().map(cartItem -> (CartItem) cartItem.clone())
                        .collect(Collectors.toList()))
                .setSubtotal(cart.getTotalPrice())
                .setDeliveryCost(calculateDeliveryCost())
                .setCartTotal(order.getSubtotal().add(order.getDeliveryCost()));
        return order;
    }

    private static BigDecimal calculateDeliveryCost() {
        return new BigDecimal(30);
    }
}
