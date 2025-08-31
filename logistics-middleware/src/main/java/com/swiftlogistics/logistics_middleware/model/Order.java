package com.swiftlogistics.logistics_middleware.model;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess;

import java.util.List;

public class Order {
    private String id;
    private List<AbstractReadWriteAccess.Item> items;
    private String status;
    private long timestamp;

    public Order() {
    }
    public Order(String id, List<Item>, String status, long timestamp){
        this.id = id;
        this.items = items;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public List<Item> getItems() {
        return items;
    }
    public void setItems(List<Item> items) {
        this.items = items;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString(){
        return "Order{" +
                "id='" + id + '\'' +
                ", items=" + items +
                ", status='" + status + '\'' +
                ", timestamp=" + timestamp +
                '}';

    }

    public static class Item {
        private String sku;
        private int quantity;

        public Item(){

        }
        public Item(String sku, int quantity){
            this.sku = sku;
            this.quantity = quantity;
        }

        public String getSku() {
            return sku;
        }
        public void setSku(String sku) {
            this.sku = sku;
        }
        public int getQuantity() {
            return quantity;
        }
        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "sku='" + sku + '\'' +
                    ", quantity=" + quantity +
                    '}';
        }
    }
}
