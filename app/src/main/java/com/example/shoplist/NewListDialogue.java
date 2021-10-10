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
import androidx.fragment.app.DialogFragment;

public class NewListDialogue extends AppCompatDialogFragment {

    private EditText editTextListName;
    private String listTitle;

    private NewListDialogueListener listener;

    public interface NewListDialogueListener{
        void applyList(String listTitle);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (NewListDialogueListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement NewListDialogueListener");
        }
    }

    // To not make the dialogue disappear, need to add custom buttons to the layout
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_new_list, null);

        builder.setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listTitle = editTextListName.getText().toString();
                        if (listTitle.equals(""))
                        {
                            Toast.makeText(getContext(), "Field is empty", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            listener.applyList(listTitle);
                        }

                    }
                });

        editTextListName = view.findViewById(R.id.etNewList);

        return builder.create();
    }


}
