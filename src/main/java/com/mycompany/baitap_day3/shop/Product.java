package com.mycompany.baitap_day3.shop;

import java.util.List;

public class Product {

    public static final List<Product> CATALOG = List.of(
            new Product("86", "86 (the band) - True Life Songs and Pictures", 14.95),
            new Product("pf01", "Paddlefoot - The first CD", 12.95),
            new Product("pf02", "Paddlefoot - The second CD", 14.95),
            new Product("jr01", "Joe Rut - Genuine Wood Grained Finish", 14.95));

    private final String code;
    private final String description;
    private final double price;

    public Product(String code, String description, double price) {
        this.code = code;
        this.description = description;
        this.price = price;
    }

    public static Product find(String code) {
        for (Product p : CATALOG) {
            if (p.code.equals(code)) {
                return p;
            }
        }
        return null;
    }

    public String getCode() { return code; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
}
