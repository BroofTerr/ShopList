package com.example.shoplist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper  extends SQLiteOpenHelper {

    public static final String PRODUCT_TABLE = "PRODUCT_TABLE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_PRODUCT_NAME = "PRODUCT_NAME";
    public static final String COLUMN_PRODUCT_CATEGORY = "PRODUCT_CATEGORY";
    public static final String COLUMN_PRODUCT_PRICE = "PRODUCT_PRICE";
    public static final String COLUMN_PRODUCT_QUANTITY = "PRODUCT_QUANTITY";
    public static final String COLUMN_PRODUCT_CHECKED = "PRODUCT_CHECKED";
    public static final String COLUMN_PRODUCT_LIST_TITLE = "PRODUCT_LIST_TITLE";
    public static final String COLUMN_PRODUCT_LIST_COUNT = "PRODUCT_LIST_COUNT";


    public DBHelper(@Nullable Context context) {
        super(context, "shoppingList.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + PRODUCT_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + COLUMN_PRODUCT_NAME + ", " +
                 COLUMN_PRODUCT_CATEGORY + ", " + COLUMN_PRODUCT_PRICE + ", " + COLUMN_PRODUCT_QUANTITY + ", " + COLUMN_PRODUCT_CHECKED + ", "
                + COLUMN_PRODUCT_LIST_TITLE + ", " + COLUMN_PRODUCT_LIST_COUNT + ")";

        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addToDatabase(ShoppingList shoppingList)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        for (ProductEntry prodEntry:
             shoppingList.productList)
        {
            cv.put(COLUMN_PRODUCT_NAME, prodEntry.product.getName());
            cv.put(COLUMN_PRODUCT_CATEGORY, prodEntry.product.getCategory());
            cv.put(COLUMN_PRODUCT_PRICE, prodEntry.product.getPrice());
            cv.put(COLUMN_PRODUCT_QUANTITY, prodEntry.quantity);
            cv.put(COLUMN_PRODUCT_CHECKED, prodEntry.isChecked);
            cv.put(COLUMN_PRODUCT_LIST_TITLE, shoppingList.title);
            cv.put(COLUMN_PRODUCT_LIST_COUNT, shoppingList.productList.size());
        }

        long insert = db.insert(PRODUCT_TABLE, null, cv);
        if (insert == -1) { return false; }
        else { return true; }
    }

    public boolean deleteProductFromDatabase(int index)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + PRODUCT_TABLE + " WHERE " + COLUMN_ID + " = " + index;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst())
            return true;
        else return false;

    }

    public ShoppingList getAll()
    {
        String listTitle = "";
        int listCount = 0;
        List<ProductEntry> returnProducts = new ArrayList<>();

        String query = "SELECT * FROM " + PRODUCT_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            do {
                int productID = cursor.getInt(0);
                String productName = cursor.getString(1);
                String productCategory = cursor.getString(2);
                float productPrice = cursor.getFloat(3);
                int productQuantity = cursor.getInt(4);
                boolean productChecked = cursor.getInt(5) == 1 ? true: false;
                listTitle = cursor.getString(6);
                listCount = cursor.getInt(7);

                ProductEntry newEntry = new ProductEntry(new Product(productID, productName,
                        productCategory, productPrice), productQuantity, productChecked);
                returnProducts.add(newEntry);

            } while (cursor.moveToNext());

        }
        ShoppingList shopList = new ShoppingList(listTitle, returnProducts);
        cursor.close();
        db.close();
        return shopList;
    }


}
