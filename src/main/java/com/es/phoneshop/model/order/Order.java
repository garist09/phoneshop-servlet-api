package com.es.phoneshop.model.order;

import com.es.phoneshop.model.cart.CartItem;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Order implements Serializable {
    private static final Random random;

    private String id;
    private List<CartItem> cartItemList;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String deliveryAddress;
    private String deliveryDate;
    private String paymentMethod;
    private BigDecimal subtotal;
    private BigDecimal deliveryCost;
    private BigDecimal cartTotal;

    static {
        random = new Random();
    }

    public Order() {
        cartItemList = new ArrayList<>();
        this.setId(new UUID(random.nextLong(),random.nextLong()).toString());
    }

    public Order setId(String id) {
        this.id = id;
        return this;
    }

    public Order setCartItemList(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
        return this;
    }

    public Order setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public Order setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Order setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public Order setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
        return this;
    }

    public Order setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
        return this;
    }

    public Order setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public Order setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
        return this;
    }

    public Order setDeliveryCost(BigDecimal deliveryCost) {
        this.deliveryCost = deliveryCost;
        return this;
    }

    public Order setCartTotal(BigDecimal cartTotal) {
        this.cartTotal = cartTotal;
        return this;
    }

    public String getId() {
        return id;
    }

    public List<CartItem> getCartItemList() {
        return cartItemList;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getDeliveryCost() {
        return deliveryCost;
    }

    public BigDecimal getCartTotal() {
        return cartTotal;
    }
}
