package com.example.shoplist;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.shoplist.PictureFragment;

public class PictureActivity extends AppCompatActivity
{

    FrameLayout frameLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictures);

        loadFragment(new PictureFragment(), false);
    }

    public void loadFragment(Fragment fragment, Boolean bool)
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);

        if (bool)
        {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

}
