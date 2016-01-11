package net.azurewebsites.sportywarsaw.activities;

import android.content.Intent;
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

    @Inject
    UserService service;
    private SearchFriendsAdapter adapter;
    private DelayAutoCompleteTextView friendView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friends);
        ((MyApplication) getApplication()).getServicesComponent().inject(this);
        setupAutoCompleteFacilities();
    }

    private void setupAutoCompleteFacilities() {
        friendView = (DelayAutoCompleteTextView) findViewById(R.id.friend_auto_complete);
        friendView.setThreshold(0);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar_friend_autocomplete);
        friendView.setProgressBar(progressBar);
        // ma byc fragment_user_item
        adapter = new SearchFriendsAdapter(this, R.layout.fragment_user_item, service);
        friendView.setAdapter(adapter);
        friendView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedUserName = adapter.getItem(position).getUsername();
                Intent intent = new Intent(SearchFriendsActivity.this, UserProfileActivity. class);
                intent.putExtra("username", selectedUserName);
                startActivity(intent);
            }
        });
    }

}
