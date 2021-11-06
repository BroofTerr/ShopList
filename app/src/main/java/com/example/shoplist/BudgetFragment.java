package com.example.shoplist;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class BudgetFragment extends Fragment {

    EditText etBudget;

    public BudgetFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.fragment_budget, container, false);

        etBudget = fragView.findViewById(R.id.etBudget);
        etBudget.setText(String.valueOf(MainActivity.budgetLimit));

        etBudget.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!etBudget.getText().equals("") && etBudget.getText().length() > 0)
                {
                    MainActivity.setBudgetLimit(Float.parseFloat(etBudget.getText().toString()));
                }
                else
                {
                    MainActivity.setBudgetLimit(0);
                }
            }
        });

        return fragView;
    }
}