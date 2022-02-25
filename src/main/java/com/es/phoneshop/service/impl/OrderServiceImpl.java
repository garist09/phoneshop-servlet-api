package com.es.phoneshop.service.impl;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.service.OrderService;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.ResourceBundle;

public class OrderServiceImpl implements OrderService {
    private static final String CONSTANT_PROPERTY_FILE_NAME = "constant";
    private static final String DELIVERY_COST_VALUE = "delivery.cost";

    private static OrderService instance;

    private ResourceBundle resourceBundle;

    private OrderServiceImpl() {
        resourceBundle = ResourceBundle.getBundle(CONSTANT_PROPERTY_FILE_NAME);
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
        Cart newCart = cart.clone();
        order.setCartItemList(newCart.getCartItems());
        order.setSubtotal(newCart.getTotalPrice());
        order.setDeliveryCost(calculateDeliveryCost());
        order.setCartTotal(order.getSubtotal().add(order.getDeliveryCost()));
        return order;
    }

    private BigDecimal calculateDeliveryCost() {
        return new BigDecimal(resourceBundle.getString(DELIVERY_COST_VALUE));
    }
}
