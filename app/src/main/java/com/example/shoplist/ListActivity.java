package com.example.shoplist;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements ProductListAdapter.OnProductListener,
        NewProductDialogue.NewProductDialogueListener,
        EditProductDialogue.EditProductDialogueListener {

    ShoppingList data;
    //List<ProductEntry> productList;
    List<ProductEntry> filteredList;

    RecyclerView recyclerView;
    ProductListAdapter adapter;

    String productFilterName;

    TextView tvTotal;
    TextView tvChecked;
    TextView productName;
    ImageView productNameImage;
    TextView productCategory;
    ImageView productCategoryImage;
    EditText etSearch;
    Button bttnSearch;
    Button bttnChange;
    int count = 0;

    boolean isFiltered;
    boolean isTextEntry;

    int editIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setEnterTransition(new Slide());
        getWindow().setExitTransition(new Slide(Gravity.TOP));

        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbarList);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        data = getIntent().getParcelableExtra("list_object");

        /*if(data.productList != null && data.productList.size() != 0)
        {
            productList = data.productList;
        }*/
        adapter = new ProductListAdapter(data.productList, this);

        setTitle(data.title);

        recyclerView = findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        isTextEntry = true;
        tvTotal = findViewById(R.id.tvTotalCost);
        tvChecked = findViewById(R.id.tvCheckedCost);
        etSearch = findViewById((R.id.etSearch));
        bttnSearch = findViewById(R.id.bttnSearch);
        bttnChange = findViewById(R.id.bttnChange);

        bttnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productFilterName = etSearch.getText().toString();
                FilterList();
            }
        });

        bttnChange.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                for (ProductEntry e : data.productList)
                {
                    productName = findViewById(R.id.tvProduct);
                    productNameImage = findViewById(R.id.ProductImageView);
                    productCategory = findViewById(R.id.tvCategory);
                    productCategoryImage = findViewById(R.id.CategoryImageView);

                    if (isTextEntry)
                    {
                        isTextEntry = false;
                        productName.setVisibility(view.INVISIBLE);
                        productNameImage.setImageResource(R.drawable.apple); //later Change to image from product image
                        productNameImage.setVisibility(View.VISIBLE);
                        productCategory.setVisibility(view.INVISIBLE);
                        productCategoryImage.setImageResource(R.drawable.coupe_entree_plate_drk_brn_wht_o_ut); //later Change to image from category image
                        productCategoryImage.setVisibility(view.VISIBLE);
                    }
                    else
                    {
                        isTextEntry = true;
                        productName.setVisibility(view.VISIBLE);
                        productNameImage.setVisibility(View.INVISIBLE);
                        productCategory.setVisibility(view.VISIBLE);
                        productCategoryImage.setVisibility(view.INVISIBLE);
                    }
                }
            }
        });
        CalculateCosts(data.productList);
    }

    public void FilterList()
    {
        filteredList = new ArrayList<>();
        for (ProductEntry e : data.productList)
        {
            if (e.product.name.equals(productFilterName) || e.product.category.equals(productFilterName))
            {
                filteredList.add(e);
            }
        }

        if (productFilterName.equals(""))
        {
            adapter.UpdateList(data.productList);
            isFiltered = false;
        }
        else
        {
            adapter.UpdateList(filteredList);
            isFiltered = true;
        }

    }

    public void CalculateCosts(List<ProductEntry> list)
    {
        float total = 0, checked = 0;
        for (ProductEntry e : list)
        {
            total += e.getCost();
            if (e.isChecked) checked += e.getCost();
        }
        DecimalFormat frmt = new DecimalFormat("#.##");
        tvTotal.setText(frmt.format(total));
        tvChecked.setText(frmt.format(checked));

        if (MainActivity.budgetLimit > 0 && total > MainActivity.budgetLimit)
        {
            Toast.makeText(this, "List exceeds budget limit (" + MainActivity.budgetLimit + ")", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu, menu);
        getMenuInflater().inflate(R.menu.app_bar_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.appBarSearch).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.appBarAddNew:
                openAddProductDialogue();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("list_object", data);
        setResult(Activity.RESULT_OK, resultIntent);
        this.finishAfterTransition();
    }

    @Override
    public void onProductClick(int position) {
        onCheck(position);
    }

    @Override
    public void onProductLongClick(int position) {
        // Implement menu to remove the product or edit
        editIndex = position;
        //unfiltered
        EditProductDialogue editProdDial = new EditProductDialogue(data.productList.get(editIndex));
        editProdDial.show(getSupportFragmentManager(), "editProduct");
    }

    @Override
    public void onCheck(int position) {
        float checked = Float.valueOf(tvChecked.getText().toString());
        ProductEntry e;

        if (!isFiltered)
        {
            e = data.productList.get(position);
        }
        else
        {
            e = filteredList.get(position);
        }

        e.isChecked = !e.isChecked;
        if (e.isChecked)
        {
            checked += e.getCost();
        }
        else
        {
            checked -= e.getCost();
        }
        DecimalFormat frmt = new DecimalFormat("#.##");
        tvChecked.setText(frmt.format(checked));
    }

    @Override
    public void applyProduct(String productName, String productCategory, float productPrice, int productQuantity)
    {
        Product prod = new Product(productName, productCategory, productPrice);
        data.AddProduct(prod, productQuantity);
        adapter.notifyItemInserted(data.productList.size() - 1);
        CalculateCosts(data.productList);
    }

    @Override
    public void editProduct(String productName, String productCategory, float productPrice, int productQuantity)
    {
        ProductEntry e = data.productList.get(editIndex);
        e.product.name = productName;
        e.product.category = productCategory;
        e.product.price = productPrice;
        e.quantity = productQuantity;
        data.EditProduct(e, editIndex);
        adapter.notifyItemChanged(editIndex);
        CalculateCosts(data.productList);
    }

    @Override
    public void removeProduct()
    {
        //unfiltered
        data.RemoveProduct(editIndex);
        adapter.notifyItemRemoved(editIndex);
        CalculateCosts(data.productList);
    }

    public void openAddProductDialogue()
    {
        NewProductDialogue newProdDial = new NewProductDialogue();
        newProdDial.show(getSupportFragmentManager(), "addNewProduct");
    }

}