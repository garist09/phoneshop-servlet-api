package com.es.phoneshop.dao;

import com.es.phoneshop.exception.IdNotFoundException;
import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ArrayListOrderDao implements OrderDao {
    private static final Object lock;
    public static final String ORDER_LIST_ATTRIBUTE = "orderList";

    private static List<Order> orderList;
    private static OrderDao instance;

    static {
        lock = new Object();
    }

    private ArrayListOrderDao() {
    }

    public static OrderDao getInstance() {
        if (Objects.isNull(instance)) {
            synchronized (lock) {
                instance = new ArrayListOrderDao();
            }
        }
        return instance;
    }

    @Override
    public List<Order> getOrderList(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (Objects.isNull(session.getAttribute(ORDER_LIST_ATTRIBUTE))) {
            synchronized (session) {
                orderList = new ArrayList<>();
            }
            session.setAttribute(ORDER_LIST_ATTRIBUTE, orderList);
        } else {
            orderList = (List) session.getAttribute(ORDER_LIST_ATTRIBUTE);
        }
        return orderList;
    }

    @Override
    public Order getOrder(HttpServletRequest request, String orderId) {
        if (StringUtils.isBlank(orderId)) {
            throw new IdNotFoundException();
        }
        orderList = getOrderList(request);
        return orderList.stream()
                .filter(order -> Objects.equals(order.getId(), orderId))
                .findAny()
                .orElseThrow(OrderNotFoundException::new);
    }

    @Override
    public void addOrder(HttpServletRequest request, Order order) {
        if (Objects.isNull(order)) {
            throw new OrderNotFoundException();
        }
        synchronized (request.getSession()) {
            getOrderList(request).add(order);
        }
    }

    @Override
    public void deleteOrder(HttpServletRequest request, String orderId) {
        if (StringUtils.isBlank(orderId)) {
            throw new IdNotFoundException();
        }
        synchronized (request.getSession()) {
            getOrderList(request).remove(getOrder(request, orderId));
        }
    }
}
