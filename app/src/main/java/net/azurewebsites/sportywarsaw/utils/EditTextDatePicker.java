package net.azurewebsites.sportywarsaw.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;

public class EditTextDatePicker implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    private EditText editText;
    private int day;
    private int month;
    private int year;
    private Context context;

    public EditTextDatePicker(Context context, EditText editText)
    {
        this.editText = editText;
        this.editText.setOnClickListener(this);
        this.context = context;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        month = monthOfYear;
        day = dayOfMonth;
        updateDisplay();
    }

    @Override
    public void onClick(View v) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog =  new DatePickerDialog(context, this, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void updateDisplay() {
        editText.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(day).append("/").append(month + 1).append("/").append(year).append(" "));
    }
}