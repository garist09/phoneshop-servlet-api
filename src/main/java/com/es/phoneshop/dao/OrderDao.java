package com.es.phoneshop.dao;

import com.es.phoneshop.model.order.Order;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface OrderDao {
    List<Order> getOrderList(HttpServletRequest request);
    Order getOrder(HttpServletRequest request, String orderId);
    void addOrder(HttpServletRequest request, Order order);
    void deleteOrder(HttpServletRequest request, String orderId);
}
