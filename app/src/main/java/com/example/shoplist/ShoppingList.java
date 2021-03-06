package com.example.shoplist;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ShoppingList implements Parcelable {

    String title;
    int itemCount;
    List<ProductEntry> productList;

    private float totalCost;

    public ShoppingList(String title)
    {
        this.title = title;
        this.itemCount = 0;
        productList = new ArrayList<ProductEntry>();
    }

    public ShoppingList(String title, List<ProductEntry> list)
    {
        this.title = title;
        productList = list;
        itemCount = list.size();
    }

    protected ShoppingList(Parcel in) {
        title = in.readString();
        itemCount = in.readInt();
        if (in.readByte() == 0x01) {
            productList = new ArrayList<ProductEntry>();
            in.readList(productList, ProductEntry.class.getClassLoader());
        } else {
            productList = null;
        }
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(itemCount);
        if (productList == null)
        {
            dest.writeByte((byte) (0x00));
        }
        else
        {
            dest.writeByte((byte) (0x01));
            dest.writeList(productList);
        }

    }

    public void AddProduct(Product product, int quantity)
    {
        productList.add(new ProductEntry(product, quantity, false));
        itemCount++;
    }

    public void AddProduct(Product product, int quantity, boolean isChecked)
    {
        productList.add(new ProductEntry(product, quantity, isChecked));
        itemCount++;
    }

    public void RemoveProduct(int position)
    {
        productList.remove(position);
        itemCount--;
    }

    public void EditProduct(ProductEntry entry, int position)
    {
        productList.set(position, entry);
    }

    public float GetListCost()
    {
        totalCost = 0;
        for (ProductEntry e : productList)
        {
            totalCost += e.getCost();
        }
        return totalCost;
    }

    @Override
    public String toString()
    {
        String listData = "";

        listData += title + ";" + itemCount + "\n";
        for (ProductEntry entry : productList)
        {
            Product p = entry.product;
            listData += p.name + ";" + p.category + ";" + p.price + ";" + entry.quantity + ";" + entry.isChecked + "\n";
        }

        return listData;
    }

}
