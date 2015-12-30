package net.azurewebsites.sportywarsaw.infrastructure;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import net.azurewebsites.sportywarsaw.R;
import net.azurewebsites.sportywarsaw.activities.LoginActivity;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Custom callback class enabling general error handling
 * It shows alert dialogs to the user according to received errors.
 *
 * @param <T> Type of the response model
 * @author Marcin Chudy
 */
public abstract class CustomCallback<T> implements Callback<T> {

    private Context context;

    public CustomCallback(Context context) {
        this.context = context;
    }

    public abstract void onSuccess(T model);

    @Override
    public void onResponse(Response<T> response, Retrofit retrofit) {
        if(response.isSuccess()){
            T body = response.body();
            onSuccess(body);
        } else {
            switch (response.code()){
                case 401:
                    handle401();
                    break;
                default:
                    showAlertDialog(context.getString(R.string.error), context.getString(R.string.message_error_occurred));
            }
        }
    }

    @Override
    public void onFailure(Throwable t) {
        showAlertDialog(context.getString(R.string.connection_error_title),
                context.getString(R.string.connection_error_message));
    }

    private void showAlertDialog(String title, String message) {
        new AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            })
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();
    }

    private void handle401() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("accessToken");
        editor.apply();
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
