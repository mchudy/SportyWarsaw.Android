package net.azurewebsites.sportywarsaw.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.squareup.okhttp.ResponseBody;

import net.azurewebsites.sportywarsaw.MyApplication;
import net.azurewebsites.sportywarsaw.R;
import net.azurewebsites.sportywarsaw.infrastructure.CustomCallback;
import net.azurewebsites.sportywarsaw.models.RegisterAccountModel;
import net.azurewebsites.sportywarsaw.services.AccountService;

import java.io.IOException;

import javax.inject.Inject;

import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Activity that offers account registration
 *
 * @author Marcin Chudy
 */
public class RegisterActivity extends AppCompatActivity {

    @Inject
    AccountService service;

    private EditText usernameView;
    private EditText emailView;
    private EditText passwordView;
    private EditText confirmPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ((MyApplication) getApplication()).getServicesComponent().inject(this);

        usernameView = (EditText) findViewById(R.id.username);
        emailView = (EditText) findViewById(R.id.email);
        passwordView = (EditText) findViewById(R.id.password);
        confirmPasswordView = (EditText) findViewById(R.id.confirm_password);

        Button signInButton = (Button) findViewById(R.id.sign_up_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });
    }

    private void attemptRegister() {
        String username = usernameView.getText().toString();
        String password = passwordView.getText().toString();
        String email = emailView.getText().toString();
        String confirmPassword = confirmPasswordView.getText().toString();

        boolean valid = validateInput(username, password, email, confirmPassword);
        if (valid) {
            RegisterAccountModel model = new RegisterAccountModel(username, email, password, confirmPassword);
            Call<ResponseBody> call = service.registerAccount(model);
            call.enqueue(new CustomCallback<ResponseBody>(RegisterActivity.this) {
                @Override
                public void onSuccess(ResponseBody model) {
                    Toast.makeText(RegisterActivity.this, R.string.success_account_registered, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                    if (response.code() == 400) {
                        try {
                            String json = response.errorBody().string();
                            Gson gson = new Gson();
                            ErrorResponse errorResponse = gson.fromJson(json, ErrorResponse.class);

                            new AlertDialog.Builder(RegisterActivity.this)
                                    .setTitle(getString(R.string.error))
                                    .setMessage(errorResponse.getErrorsMessage())
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        } catch (IOException e) {
                            Log.e("Register", e.getMessage());
                        }
                    } else {
                        super.onResponse(response, retrofit);
                    }
                }
            });
        }
    }

    private boolean validateInput(String username, String password, String email, String confirmPassword) {
        boolean valid = true;

        if (username.isEmpty()) {
            usernameView.setError(getString(R.string.error_field_required));
            valid = false;
        } else {
            usernameView.setError(null);
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailView.setError(getString(R.string.error_invalid_email));
            valid = false;
        } else {
            emailView.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
            passwordView.setError(getString(R.string.error_too_short_password));
            valid = false;
        } else {
            usernameView.setError(null);
        }

        if (!confirmPassword.equals(password)) {
            confirmPasswordView.setError(getString(R.string.error_passwords_do_not_match));
            valid = false;
        } else {
            confirmPasswordView.setError(null);
        }

        return valid;
    }

    public class ErrorResponse {
        private String message;
        private ModelState modelState;

        public String getMessage() {
            return message;
        }

        public String getErrorsMessage() {
            return TextUtils.join(" ", modelState.getErrors());
        }

        private class ModelState
        {
            @SerializedName("")
            private String[] errors;

            public String[] getErrors() {
                return errors;
            }
        }
    }
}
