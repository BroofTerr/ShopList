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
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class BudgetFragment extends Fragment {

    EditText etBudget;
    TextView textView1;
    TextView tvConv;

    ArrayList<HashMap<String, Float>> rates = new ArrayList<HashMap<String, Float>>();
    ArrayList<String> currencies = new ArrayList<>();
    float conv;

    public BudgetFragment() {

    }

    private String readJSON() {
        String data = null;
        try {
            InputStream stream = getActivity().getAssets().open("currencies.json");
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            data = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return data;
    }

    private void parseJSON()
    {
        try
        {

            JSONObject jsonObject = new JSONObject(readJSON());
            String success = jsonObject.getString("success");
            String timestamp = jsonObject.getString("timestamp");
            String base = jsonObject.getString("base");
            String date = jsonObject.getString("date");
            JSONObject ratesObject = jsonObject.getJSONObject("rates");

            String str = "Success:" + success + "\nstamp:" + timestamp + "\nbase:" + base + "\ndate:" + date;
            Iterator<String> iterator = ratesObject.keys();
            String rateStrings = "";
            while (iterator.hasNext())
            {
                String key = iterator.next();
                Float value = Float.valueOf(ratesObject.getString(key));
                HashMap<String, Float> pair = new HashMap<String, Float>();
                pair.put(key, value);
                currencies.add(key);
                rates.add(pair);
            }
        }
        catch (JSONException e) {}
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.fragment_budget, container, false);

        //reads from the "currencies" JSON file and parses currency/rate pairs
        parseJSON();

        etBudget = fragView.findViewById(R.id.etBudget);
        etBudget.setText(String.valueOf(MainActivity.budgetLimit));

        textView1 = fragView.findViewById(R.id.tvRate);
        tvConv = fragView.findViewById(R.id.tvConverted);

        textView1.setText(currencies.get(0) + ": " + rates.get(0).get(currencies.get(0)).toString());

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