package com.example.shoplist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements ProductListAdapter.OnProductListener {

    ShoppingList data;
    List<ProductEntry> productList;
    List<ProductEntry> filteredList;

    RecyclerView recyclerView;
    ProductListAdapter adapter;

    String productFilterName;

    TextView tvTotal;
    TextView tvChecked;
    EditText etSearch;
    Button bttnSearch;

    boolean isFiltered;

    public ListActivity()
    {
        Product p1 = new Product("Apple",3f);
        Product p2 = new Product("Juice", 0.75f);
        ProductEntry e1 = new ProductEntry(p1, 2, false);
        ProductEntry e2 = new ProductEntry(p2, 1, true);
        productList = new ArrayList<>();
        productList.add(e1);
        productList.add(e2);

        adapter = new ProductListAdapter(productList, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbarList);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        data = getIntent().getParcelableExtra("list_object");

        if(data.productList != null && data.productList.size() != 0)
        {
            productList = data.productList;
        }

        setTitle(data.title);

        recyclerView = findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        tvTotal = findViewById(R.id.tvTotalCost);
        tvChecked = findViewById(R.id.tvCheckedCost);
        etSearch = findViewById((R.id.etSearch));
        bttnSearch = findViewById(R.id.bttnSearch);

        bttnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productFilterName = etSearch.getText().toString();
                FilterList();
            }
        });

        CalculateCosts(productList);
    }

    public void FilterList()
    {
        filteredList = new ArrayList<>();
        for (ProductEntry e : productList)
        {
            if (e.product.name.equals(productFilterName))
            {
                filteredList.add(e);
            }
        }

        if (productFilterName.equals(""))
        {
            adapter.UpdateList(productList);
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
        tvTotal.setText(String.valueOf(total));
        tvChecked.setText(String.valueOf(checked));
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
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProductClick(int position) {
        Toast.makeText(this,"You clicked on: " + productList.get(position).product.name, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProductLongClick(int position) {
        // Implement menu to remove the product or edit
        Toast.makeText(this,"You held on: " + productList.get(position).product.name, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCheck(int position) {
        float checked = Float.valueOf(tvChecked.getText().toString());
        ProductEntry e;

        if (!isFiltered)
        {
            e = productList.get(position);
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
        tvChecked.setText(String.valueOf(checked));
    }
}