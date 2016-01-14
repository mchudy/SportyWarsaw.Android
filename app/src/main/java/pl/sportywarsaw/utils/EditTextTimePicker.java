package pl.sportywarsaw.utils;

import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

public class EditTextTimePicker implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {
    private EditText editText;
    private int hourOfDay;
    private int minutes;
    private Context context;

    public EditTextTimePicker(Context context, EditText editText)
    {
        this.editText = editText;
        this.editText.setOnClickListener(this);
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog dialog =  new TimePickerDialog(context, this, calendar.get(calendar.HOUR_OF_DAY),
                calendar.get(calendar.MINUTE), true);
        dialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.hourOfDay = hourOfDay;
        this.minutes = minute;
        updateDisplay();
    }

    private void updateDisplay() {
        editText.setText(new StringBuilder()
                .append(hourOfDay).append(":").append(minutes));
    }
}