package com.inventory.model;

import java.util.InputMismatchException;

public class Product {
    private int id;
    private String name;
    private String category;
    private int quantity;
    private double price;
    private int threshold;

    public int getThreshold() {
        return this.threshold;
    }

    public void setThreshold(final int threshold) {
        this.threshold = threshold;
    }

    public Product(int id, String name, String category, int quantity, double price) {
        setId(id);
        setName(name);
        setCategory(category);
        setQuantity(quantity);
        setPrice(price);
    }

    public double stockValue() {
        return quantity * price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name==null || name.trim().isEmpty()) throw new IllegalArgumentException("product name cannot be empty");
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if(id<0) throw new IllegalArgumentException("❌Id must be greater than 0");
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if(quantity <0) throw new InputMismatchException("❌Quantity must be greater than 0");
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if(price < 0) throw new InputMismatchException("❌Price cannot be negitive");
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        if(category==null || category.trim().isEmpty()) throw new IllegalArgumentException("category cannot be empty");
        this.category = category;
    }

    public void display() {
        System.out.println("Product ID : " + id);
        System.out.println("Name : " + name);
        System.out.println("Category: " + category);
        System.out.println("Quantity: " + quantity);
        System.out.println("Price: " + price);
        System.out.println("Stock Value: " + stockValue());
        System.out.println();
        }


}
