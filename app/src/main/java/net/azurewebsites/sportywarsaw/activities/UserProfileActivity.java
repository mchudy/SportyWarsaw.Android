package net.azurewebsites.sportywarsaw.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.ResponseBody;

import net.azurewebsites.sportywarsaw.MyApplication;
import net.azurewebsites.sportywarsaw.R;
import net.azurewebsites.sportywarsaw.infrastructure.CustomCallback;
import net.azurewebsites.sportywarsaw.models.FriendshipModel;
import net.azurewebsites.sportywarsaw.models.UserPlusModel;
import net.azurewebsites.sportywarsaw.services.UserService;

import javax.inject.Inject;

import retrofit.Call;

public class UserProfileActivity extends AppCompatActivity {
    private String username;
    private String loggedUsername;
    private UserPlusModel model;
    @Inject UserService service;
    @Inject SharedPreferences preferences;
    private Button sendRequestButton;
    private Button acceptRequestButton;
    private Button rejectRequestButton;
    private Button removeFriendButton;
    private TextView friendsView;
    private ProgressBar progressBar;
    private LinearLayout content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ((MyApplication) getApplication()).getServicesComponent().inject(this);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        content = (LinearLayout) findViewById(R.id.content);
        progressBar.setVisibility(View.VISIBLE);
        content.setVisibility(View.GONE);

        username = getIntent().getStringExtra("username");
        loggedUsername = preferences.getString("username", "");

        friendsView = (TextView) findViewById(R.id.friends_message);
        sendRequestButton = (Button) findViewById(R.id.send_request_button);
        acceptRequestButton = (Button) findViewById(R.id.accept_request_button);
        rejectRequestButton = (Button) findViewById(R.id.reject_request_button);
        removeFriendButton = (Button) findViewById(R.id.remove_friend_button);

        sendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
        acceptRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptRequest();
            }
        });
        rejectRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectRequest();
            }
        });
        removeFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFriend();
            }
        });

        loadData();
    }

    private void sendRequest() {
        Call<ResponseBody> call = service.sendFriendRequest(model.getUsername());
        call.enqueue(new CustomCallback<ResponseBody>(this) {
            @Override
            public void onSuccess(ResponseBody model) {
                sendRequestButton.setVisibility(View.GONE);
                friendsView.setText(R.string.sent_request);
                friendsView.setVisibility(View.VISIBLE);
                Toast.makeText(UserProfileActivity.this, "Request has been sent", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void rejectRequest() {
        Call<ResponseBody> call = service.removeFriend(model.getUsername());
        call.enqueue(new CustomCallback<ResponseBody>(this) {
            @Override
            public void onSuccess(ResponseBody model) {
                sendRequestButton.setVisibility(View.VISIBLE);
                rejectRequestButton.setVisibility(View.GONE);
                Toast.makeText(UserProfileActivity.this, "Request has been rejected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeFriend() {
        Call<ResponseBody> call = service.removeFriend(model.getUsername());
        call.enqueue(new CustomCallback<ResponseBody>(this) {
            @Override
            public void onSuccess(ResponseBody model) {
                sendRequestButton.setVisibility(View.VISIBLE);
                removeFriendButton.setVisibility(View.GONE);
                friendsView.setVisibility(View.GONE);
                Toast.makeText(UserProfileActivity.this, "Removed friend", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void acceptRequest() {
        Call<ResponseBody> call = service.acceptFriendRequest(model.getUsername());
        call.enqueue(new CustomCallback<ResponseBody>(this) {
            @Override
            public void onSuccess(ResponseBody model) {
                friendsView.setVisibility(View.VISIBLE);
                friendsView.setText(getString(R.string.friends));
                acceptRequestButton.setVisibility(View.GONE);
                rejectRequestButton.setVisibility(View.GONE);
                removeFriendButton.setVisibility(View.VISIBLE);
                Toast.makeText(UserProfileActivity.this, "Accepted request", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadData() {
        Call<UserPlusModel> call = service.getDetails(username);
        call.enqueue(new CustomCallback<UserPlusModel>(this) {
            @Override
            public void onSuccess(UserPlusModel model) {
                UserProfileActivity.this.model = model;
                checkFriendshipStatus();
                TextView firstNameView = (TextView) findViewById(R.id.first_name);
                firstNameView.setText(model.getFirstName());

                TextView lastNameView = (TextView) findViewById(R.id.last_name);
                lastNameView.setText(model.getLastName());

                TextView usernameView = (TextView) findViewById(R.id.username);
                usernameView.setText(model.getUsername());
            }
            @Override
            public void always() {
                progressBar.setVisibility(View.GONE);
                content.setVisibility(View.VISIBLE);
            }
        });
    }

    private void checkFriendshipStatus() {
        if(model.getUsername().equals(loggedUsername)) return;
        for (FriendshipModel friendship : model.getFriendshipsInitiated()) {
            if(friendship.getFriendUsername().equals(loggedUsername)) {
                if(friendship.isConfirmed()) {
                    friendsView.setVisibility(View.VISIBLE);
                    removeFriendButton.setVisibility(View.VISIBLE);
                } else {
                    acceptRequestButton.setVisibility(View.VISIBLE);
                    rejectRequestButton.setVisibility(View.VISIBLE);
                }
                return;
            }
        }
        for (FriendshipModel friendship : model.getFriendshipsRequested()) {
            if(friendship.getInviterUsername().equals(loggedUsername)) {
                if(friendship.isConfirmed()) {
                    friendsView.setVisibility(View.VISIBLE);
                    removeFriendButton.setVisibility(View.VISIBLE);
                } else {
                    friendsView.setVisibility(View.VISIBLE);
                    friendsView.setText("Friend request sent");
                }
                return;
            }
        }
        sendRequestButton.setVisibility(View.VISIBLE);
    }

}
