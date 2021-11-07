package com.example.shoplist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder>{

    List<ShoppingList> shoppingList;
    OnListListener mOnListListener;

    FragmentManager fragManager;

    public interface OnListListener{
        void onListClick(int position);
        void onRenameClick(int position);
        void onRemindClick(String title);
        void onListLongClick(int position);
    }

    public ShoppingListAdapter(List<ShoppingList> shoppingList, OnListListener onListListener)
    {
        super();
        this.shoppingList = shoppingList;
        mOnListListener = onListListener;
    }

    public static class ShoppingListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        View view;
        int defaultColor;
        TextView textViewList;
        TextView textViewCount;
        Button bttnOptions;

        OnListListener onListListener;

        public ShoppingListViewHolder(@NonNull View itemView, OnListListener onListListener) {
            super(itemView);
            view = itemView;
            defaultColor = Color.parseColor("#FFBB86FC");
            textViewList = view.findViewById(R.id.tvList);
            textViewCount = view.findViewById(R.id.tvListCount);
            bttnOptions = view.findViewById(R.id.bttnOptions);
            this.onListListener = onListListener;

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onListListener.onListClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v)
        {
            onListListener.onListLongClick(getAdapterPosition());
            return true;
        }
    }

    @NonNull
    @Override
    public ShoppingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shopping_list, parent, false);
        return new ShoppingListViewHolder(view, mOnListListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingListViewHolder holder, int position) {
        ShoppingList list = shoppingList.get(position);
        //Checks if list exceeds budget
        if (MainActivity.budgetLimit > 0 && list.GetListCost() > MainActivity.budgetLimit)
        {
            holder.view.setBackgroundColor(Color.RED);
        }
        else
        {
            holder.view.setBackgroundColor(holder.defaultColor);
        }

        holder.textViewList.setText(list.title);
        holder.textViewCount.setText(String.valueOf(list.itemCount));

        holder.bttnOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(view.getContext(), holder.bttnOptions);
                popup.inflate(R.menu.list_options_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.optionRename:
                                mOnListListener.onRenameClick(holder.getAdapterPosition());
                                return true;
                            case R.id.optionRemove:
                                removeListEntry(holder.getAdapterPosition());
                                return true;
                            case R.id.optionRemind:
                                mOnListListener.onRemindClick(list.title);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popup.show();

            }
        });
    }



    @Override
    public int getItemCount() {
        return shoppingList.size();
    }

    private void removeListEntry(int position)
    {
        shoppingList.remove(position);
        //refresh view
        notifyItemRemoved(position);
    }

    public void setFragmentManager(FragmentManager manager)
    {
        fragManager = manager;
    }

    public void UpdateList(List<ShoppingList> newList)
    {
        shoppingList = newList;
        notifyDataSetChanged();

    }
}
