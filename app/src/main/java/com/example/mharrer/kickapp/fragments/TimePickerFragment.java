package com.example.mharrer.kickapp.fragments;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public  class TimePickerFragment extends android.support.v4.app.DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private TextView editView;

    private int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    private int min = Calendar.getInstance().get(Calendar.MINUTE);

    public void setView(TextView view) {
        this.editView = view;
        String[] dateComponents = view.getText().toString().split("\\:");
        if (dateComponents.length == 2) {
            min = Integer.parseInt(dateComponents[1]);
            hour = Integer.parseInt(dateComponents[0]);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create a new instance of DatePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, min, true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        editView.setText(hourOfDay+":"+ (minute<10 ? "0"+minute : minute )+ " Uhr");
    }
}