package com.tasks.completed.deepclone.model;

public class Product implements Cloneable {
    private String name; // immutable
    private String description; // immutable
    private double price;
    private SensitiveObject sensitiveObject;

    public Product(String name, String description,
                   double price, SensitiveObject sensitiveObject) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.sensitiveObject = sensitiveObject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public SensitiveObject getSensitiveObject() {
        return sensitiveObject;
    }

    public void setSensitiveObject(SensitiveObject sensitiveObject) {
        this.sensitiveObject = sensitiveObject;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", sensitiveObject=" + sensitiveObject +
                '}';
    }

    @Override
    public Product clone() {
        try {
            Product clone = (Product) super.clone();
            clone.sensitiveObject = clone.sensitiveObject.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
