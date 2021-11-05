package com.example.shoplist;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductEntry implements Parcelable {
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

    protected ProductEntry(Parcel in) {
        quantity = in.readInt();
        isChecked = in.readByte() != 0;
        product = (Product) in.readValue(Product.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(quantity);
        dest.writeByte((byte) (isChecked ? 1 : 0));
        dest.writeValue(product);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductEntry> CREATOR = new Creator<ProductEntry>() {
        @Override
        public ProductEntry createFromParcel(Parcel in) {
            return new ProductEntry(in);
        }

        @Override
        public ProductEntry[] newArray(int size) {
            return new ProductEntry[size];
        }
    };

    public float getCost()
    {
        return product.price * quantity;
    }
}
