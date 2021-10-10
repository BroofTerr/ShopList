package com.example.shoplist;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductListViewHolder> {

    List<ProductEntry> productList;
    ProductListAdapter.OnProductListener mOnProductListener;

    FragmentManager fragManager;

    public interface OnProductListener{
        void onProductClick(int position);
    }

    public ProductListAdapter(List<ProductEntry> productList, ProductListAdapter.OnProductListener onProductListener)
    {
        super();
        this.productList = productList;
        mOnProductListener = onProductListener;
    }

    public static class ProductListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View view;
        CheckBox checkBoxChecked;
        TextView textViewProduct;
        TextView textViewCount;
        TextView textViewPrice;

        ProductListAdapter.OnProductListener onProductListener;

        public ProductListViewHolder(@NonNull View itemView, ProductListAdapter.OnProductListener onProdctListener) {
            super(itemView);
            view = itemView;
            checkBoxChecked = view.findViewById(R.id.cbChecked);
            textViewProduct = view.findViewById(R.id.tvProduct);
            textViewCount = view.findViewById(R.id.tvProductCount);
            textViewPrice = view.findViewById(R.id.tvProductPrice);
            this.onProductListener = onProdctListener;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onProductListener.onProductClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public ProductListAdapter.ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductListAdapter.ProductListViewHolder(view, mOnProductListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListAdapter.ProductListViewHolder holder, int position) {
        ProductEntry entry = productList.get(position);

        holder.checkBoxChecked.setChecked(entry.isChecked);
        holder.textViewProduct.setText(entry.product.name);
        holder.textViewCount.setText(String.valueOf(entry.quantity));
        holder.textViewPrice.setText(String.valueOf(entry.getCost()));

        /*holder.bttnOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(view.getContext(), holder.bttnOptions);
                //inflating menu from xml resource
                popup.inflate(R.menu.list_options_menu);
                //adding click listener
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
                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();

            }
        });*/
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    private void removeListEntry(int position)
    {
        productList.remove(position);
        //refresh view
        notifyItemRemoved(position);
    }

    public void setFragmentManager(FragmentManager manager)
    {
        fragManager = manager;
    }

}
