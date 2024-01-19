package com.tasks.deepclone.model;

public class Address implements Cloneable {
    @Override
    public String toString() {
        return "Address{}";
    }

    @Override
    public Address clone() {
        try {
            return (Address) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
