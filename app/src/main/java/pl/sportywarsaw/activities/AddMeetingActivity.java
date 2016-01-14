package pl.sportywarsaw.activities;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.okhttp.ResponseBody;

import pl.sportywarsaw.MyApplication;
import pl.sportywarsaw.R;
import pl.sportywarsaw.adapters.SearchFacilityArrayAdapter;
import pl.sportywarsaw.enums.SportType;
import pl.sportywarsaw.infrastructure.CustomCallback;
import pl.sportywarsaw.models.AddMeetingModel;
import pl.sportywarsaw.services.MeetingsService;
import pl.sportywarsaw.services.SportsFacilitiesService;
import pl.sportywarsaw.utils.DelayAutoCompleteTextView;
import pl.sportywarsaw.utils.EditTextDatePicker;
import pl.sportywarsaw.utils.EditTextTimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import retrofit.Call;

public class AddMeetingActivity extends AppCompatActivity {

    @Inject
    MeetingsService service;

    @Inject
    SportsFacilitiesService sportsFacilitiesService;
    private EditText endDateView;
    private EditText startDateView;
    private EditText startTimeView;
    private EditText endTimeView;
    private Spinner typeSpinner;
    private SearchFacilityArrayAdapter adapter;
    private DelayAutoCompleteTextView sportsFacilityView;

    private int selectedFacilityId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);
        ((MyApplication) getApplication()).getServicesComponent().inject(this);
        Button addMeetingButton = (Button) findViewById(R.id.add_meeting_button);
        addMeetingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptAddMeeting();
            }
        });

        typeSpinner = (Spinner) findViewById(R.id.type_spinner);
        typeSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, SportType.values()));

        setupAutoCompleteFacilities();
        setupDateTimeListeners();
    }

    private void setupAutoCompleteFacilities() {
        sportsFacilityView = (DelayAutoCompleteTextView) findViewById(R.id.sports_facility_auto_complete);
        sportsFacilityView.setThreshold(0);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar_autocomplete);
        sportsFacilityView.setProgressBar(progressBar);
        adapter = new SearchFacilityArrayAdapter(this, R.layout.fragment_sports_facility_item, sportsFacilitiesService);
        sportsFacilityView.setAdapter(adapter);
        sportsFacilityView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedFacilityId = adapter.getItem(position).getId();
            }
        });
    }

    private void setupDateTimeListeners() {
        endDateView = (EditText) findViewById(R.id.end_date);
        endDateView.setOnClickListener(new EditTextDatePicker(this, endDateView));

        startDateView = (EditText) findViewById(R.id.start_date);
        startDateView.setOnClickListener(new EditTextDatePicker(this, startDateView));

        startTimeView = (EditText) findViewById(R.id.start_time);
        startTimeView.setOnClickListener(new EditTextTimePicker(this, startTimeView));

        endTimeView = (EditText) findViewById(R.id.end_time);
        endTimeView.setOnClickListener(new EditTextTimePicker(this, endTimeView));
    }

    private void attemptAddMeeting() {
        boolean valid = true;

        EditText titleView = (EditText) findViewById(R.id.title);
        String title = titleView.getText().toString();
        String startDateString = startDateView.getText().toString();
        String startTimeString = startTimeView.getText().toString();
        String endDateString = endDateView.getText().toString();
        String endTimeString = endTimeView.getText().toString();

        TextInputLayout layout = (TextInputLayout) findViewById(R.id.title_layout);
        valid = validateRequired(valid, title, layout);
        layout = (TextInputLayout) findViewById(R.id.start_date_layout);
        valid = validateRequired(valid, startDateString, layout);
        layout = (TextInputLayout) findViewById(R.id.start_time_layout);
        valid = validateRequired(valid, startTimeString, layout);
        layout = (TextInputLayout) findViewById(R.id.end_date_layout);
        valid = validateRequired(valid, endDateString, layout);
        layout = (TextInputLayout) findViewById(R.id.end_time_layout);
        valid = validateRequired(valid, endTimeString, layout);

        layout = (TextInputLayout) findViewById(R.id.sports_facility_layout);
        if(selectedFacilityId < 0) {
            layout.setError(getString(R.string.error_field_required));
            valid = false;
        } else {
            layout.setError(null);
        }
        if(!valid) return;

        Date startDate = parseDateTime(startDateString, startTimeString);
        Date endDate = parseDateTime(endDateString, endTimeString);
        if(startDate.after(endDate)) {
            layout.setError(getString(R.string.error_start_date_after_end_date));
            valid = false;
        }

        if(!valid) return;

        AddMeetingModel model = new AddMeetingModel();
        model.setTitle(title);
        model.setStartTime(startDate);
        model.setEndTime(endDate);
        model.setSportType((SportType) typeSpinner.getSelectedItem());

        //TODO: make nullable on the server?
        EditText participantsView = (EditText) findViewById(R.id.maxparticipants);
        int maxParticipants = 1;
        if(!TextUtils.isEmpty(participantsView.getText().toString())) {
            maxParticipants = Integer.parseInt(participantsView.getText().toString());
        }
        model.setMaxParticipants(maxParticipants);

        EditText costView = (EditText) findViewById(R.id.cost);
        double cost = 0.0;
        if(!TextUtils.isEmpty(costView.getText().toString())) {
            cost = Double.parseDouble(costView.getText().toString());
        }
        model.setCost(cost);

        EditText descriptionView = (EditText) findViewById(R.id.description);
        model.setDescription(descriptionView.getText().toString());

        model.setSportsFacilityId(selectedFacilityId);
        addMeeting(model);
    }

    private boolean validateRequired(boolean valid, String startDateString, TextInputLayout layout) {
        if(startDateString.isEmpty()) {
            layout.setError(getString(R.string.error_field_required));
            valid = false;
        } else {
            layout.setError(null);
        }
        return valid;
    }

    private Date parseDateTime(String dateText, String timeText) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        Date date = null;
        String text = dateText + " " + timeText;
        try {
            date = df.parse(text);
        } catch (ParseException e) {
            Log.e("Parse", e.getMessage());
        }
        return date;
    }

    private void addMeeting(AddMeetingModel model) {
        Call<ResponseBody> call = service.post(model);
        call.enqueue(new CustomCallback<ResponseBody>(AddMeetingActivity.this) {
            @Override
            public void onSuccess(ResponseBody model) {
                Toast.makeText(AddMeetingActivity.this, getString(R.string.message_meeting_added),
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
