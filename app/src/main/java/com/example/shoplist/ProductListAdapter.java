package com.example.shoplist;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
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

    public interface OnProductListener{
        void onProductClick(int position);
        void onProductLongClick(int position);
        void onCheck(int position);
    }

    public ProductListAdapter(List<ProductEntry> productList, ProductListAdapter.OnProductListener onProductListener)
    {
        super();
        this.productList = productList;
        mOnProductListener = onProductListener;
    }

    public static class ProductListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        View view;
        CheckBox checkBoxChecked;
        TextView textViewProduct;
        TextView textViewCategory;
        TextView textViewCount;
        TextView textViewPrice;

        ProductListAdapter.OnProductListener onProductListener;

        public ProductListViewHolder(@NonNull View itemView, ProductListAdapter.OnProductListener onProductListener) {
            super(itemView);
            view = itemView;
            checkBoxChecked = view.findViewById(R.id.cbChecked);
            textViewProduct = view.findViewById(R.id.tvProduct);
            textViewCategory = view.findViewById(R.id.tvCategory);
            textViewCount = view.findViewById(R.id.tvProductCount);
            textViewPrice = view.findViewById(R.id.tvProductPrice);
            this.onProductListener = onProductListener;

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            checkBoxChecked.setChecked(!checkBoxChecked.isChecked());
            onProductListener.onProductClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            onProductListener.onProductLongClick(getAdapterPosition());
            return true;
        }
    }

    @NonNull
    @Override
    public ProductListAdapter.ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                    {
                        v.animate().alpha(0.5f).setDuration(200).start();
                        break;
                    }
                    default:
                        v.animate().alpha(1f).setDuration(200).start();
                }
                return false;
            }
        });

        return new ProductListAdapter.ProductListViewHolder(view, mOnProductListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListAdapter.ProductListViewHolder holder, int position) {
        ProductEntry entry = productList.get(position);

        holder.checkBoxChecked.setChecked(entry.isChecked);
        holder.textViewProduct.setText(entry.product.name);
        holder.textViewCategory.setText(entry.product.category);
        holder.textViewCount.setText(String.valueOf(entry.quantity));
        holder.textViewPrice.setText(String.valueOf(entry.product.price));

        holder.checkBoxChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mOnProductListener.onCheck(holder.getAdapterPosition());
            }
        });

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

    public void UpdateList(List<ProductEntry> newList)
    {
        productList = newList;
        notifyDataSetChanged();

    }

}
