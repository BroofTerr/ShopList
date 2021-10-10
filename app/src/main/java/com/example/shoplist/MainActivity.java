package com.example.shoplist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    Fragment _listsFragment;
    Fragment _statsFragment;
    Fragment _budgetFragment;
    BottomNavigationView _botNavMenu;

    // Swaps between activity fragments
    private void setCurrentFragment(Fragment frag)
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayoutFragment, frag)
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _listsFragment = new ListsFragment();
        _statsFragment = new StatsFragment();
        _budgetFragment = new BudgetFragment();
        setCurrentFragment(_listsFragment);

        _botNavMenu = findViewById(R.id.bottomNavigationView);
        _botNavMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navLists:
                        setCurrentFragment(_listsFragment);
                        break;

                    case R.id.navBudget:
                        setCurrentFragment(_budgetFragment);
                        break;

                    case R.id.navStats:
                        setCurrentFragment(_statsFragment);
                        break;
                }
                return true;
            }
        });
    }

}