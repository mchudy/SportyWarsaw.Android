package net.azurewebsites.sportywarsaw.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import net.azurewebsites.sportywarsaw.MyApplication;
import net.azurewebsites.sportywarsaw.R;
import net.azurewebsites.sportywarsaw.models.SportsFacilityModel;
import net.azurewebsites.sportywarsaw.services.SportsFacilitiesService;

import java.io.IOException;

import javax.inject.Inject;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {

    @Inject SportsFacilitiesService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ((MyApplication) getApplication()).getServicesComponent().inject(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<SportsFacilityModel> call = service.getSportsFacility(1);
                call.enqueue(new Callback<SportsFacilityModel>() {
                    @Override
                    public void onResponse(Response<SportsFacilityModel> response, Retrofit retrofit) {
                        if (response.isSuccess()) {
                            SportsFacilityModel result = response.body();
                            Toast.makeText(MainActivity.this, result.getDescription(), Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                String errorMessage = response.errorBody().string();
                                Toast.makeText(MainActivity.this, response.code() + errorMessage, Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
