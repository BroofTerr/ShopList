package com.example.shoplist;

public class Product {
    String name;
    //category;
    float price;
    //Image;

    public Product()
    {
        name = "";
        price = 0f;
    }

    public Product(String name, float price)
    {
        this.name = name;
        this.price = price;
    }
}

