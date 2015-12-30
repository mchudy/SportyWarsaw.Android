package net.azurewebsites.sportywarsaw.activities;

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
import android.widget.Toast;

import net.azurewebsites.sportywarsaw.MyApplication;
import net.azurewebsites.sportywarsaw.R;
import net.azurewebsites.sportywarsaw.models.AccessTokenModel;
import net.azurewebsites.sportywarsaw.services.AccountService;

import javax.inject.Inject;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {

    private static String grantType = "password";

    private EditText usernameView;
    private EditText passwordView;
    private View progressView;
    private View loginFormView;

    @Inject AccountService service;
    @Inject SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ((MyApplication) getApplication()).getServicesComponent().inject(this);

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
                //TODO show RegisterActivity
            }
        });

        loginFormView = findViewById(R.id.login_form);
        progressView = findViewById(R.id.login_progress);
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
            //showProgress(true);
            getToken(username, password);
        }
    }

    private void getToken(String username, String password) {
        Call<AccessTokenModel> call = service.getToken(username, password, grantType);
        call.enqueue(new Callback<AccessTokenModel>() {
            @Override
            public void onResponse(Response<AccessTokenModel> response, Retrofit retrofit) {
                boolean success = false;
                if (response.isSuccess()) {
                    AccessTokenModel result = response.body();
                    String token = result.getAccessToken();
                    if(!TextUtils.isEmpty(token)) {
                        success = true;
                        SharedPreferences.Editor edit = preferences.edit();
                        edit.putString("accessToken", token);
                        edit.commit();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
                if(!success) {
                    Toast.makeText(LoginActivity.this, R.string.incorrect_username_or_password, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Throwable t) {
                //TODO
            }
        });
    }
}

