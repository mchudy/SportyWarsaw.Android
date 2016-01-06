package net.azurewebsites.sportywarsaw.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ProgressBar;

import net.azurewebsites.sportywarsaw.R;
import net.azurewebsites.sportywarsaw.adapters.FriendsRecyclerViewAdapter;
import net.azurewebsites.sportywarsaw.models.UserModel;
import net.azurewebsites.sportywarsaw.services.UserService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SearchFriendsActivity extends AppCompatActivity {

    private FriendsRecyclerViewAdapter adapter;
    private List<UserModel> items = new ArrayList<>();
    private ProgressBar progressBar;

    @Inject
    UserService service;
    private RecyclerView recyclerView;
    private android.support.v4.app.Fragment fragment;

    private int selectedFacilityId = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_search_friends); // powinno sie ustawic
        setContentView(R.layout.fragment_friendsfinder); // powinno sie ustawic
        // zlapac items

        final EditText editText = (EditText)findViewById(R.id.friend_finder_text);
        adapter = new FriendsRecyclerViewAdapter(items,recyclerView,fragment);
        editText.addTextChangedListener(new TextWatcher() {
        // przy zmianach tekstu bedzie działało
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if(!s.equals("") )
                {
                 // wyswietlam userów po filtrowaniue
                   // editText.setText("hehe"); // zawiesza
                    // // TODO: changeview 
                }
                else
                {
                    /// // TODO: allusers 
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });
       // Intent intent = getIntent();
       // if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
       //     String query = intent.getStringExtra(SearchManager.QUERY);
       //     Search(query);
       // }
    }

    private void Search(String query)
    {
        // // TODO: searching query 
    }



}
