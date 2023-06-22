package com.akashaba.ProductQuery;

public class StockEvent {
    private String type;
    private Stock stock;

    public StockEvent(String type, Stock stock) {
        this.type = type;
        this.stock = stock;
    }

    public StockEvent() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }
}
