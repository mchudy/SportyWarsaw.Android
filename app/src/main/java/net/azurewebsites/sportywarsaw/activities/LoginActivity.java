package net.azurewebsites.sportywarsaw.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import net.azurewebsites.sportywarsaw.MyApplication;
import net.azurewebsites.sportywarsaw.R;
import net.azurewebsites.sportywarsaw.infrastructure.CustomCallback;
import net.azurewebsites.sportywarsaw.models.AccessTokenModel;
import net.azurewebsites.sportywarsaw.services.AccountService;

import org.w3c.dom.Text;

import javax.inject.Inject;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A login screen that offers login via username and password.
 *
 * @author Marcin Chudy
 */
public class LoginActivity extends AppCompatActivity  {

    private static final String GRANT_TYPE = "password";
    private static final String ACCESS_TOKEN_KEY = "accessToken";

    private EditText usernameView;
    private EditText passwordView;

    @Inject AccountService service;
    @Inject SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ((MyApplication) getApplication()).getServicesComponent().inject(this);
        preferences.edit().clear().apply();
        if(!TextUtils.isEmpty(preferences.getString(ACCESS_TOKEN_KEY, null))){
            showMainActivity();
        }

        usernameView = (EditText) findViewById(R.id.username);
        passwordView = (EditText) findViewById(R.id.password);

        Button signInButton = (Button) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void attemptLogin() {
        usernameView.setError(null);
        passwordView.setError(null);

        String username = usernameView.getText().toString();
        String password = passwordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            passwordView.setError(getString(R.string.error_field_required));
            focusView = passwordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(username)) {
            usernameView.setError(getString(R.string.error_field_required));
            focusView = usernameView;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            getToken(username, password);
        }
    }

    private void getToken(String username, String password) {
        Call<AccessTokenModel> call = service.getToken(username, password, GRANT_TYPE);
        call.enqueue(new CustomCallback<AccessTokenModel>(this) {
            @Override
            public void onSuccess(AccessTokenModel model) {
                String token = model.getAccessToken();
                if(!TextUtils.isEmpty(token)) {
                    SharedPreferences.Editor edit = preferences.edit();
                    edit.putString(ACCESS_TOKEN_KEY, token);
                    edit.commit();
                    showMainActivity();
                }
            }

            @Override
            public void onResponse(Response<AccessTokenModel> response, Retrofit retrofit) {
                if(!response.isSuccess() && response.code() == 400) {
                    showIncorrectCredentialsDialog();
                } else{
                    super.onResponse(response, retrofit);
                }
            }
        });
    }

    private void showIncorrectCredentialsDialog() {
        new AlertDialog.Builder(LoginActivity.this)
            .setTitle(R.string.incorrect_credentials)
            .setMessage(R.string.incorrect_credentials_message)
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            })
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();
    }

    private void showMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }
}

