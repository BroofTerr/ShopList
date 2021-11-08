package com.example.shoplist;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class NewProductDialogue extends AppCompatDialogFragment {

    private EditText editTextProductName;
    private EditText editTextProductCategory;
    private EditText editTextProductPrice;
    private EditText editTextProductQuantity;
    private String productName;
    private String productCategory;
    private float productPrice;
    private int productQuantity;

    private NewProductDialogueListener listener;

    public interface NewProductDialogueListener{
        void applyProduct(String listTitle, String productName, String productCategory, float productPrice, int productQuantity);
        void applyProduct(String productName, String productCategory, float productPrice, int productQuantity);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (NewProductDialogueListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement NewProductDialogueListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_new_product, null);

        builder.setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                })
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBHelper dataBaseHelper = new DBHelper(getContext());
                        ShoppingList shopList = dataBaseHelper.getAll();

                        productName = editTextProductName.getText().toString();
                        productCategory = editTextProductCategory.getText().toString();
                        productPrice = Float.parseFloat(editTextProductPrice.getText().toString());
                        productQuantity = Integer.parseInt(editTextProductQuantity.getText().toString());

                        if (productName.equals("") || productCategory.equals("") || productPrice <= 0f || productQuantity <= 0)
                        { Toast.makeText(getContext(), "Field is empty or incorrect", Toast.LENGTH_SHORT).show(); }
                        else
                        {
                            listener.applyProduct(shopList.title, productName, productCategory, productPrice, productQuantity);
                        }
                    }
                });

        editTextProductName = view.findViewById(R.id.etNewProductName);
        editTextProductCategory = view.findViewById(R.id.etNewProductCategory);
        editTextProductPrice = view.findViewById(R.id.etNewProductPrice);
        editTextProductQuantity = view.findViewById(R.id.etNewProductQuantity);

        return builder.create();
    }


}
