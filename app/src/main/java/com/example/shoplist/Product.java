package com.example.shoplist;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    String name;
    String category;
    float price;
    //Image;

    public Product()
    {
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

    protected Product(Parcel in) {
        name = in.readString();
        category = in.readString();
        price = in.readFloat();
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

