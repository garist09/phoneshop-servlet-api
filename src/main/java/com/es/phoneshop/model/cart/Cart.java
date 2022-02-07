package com.es.phoneshop.model.cart;

import com.es.phoneshop.exception.IdNotFoundException;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.product.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Cart {
    public static final String NON_POSITIVE = "Quantity is non-positive";
    public static final int INT0 = 0;

    private List<CartItem> cartItemList;

    public Cart() {
        this.cartItemList = new ArrayList<>();
    }

    public void add(Product product, int quantity) throws ProductNotFoundException, IdNotFoundException, IllegalArgumentException {
        if (Objects.isNull(product)) {
            throw new ProductNotFoundException();
        }
        if (quantity <= INT0) {
            throw new IllegalArgumentException(NON_POSITIVE);
        }
        cartItemList.stream()
                .filter(cartItem -> cartItem.getProduct().equals(product))
                .findAny()
                .ifPresentOrElse(cartItem -> cartItem.setQuantity(cartItem.getQuantity()+quantity),
                        () -> cartItemList.add(new CartItem(product, quantity)));
        product.setStock(product.getStock() - quantity);
    }

    public List<CartItem> getProducts() {
        return cartItemList;
    }
}
