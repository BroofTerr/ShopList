package com.example.shoplist;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ShoppingList implements Parcelable {

    String title;
    int itemCount;
    List<ProductEntry> productList;

    public ShoppingList(String title)
    {
        this.title = title;
        this.itemCount = 0;
        productList = new ArrayList<ProductEntry>();
    }

    protected ShoppingList(Parcel in) {
        title = in.readString();
        itemCount = in.readInt();
    }

    public static final Creator<ShoppingList> CREATOR = new Creator<ShoppingList>() {
        @Override
        public ShoppingList createFromParcel(Parcel in) {
            return new ShoppingList(in);
        }

        @Override
        public ShoppingList[] newArray(int size) {
            return new ShoppingList[size];
        }
    };

    public void AddProduct(Product product)
    {
        itemCount++;
    }

    public void RemoveProduct(Product product)
    {
        itemCount--;
    }

    public void ChangeQuantity(Product product, int quantity)
    {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(itemCount);
    }
}
