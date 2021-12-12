package com.example.shoplist;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    //int id;
    String name;
    String category;
    float price;

    public Product()
    {
        //int id = 0;
        name = "";
        category = "";
        price = 0f;
    }

    public Product(String name, String category, float price)
    {
        this.name = name;
        this.category = category;
        this.price = price;
    }
    public Product(int id, String name, String category, float price)
    {
        //this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
    }

    /*public int getId() {
        return id;
    }*/

    /*public void setId(int id) {
        this.id = id;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    protected Product(Parcel in) {
        name = in.readString();
        category = in.readString();
        price = in.readFloat();
    }

    @Override
    public String toString() {
        return "Product{" +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(category);
        dest.writeFloat(price);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}

