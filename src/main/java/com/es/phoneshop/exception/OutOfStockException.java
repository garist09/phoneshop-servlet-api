package com.es.phoneshop.exception;

public class OutOfStockException extends RuntimeException {
    private int stock;

    public OutOfStockException(int stock) {
        this.stock = stock;
    }

    public int getStock() {
        return stock;
    }

}
