package pl.sportywarsaw.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import pl.sportywarsaw.MyApplication;
import pl.sportywarsaw.R;
import pl.sportywarsaw.adapters.SearchFriendsAdapter;
import pl.sportywarsaw.services.UserService;
import pl.sportywarsaw.utils.DelayAutoCompleteTextView;

import javax.inject.Inject;

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
