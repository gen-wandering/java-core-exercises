package com.tasks.deepclone.model;

public class User implements Cloneable {
    private String name; // immutable
    private Address address;
    private SensitiveObject sensitiveObject;

    public User(String name, Address address, SensitiveObject sensitiveObject) {
        this.name = name;
        this.address = address;
        this.sensitiveObject = sensitiveObject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public SensitiveObject getSensitiveObject() {
        return sensitiveObject;
    }

    public void setSensitiveObject(SensitiveObject sensitiveObject) {
        this.sensitiveObject = sensitiveObject;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", address=" + address +
                ", sensitiveObject=" + sensitiveObject +
                '}';
    }

    @Override
    public User clone() {
        try {
            User clone = (User) super.clone();
            clone.address = clone.address.clone();
            clone.sensitiveObject = clone.sensitiveObject.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
