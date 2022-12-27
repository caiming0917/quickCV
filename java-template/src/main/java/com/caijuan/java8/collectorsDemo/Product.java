package com.caijuan.java8.collectorsDemo;

import java.math.BigDecimal;

/**
 * https://blog.csdn.net/u014231523/article/details/102535902
 */
public class Product {

    private Long id;
    private int num;
    private BigDecimal price;
    private String name;
    private String category;

    public Product(Long id, Integer num, BigDecimal price, String name, String category) {
        this.id = id;
        this.num = num;
        this.price = price;
        this.name = name;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public int getNum() {
        return num;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", num=" + num +
                ", price=" + price +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
