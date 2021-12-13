package com.example.shoplist;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PictureStoreDatabaseFragment extends Fragment
{
    RecyclerView recyclerView;
    private DBHelper myDatabase;
    private SQLiteDatabase db;
    private ArrayList singleRowArrayList;
    private Picture singleRow;
    String image;
    int uid;
    Cursor cursor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stored_pictures,container,false);
        recyclerView = view.findViewById(R.id.recyclerview);
        myDatabase = new DBHelper(getContext());
        db = myDatabase.getWritableDatabase();
        setData();
        return view;
    }

    private void setData() {
        db = myDatabase.getWritableDatabase();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        singleRowArrayList = new ArrayList<>();
        String[] columns = {DBHelper.KEY_ID, DBHelper.KEY_IMG_URL};
        cursor = db.query(DBHelper.TABLE_NAME, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {

            int index1 = cursor.getColumnIndex(DBHelper.KEY_ID);
            int index2 = cursor.getColumnIndex(DBHelper.KEY_IMG_URL);

            uid = cursor.getInt(index1);
            image = cursor.getString(index2);

            singleRow = new Picture(image,uid);
            singleRowArrayList.add(singleRow);
        }
        if (singleRowArrayList.size()==0){
            //empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else {
            DBAdapter localDataBaseResponse = new DBAdapter(getContext(), singleRowArrayList, db, myDatabase);
            recyclerView.setAdapter(localDataBaseResponse);
        }


    }
}
