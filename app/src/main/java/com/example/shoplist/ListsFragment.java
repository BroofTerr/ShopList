package com.example.shoplist;

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
        RenameListDialogue.RenameListDialogueListener {

    RecyclerView recyclerView;
    ShoppingListAdapter adapter;
    List<ShoppingList> shoppingList;

    LinearLayoutManager linearLayoutManager;
    int renameIndex;

    public ListsFragment() {
        // Required empty public constructor
        // Later load data
        shoppingList = new ArrayList<ShoppingList>();
        shoppingList.add(new ShoppingList("List1"));
        shoppingList.add(new ShoppingList("Test list"));

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

    @Override
    public void onListClick(int position) {
        //Implement transition to a new activity (where it shows what is in the list)
        Toast.makeText(getContext(),"You clicked on: " + shoppingList.get(position).title, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getContext(), ListActivity.class);
        intent.putExtra("list_object", shoppingList.get(position));
        startActivity(intent);
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
}