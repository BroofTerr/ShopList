package com.example.shoplist;

public class ProductEntry {
    Product product;
    int quantity;
    boolean isChecked;

    public ProductEntry()
    {
        product = new Product();
        quantity = 0;
        isChecked = false;
    }

    public ProductEntry(Product p, int q, boolean isChecked)
    {
        product = p;
        quantity = q;
        this.isChecked = isChecked;
    }

    public float getCost()
    {
        return product.price * quantity;
    }
}
