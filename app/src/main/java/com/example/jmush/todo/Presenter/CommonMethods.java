package com.example.jmush.todo.Presenter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.jmush.todo.activity.MainActivity;

import java.util.Calendar;

/**
 * Created by jmush on 9/24/17.
 */

public class CommonMethods {
    Context context;

    /**
     * This method gives DatePicker
     * @param tododate Set selected date to EditText Field
     * @param context
     */


    public void datePicker(final EditText tododate, Context context){

        this.context=context;
        // Creating calender instance

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        // getting datePicker and selecting the date
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        tododate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);


                    }
                }, mYear, mMonth, mDay);
        // Pop up DatePicker Dialog
        datePickerDialog.show();
    }
}
