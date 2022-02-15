package com.es.phoneshop.model.cart;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Cart implements Serializable {
    private List<CartItem> cartItemList;
    private int totalQuantity;
    private BigDecimal totalPrice;

    public Cart() {
        this.cartItemList = new ArrayList<>();
        totalPrice = BigDecimal.ZERO;
    }

    public List<CartItem> getProducts() {
        cartItemList = cartItemList.stream().filter(this::quantityComparing).collect(Collectors.toList());
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

    private boolean quantityComparing(CartItem cartItem) {
        if (Objects.isNull(cartItem)) {
            return false;
        }
        return cartItem.getQuantity() > 0;
    }
}
