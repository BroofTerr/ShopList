package com.example.shoplist;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class BudgetFragment extends Fragment {

    EditText etBudget;
    TextView textView1;
    TextView tvConv;
    public static final String ParsingData = "{\"data\":{\"success\":\"true\",\"timestamp\":\"1636354863\",\"base\":\"EUR\",\"date\":\"2021-11-08\",\"rate\":\"4.248225\"}}";
    float conv;

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

        textView1 = fragView.findViewById(R.id.tvRate);
        tvConv = fragView.findViewById(R.id.tvCoverted);
        try
        {

            JSONObject jsonObject = new JSONObject(ParsingData).getJSONObject("data");
            String success = jsonObject.getString("success");
            String timestamp = jsonObject.getString("timestamp");
            String base = jsonObject.getString("base");
            String date = jsonObject.getString("date");
            String rate = jsonObject.getString("rate");

            String str = "Success:" + success + "\nstamp:" + timestamp + "\nbase:" + base + "\ndate:" + date + "\nrate:" + rate;
            textView1.setText(str);
            conv = Float.valueOf(rate) * MainActivity.budgetLimit;
        }
        catch (JSONException e) {}


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
                tvConv.setText("AED: " + String.valueOf(conv));
            }
        });

        return fragView;
    }
}