package com.example.foobarfactory;

public abstract class Product {
    private final String serialNumber;

    public Product(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Override
    public String toString() {
        return serialNumber;
    }
}
