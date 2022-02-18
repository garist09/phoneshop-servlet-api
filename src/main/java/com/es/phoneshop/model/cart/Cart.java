package com.es.phoneshop.model.cart;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Cart implements Serializable {
    private List<CartItem> cartItemList;
    private int totalQuantity;
    private BigDecimal totalPrice;

    public Cart() {
        this.cartItemList = new ArrayList<>();
        totalPrice = BigDecimal.ZERO;
    }

    public List<CartItem> getCartItems() {
        return cartItemList;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
