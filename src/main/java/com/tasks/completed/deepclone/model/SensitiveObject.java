package com.tasks.completed.deepclone.model;

public class SensitiveObject implements Cloneable {
    private int secretCode;

    public SensitiveObject(int secretCode) {
        this.secretCode = secretCode;
    }

    public int getSecretCode() {
        return secretCode;
    }

    public void setSecretCode(int secretCode) {
        this.secretCode = secretCode;
    }

    @Override
    public String toString() {
        return "SensitiveObject{" +
                "secretCode=" + secretCode +
                '}';
    }

    @Override
    public SensitiveObject clone() {
        try {
            return (SensitiveObject) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
