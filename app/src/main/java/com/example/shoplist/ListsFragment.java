package com.example.shoplist;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Debug;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListsFragment extends Fragment implements NewListDialogue.NewListDialogueListener,
        ShoppingListAdapter.OnListListener,
        RenameListDialogue.RenameListDialogueListener,
        TimePickerDialogue.TimePickerDialogueListener {

    RecyclerView recyclerView;
    ShoppingListAdapter adapter;
    List<ShoppingList> shoppingList;

    LinearLayoutManager linearLayoutManager;
    int renameIndex;
    int activeIndex;

    String filename = "shoplistData.txt";
    File dataDir;

    boolean hasRead = false;

    public ListsFragment() {

        // Later load data (shoppingList = data list)
        shoppingList = new ArrayList<ShoppingList>();

        adapter = new ShoppingListAdapter(shoppingList, this);
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void managePermissions()
    {
        ActivityCompat.requestPermissions(getActivity(), new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
    }

    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readFromFile();
            }
        }
    }

    private void saveToFile(List<ShoppingList> shoppingList)
    {
        String data = "";
        data += shoppingList.size() + "\n";
        for(ShoppingList list : shoppingList)
        {
            data += list.toString();
        }
        Log.i("ListData", data);

        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED))
        {
            try
            {
                File externalFile = new File(dataDir, filename);
                if (externalFile.exists())
                {
                    externalFile.delete();
                }
                externalFile.createNewFile();

                Log.i("DIR", externalFile.toString());
                FileOutputStream fos = null;

                fos = new FileOutputStream(externalFile);
                fos.write(data.getBytes());
                fos.close();
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void readFromFile()
    {
        dataDir = getActivity().getFilesDir();
        if (checkPermissions())
        {
            FileReader fr = null;
            File externalFile = new File(dataDir, filename);
            StringBuilder strBuilder = new StringBuilder();
            try {
                fr = new FileReader(externalFile);
                BufferedReader br = new BufferedReader(fr);
                String line = br.readLine();
                while (line != null)
                {
                    strBuilder.append(line).append('\n');
                    line = br.readLine();
                }
                fr.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("DATAFROMFILE", strBuilder.toString());
            parseData(strBuilder.toString());
        }
        else
        {
            managePermissions();
        }

    }

    private void parseData(String data)
    {
        String[] tokens = data.split("\\r?\\n");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Iterator<String> iterator = Arrays.stream(tokens).iterator();
            int listCount = Integer.valueOf(iterator.next());
            for (int i = 0; i < listCount; i++)
            {
                String listData = iterator.next();
                String[] listDataTokens = listData.split(";");
                String listTitle = listDataTokens[0];
                int productCount = Integer.valueOf(listDataTokens[1]);

                ShoppingList list = new ShoppingList(listTitle);

                for (int j = 0; j < productCount; j++)
                {
                    String productData = iterator.next();
                    String[] productDataTokens = productData.split(";");
                    String productName = productDataTokens[0];
                    String productCategory = productDataTokens[1];
                    Float productPrice = Float.valueOf(productDataTokens[2]);
                    int productQuantity = Integer.valueOf(productDataTokens[3]);
                    boolean productIsChecked = Boolean.valueOf(productDataTokens[4]);

                    Product p = new Product(productName, productCategory,productPrice);
                    list.AddProduct(p, productQuantity, productIsChecked);
                }
                shoppingList.add(list);
            }
        }
        adapter.UpdateList(shoppingList);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (!hasRead)
        {
            readFromFile();
            hasRead = true;
        }

        View fragView = inflater.inflate(R.layout.fragment_lists, container, false);
        recyclerView = fragView.findViewById(R.id.recyclerViewShoppingLists);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(fragView.getContext()));
        adapter.setFragmentManager(getFragmentManager());
        recyclerView.setAdapter(adapter);

        return fragView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.app_bar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.appBarAddNew:
                openAddDialogue();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openAddDialogue()
    {
        NewListDialogue newDialogue = new NewListDialogue();
        newDialogue.setTargetFragment(ListsFragment.this, 1);
        newDialogue.show(getFragmentManager(), "NewListDialogue");
    }

    @Override
    public void applyList(String listTitle) {
        shoppingList.add(new ShoppingList(listTitle));
        adapter.notifyItemInserted(shoppingList.size() - 1);
        Toast.makeText(getContext(),"You created a new list: " + listTitle, Toast.LENGTH_SHORT).show();

        saveToFile(shoppingList);
    }

    //transition to a new activity (where it shows what is in the list)
    @Override
    public void onListClick(int position) {
        activeIndex = position;

        Intent intent = new Intent(getContext(), ListActivity.class);
        intent.putExtra("list_object", shoppingList.get(position));
        startActivityForResult(intent, 101, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        ShoppingList resultList = data.getParcelableExtra("list_object");
        if (requestCode == 101 && resultCode == Activity.RESULT_OK)
        {
            shoppingList.set(activeIndex, resultList);
            shoppingList.get(activeIndex).itemCount = resultList.itemCount;
            adapter.UpdateList(shoppingList);
            // Save to file

            saveToFile(shoppingList);

        }
    }


    @Override
    public void onListLongClick(int position)
    {
        Toast.makeText(getContext(), "Total: " + shoppingList.get(position).GetListCost(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRenameClick(int position) {
        renameIndex = position;
        RenameListDialogue newDialogue = new RenameListDialogue(shoppingList.get(position).title);
        newDialogue.setTargetFragment(ListsFragment.this, 1);
        newDialogue.show(getFragmentManager(), "RenameListDialogue");
    }

    @Override
    public void renameList(String listTitle) {
        shoppingList.get(renameIndex).title = listTitle;
        adapter.notifyItemChanged(renameIndex);

        saveToFile(shoppingList);
    }

    @Override
    public void onRemove()
    {
        saveToFile(shoppingList);
    }

    @Override
    public void onRemindClick(String title)
    {
        TimePickerDialogue newDialogue = new TimePickerDialogue(title);
        newDialogue.setTargetFragment(ListsFragment.this, 1);
        newDialogue.show(getFragmentManager(), "RenameListDialogue");
    }

    @Override
    public void onTimePick(int h, int m, int s, String title) {
        Toast.makeText(getContext(), "Reminder set", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getContext(), BroadcastReminder.class);
        intent.setAction(title);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        int seconds = (h * 60) + (m * 60) + s;

        long timeAtClick = System.currentTimeMillis();
        long remindInTime = 1000L * seconds; //convert from time into seconds

        alarmManager.set(AlarmManager.RTC_WAKEUP, timeAtClick + remindInTime, pendingIntent);
    }
}