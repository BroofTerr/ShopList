package com.example.shoplist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    Fragment _listsFragment;
    Fragment _statsFragment;
    Fragment _budgetFragment;
    BottomNavigationView _botNavMenu;

    //later load saved budget
    static float budgetLimit = 0;

    public static void setBudgetLimit(float newLimit)
    {
        budgetLimit = newLimit;
    }

    // Swaps between activity fragments
    private void setCurrentFragment(Fragment frag)
    {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in,
                        R.anim.fade_out)
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

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel("reminder",
                    "reminderChannel", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

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