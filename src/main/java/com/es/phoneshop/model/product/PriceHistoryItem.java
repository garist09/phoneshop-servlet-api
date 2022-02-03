package com.es.phoneshop.model.product;

import java.math.BigDecimal;

public class PriceHistoryItem {
    private String startDate;
    private BigDecimal price;

    public PriceHistoryItem() {
    }

    public PriceHistoryItem(String startDate, BigDecimal price) {
        this.startDate = startDate;
        this.price = price;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getStartDate() {
        return startDate;
    }
}
