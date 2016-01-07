package net.azurewebsites.sportywarsaw.activities;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import net.azurewebsites.sportywarsaw.MyApplication;
import net.azurewebsites.sportywarsaw.R;
import net.azurewebsites.sportywarsaw.adapters.FriendsRecyclerViewAdapter;
import net.azurewebsites.sportywarsaw.adapters.SearchFacilityArrayAdapter;
import net.azurewebsites.sportywarsaw.adapters.SearchFriendsAdapter;
import net.azurewebsites.sportywarsaw.enums.SportType;
import net.azurewebsites.sportywarsaw.infrastructure.CustomCallback;
import net.azurewebsites.sportywarsaw.models.AddMeetingModel;
import net.azurewebsites.sportywarsaw.models.FriendshipModel;
import net.azurewebsites.sportywarsaw.models.UserModel;
import net.azurewebsites.sportywarsaw.services.MeetingsService;
import net.azurewebsites.sportywarsaw.services.SportsFacilitiesService;
import net.azurewebsites.sportywarsaw.services.UserService;
import net.azurewebsites.sportywarsaw.utils.DelayAutoCompleteTextView;
import net.azurewebsites.sportywarsaw.utils.EditTextDatePicker;
import net.azurewebsites.sportywarsaw.utils.EditTextTimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import retrofit.Call;

public class SearchFriendsActivity extends AppCompatActivity {

    //@Inject
    //Friend service;

    @Inject
    UserService service;
    //private EditText endDateView;
    //private EditText startDateView;
    //private EditText startTimeView;
    //private EditText endTimeView;
    //private Spinner typeSpinner;
    private SearchFriendsAdapter adapter;
    private DelayAutoCompleteTextView friendView;

    private String selectedUserName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friends);
        ((MyApplication) getApplication()).getServicesComponent().inject(this);
        Button addFriendButton = (Button) findViewById(R.id.add_friend_button_good);
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptAddFriend();
            }
        });

        //typeSpinner = (Spinner) findViewById(R.id.type_spinner);
        //typeSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, SportType.values()));

        setupAutoCompleteFacilities();
       // setupDateTimeListeners();
    }

    private void setupAutoCompleteFacilities() {
        friendView = (DelayAutoCompleteTextView) findViewById(R.id.friend_auto_complete);
        friendView.setThreshold(0);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar_friend_autocomplete);
        friendView.setProgressBar(progressBar);
        adapter = new SearchFriendsAdapter(this, R.layout.fragment_friend_item, service);
        friendView.setAdapter(adapter);
        friendView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedUserName = adapter.getItem(position).getUsername();
            }
        });
    }



    private void attemptAddFriend() {
        boolean valid = true;
        TextInputLayout layout = (TextInputLayout) findViewById(R.id.title_layout);
        if(selectedUserName!="") {
            layout.setError(getString(R.string.error_field_required));
            valid = false;
        } else {
            layout.setError(null);
        }
        if(!valid) return;

        // dodaj przyjaciela
        addMeeting(selectedUserName);
    }

   // private boolean validateRequired(boolean valid, String startDateString, TextInputLayout layout) {
        //if(startDateString.isEmpty()) {
         //   layout.setError(getString(R.string.error_field_required));
          //  valid = false;
       // } else {
           // layout.setError(null);
        //}
      //  return valid;
    //}


    private void addMeeting(String userName) {
        Call<ResponseBody> call = service.sendFriendRequest(userName);
        call.enqueue(new CustomCallback<ResponseBody>(SearchFriendsActivity.this) {
            @Override
            public void onSuccess(ResponseBody model) {
                Toast.makeText(SearchFriendsActivity.this, getString(R.string.message_meeting_added),
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }



}
