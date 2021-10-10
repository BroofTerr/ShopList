package com.example.shoplist;

public class ProductEntry {
    Product product;
    int quantity;
    boolean isChecked;

    public float getCost()
    {
        return product.price * quantity;
    }
}
