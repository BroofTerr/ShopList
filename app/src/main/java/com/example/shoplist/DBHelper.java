package com.example.shoplist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper  extends SQLiteOpenHelper {

    public Context context;
    public static final String DATABASE_NAME = "dataManager";

    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "data";
    public static final String KEY_ID = "id";
    public static final String KEY_IMG_URL = "ImgFavourite";

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

    }

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_IMG_URL + " TEXT" + ")";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + "";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    public void deleteEntry(long row) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME, KEY_ID + "=" + row, null);
    }
}

    /*public static final int DB_VERSION = 4;
    public static final String DB_NAME = "ShopList.db";

    public final class ShoppingListClassEntry implements BaseColumns {
        public static final String TABLE_NAME = "ShoppingList";

        //public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_ITEM_COUNT = "itemCount";
    }

    public final class ProductClassEntry implements BaseColumns {
        public static final String TABLE_NAME = "Product";

        //public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_PRICE = "price";
    }

    public final class ShoppingListProductClassEntry implements BaseColumns {
        public static final String TABLE_NAME = "ProductEntries";

        public static final String COLUMN_LIST_ID = "listId";
        public static final String COLUMN_PRODUCT_ID = "productId";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_ISCHECKED = "isChecked";
    }

    private static final String SQL_CREATE_LISTS =
            "CREATE TABLE " + ShoppingListClassEntry.TABLE_NAME + " (" +
                ShoppingListClassEntry._ID + " INTEGER PRIMARY KEY, " +
                ShoppingListClassEntry.COLUMN_TITLE + ", " +
                ShoppingListClassEntry.COLUMN_ITEM_COUNT + ")";

    private static final String SQL_CREATE_PRODUCTS =
            "CREATE TABLE " + ProductClassEntry.TABLE_NAME + " (" +
                    ProductClassEntry._ID + " INTEGER PRIMARY KEY, " +
                    ProductClassEntry.COLUMN_NAME + ", " +
                    ProductClassEntry.COLUMN_CATEGORY + ", " +
                    ProductClassEntry.COLUMN_PRICE + ")";

    private static final String SQL_CREATE_LISTPRODUCTS =
            "CREATE TABLE " + ShoppingListProductClassEntry.TABLE_NAME + " (" +
                    ShoppingListProductClassEntry.COLUMN_LIST_ID + " INTEGER PRIMARY KEY, " +
                    ShoppingListProductClassEntry.COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY, " +
                    ShoppingListProductClassEntry.COLUMN_QUANTITY + ", " +
                    ShoppingListProductClassEntry.COLUMN_ISCHECKED + ")";

    private static final String SQL_DELETE_LISTS =
            "DROP TABLE IF EXISTS " + ShoppingListClassEntry.TABLE_NAME;

    private static final String SQL_DELETE_PRODUCTS =
            "DROP TABLE IF EXISTS " + ProductClassEntry.TABLE_NAME;

    private static final String SQL_DELETE_LISTPRODUCTS =
            "DROP TABLE IF EXISTS " + ShoppingListProductClassEntry.TABLE_NAME;

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
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + PRODUCT_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + COLUMN_PRODUCT_NAME + ", " +
                 COLUMN_PRODUCT_CATEGORY + ", " + COLUMN_PRODUCT_PRICE + ", " + COLUMN_PRODUCT_QUANTITY + ", " + COLUMN_PRODUCT_CHECKED + ", "
                + COLUMN_PRODUCT_LIST_TITLE + ", " + COLUMN_PRODUCT_LIST_COUNT + ")";

        db.execSQL(createTable);
        Log.i("OLDCREATE", createTable);

        /*db.execSQL(SQL_CREATE_LISTS);
        db.execSQL(SQL_CREATE_PRODUCTS);
        db.execSQL(SQL_CREATE_LISTPRODUCTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addList(ShoppingList shoppingList)
    {
        Log.i("ListCreate", SQL_CREATE_LISTS);
        Log.i("ProductCreate", SQL_CREATE_PRODUCTS);
        Log.i("ListProductCreate", SQL_CREATE_LISTPRODUCTS);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues listValues = new ContentValues();
        listValues.put(ShoppingListClassEntry.COLUMN_TITLE, shoppingList.title);
        listValues.put(ShoppingListClassEntry.COLUMN_ITEM_COUNT, shoppingList.itemCount);

        long newListId = db.insert(ShoppingListClassEntry.TABLE_NAME, null, listValues);

        if (newListId == -1)
        {
            return false;
        }

        for (ProductEntry entry : shoppingList.productList)
        {
            Product p = entry.product;
            ContentValues productValues = new ContentValues();
            productValues.put(ProductClassEntry.COLUMN_NAME, p.name);
            productValues.put(ProductClassEntry.COLUMN_CATEGORY, p.category);
            productValues.put(ProductClassEntry.COLUMN_PRICE, p.price);

            long newProductId = db.insert(ProductClassEntry.TABLE_NAME, null, productValues);

            ContentValues entryValues = new ContentValues();
            entryValues.put(ShoppingListProductClassEntry.COLUMN_LIST_ID, newListId);
            entryValues.put(ShoppingListProductClassEntry.COLUMN_PRODUCT_ID, newProductId);
            entryValues.put(ShoppingListProductClassEntry.COLUMN_QUANTITY, entry.quantity);
            entryValues.put(ShoppingListProductClassEntry.COLUMN_ISCHECKED, entry.isChecked);

            long newEntryId = db.insert(ShoppingListProductClassEntry.TABLE_NAME, null, entryValues);

            if (newProductId == -1 || newEntryId == -1)
            {
                return false;
            }
        }

        return true;

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
    }*/
