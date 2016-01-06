package net.azurewebsites.sportywarsaw.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Spinner;

import net.azurewebsites.sportywarsaw.R;
import net.azurewebsites.sportywarsaw.adapters.SearchFacilityArrayAdapter;
import net.azurewebsites.sportywarsaw.adapters.SearchFriendsAdapter;
import net.azurewebsites.sportywarsaw.services.MeetingsService;
import net.azurewebsites.sportywarsaw.services.SportsFacilitiesService;
import net.azurewebsites.sportywarsaw.utils.DelayAutoCompleteTextView;

import javax.inject.Inject;

public class SearchFriendsActivity extends AppCompatActivity {

    @Inject
    MeetingsService service;

    @Inject
    SportsFacilitiesService sportsFacilitiesService;
    private EditText stringToSearch;
    private SearchFriendsAdapter adapter;
    private DelayAutoCompleteTextView friendView;

    private int selectedFacilityId = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_search_friends); // powinno sie ustawic
        setContentView(R.layout.fragment_friendsfinder); // powinno sie ustawic

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Search(query);
        }
    }

    private void Search(String query)
    {
        // // TODO: searching query 
    }
}
