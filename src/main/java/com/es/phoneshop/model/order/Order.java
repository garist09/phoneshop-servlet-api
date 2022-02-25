package com.es.phoneshop.model.order;

import com.es.phoneshop.model.cart.CartItem;

import java.io.Serializable;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order implements Serializable {
    private static final SecureRandom secureRandom;

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
        secureRandom = new SecureRandom();
    }

    public Order() {
        cartItemList = new ArrayList<>();
        this.setId(new UUID(secureRandom.nextLong(), secureRandom.nextLong()).toString());
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCartItemList(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public void setDeliveryCost(BigDecimal deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public void setCartTotal(BigDecimal cartTotal) {
        this.cartTotal = cartTotal;
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
