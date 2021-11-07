package com.example.shoplist;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class TimePickerDialogue extends AppCompatDialogFragment {

    private NumberPicker npHour;
    private NumberPicker npMinute;
    private NumberPicker npSecond;
    private int hours;
    private int minutes;
    private int seconds;
    private String listTitle;

    private TimePickerDialogueListener listener;

    public interface TimePickerDialogueListener{
        void onTimePick(int h, int m, int s, String title);
    }

    public TimePickerDialogue(String title)
    {
        listTitle = title;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (TimePickerDialogueListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement TimePickerDialogueListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_pick_time, null);

        builder.setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Set time", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hours = npHour.getValue();
                        minutes = npMinute.getValue();
                        seconds = npSecond.getValue();
                        listener.onTimePick(hours, minutes, seconds, listTitle);
                    }
                });

        npHour = view.findViewById(R.id.npHours);
        npHour.setMaxValue(23);
        npMinute = view.findViewById(R.id.npMinutes);
        npMinute.setMaxValue(59);
        npSecond = view.findViewById(R.id.npSeconds);
        npSecond.setMaxValue(59);

        return builder.create();
    }


}
