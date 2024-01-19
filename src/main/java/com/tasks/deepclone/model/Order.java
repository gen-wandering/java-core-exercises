package com.tasks.deepclone.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Order implements Cloneable {
    private LocalDate orderDate; // immutable
    private List<Product> products;
    private User user;

    public Order(LocalDate orderDate, List<Product> products, User user) {
        this.orderDate = orderDate;
        this.products = products;
        this.user = user;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderDate=" + orderDate +
                ", products=" + products +
                ", user=" + user +
                '}';
    }

    @Override
    public Order clone() {
        try {
            Order clone = (Order) super.clone();

            clone.user = clone.user.clone();

            List<Product> productsCopy = new ArrayList<>();
            clone.products.forEach(product -> productsCopy.add(product.clone()));
            clone.products = productsCopy;

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}