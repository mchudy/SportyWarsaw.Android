package pl.sportywarsaw.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.ResponseBody;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import pl.sportywarsaw.MyApplication;
import pl.sportywarsaw.R;
import pl.sportywarsaw.infrastructure.CustomCallback;
import pl.sportywarsaw.models.FriendshipModel;
import pl.sportywarsaw.models.UserPlusModel;
import pl.sportywarsaw.services.UserService;
import pl.sportywarsaw.utils.BitmapUtils;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Call;

public class UserProfileActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_GET = 1;
    private static final int MAX_IMAGE_SIZE = 300;

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
    private Button changeImageButton;
    private CircleImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ((MyApplication) getApplication()).getServicesComponent().inject(this);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        content = (LinearLayout) findViewById(R.id.content);
        progressBar.setVisibility(View.VISIBLE);
        content.setVisibility(View.GONE);
        profileImage = (CircleImageView) findViewById(R.id.profile_image);

        username = getIntent().getStringExtra("username");
        loggedUsername = preferences.getString("username", "");

        friendsView = (TextView) findViewById(R.id.friends_message);

        setUpButtons();
        loadData();
    }

    private void setUpButtons() {
        sendRequestButton = (Button) findViewById(R.id.send_request_button);
        acceptRequestButton = (Button) findViewById(R.id.accept_request_button);
        rejectRequestButton = (Button) findViewById(R.id.reject_request_button);
        removeFriendButton = (Button) findViewById(R.id.remove_friend_button);
        changeImageButton = (Button) findViewById(R.id.change_image_button);

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
    }

    private void sendRequest() {
        Call<ResponseBody> call = service.sendFriendRequest(model.getUsername());
        call.enqueue(new CustomCallback<ResponseBody>(this) {
            @Override
            public void onSuccess(ResponseBody model) {
                sendRequestButton.setVisibility(View.GONE);
                friendsView.setText(R.string.sent_request);
                friendsView.setVisibility(View.VISIBLE);
                Toast.makeText(UserProfileActivity.this, R.string.request_sent, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(UserProfileActivity.this, R.string.request_rejected, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(UserProfileActivity.this, R.string.removed_friend, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(UserProfileActivity.this, R.string.accepted_request, Toast.LENGTH_SHORT).show();
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

                Bitmap bitmap = BitmapUtils.decodeBase64(model.getPicture());
                if(bitmap != null) {
                    profileImage.setImageBitmap(bitmap);
                }
            }
            @Override
            public void always() {
                progressBar.setVisibility(View.GONE);
                content.setVisibility(View.VISIBLE);
            }
        });
    }

    private void checkFriendshipStatus() {
        if(model.getUsername().equals(loggedUsername)){
            handleLoggedUser();
            return;
        }
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
                    friendsView.setText(R.string.friend_request_sent);
                }
                return;
            }
        }
        sendRequestButton.setVisibility(View.VISIBLE);
    }

    private void handleLoggedUser() {
        changeImageButton.setVisibility(View.VISIBLE);
        changeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            if(selectedImageUri == null) {
                return;
            }
            Picasso.with(this)
                    .load(selectedImageUri)
                    .resize(MAX_IMAGE_SIZE, MAX_IMAGE_SIZE)
                    .centerInside()
                    .into(profileImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            updateProfile();
                        }
                        @Override
                        public void onError() {
                            Toast.makeText(UserProfileActivity.this, R.string.error_loading_image, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void updateProfile() {
        Bitmap bitmap = ((BitmapDrawable)profileImage.getDrawable()).getBitmap();
        model.setPicture(BitmapUtils.encodeToBase64(bitmap));
        Call<ResponseBody> call = service.put(model);
        call.enqueue(new CustomCallback<ResponseBody>(this) {
            @Override
            public void onSuccess(ResponseBody model) {
                Toast.makeText(UserProfileActivity.this, R.string.message_profile_picture_changed, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
