package com.example.shoplist;

public class ProductEntry {
    Product product;
    String category;
    int quantity;
    boolean isChecked;

    public ProductEntry()
    {
        product = new Product();
        category = "";
        quantity = 0;
        isChecked = false;
    }

    public ProductEntry(Product p, String c, int q, boolean isChecked)
    {
        product = p;
        category = c;
        quantity = q;
        this.isChecked = isChecked;
    }

    public float getCost()
    {
        return product.price * quantity;
    }
}
