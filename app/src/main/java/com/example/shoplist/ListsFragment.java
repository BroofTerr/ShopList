package com.example.shoplist;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
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

    public ListsFragment() {

        // Later load data (shoppingList = data list)
        shoppingList = new ArrayList<ShoppingList>();

        ShoppingList entryList = new ShoppingList("List1");
        Product p1 = new Product("Apple", "Fruit",3.33f);
        entryList.AddProduct(p1, 2);
        shoppingList.add(entryList);

        adapter = new ShoppingListAdapter(shoppingList, this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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
            // Call the DB Update method
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