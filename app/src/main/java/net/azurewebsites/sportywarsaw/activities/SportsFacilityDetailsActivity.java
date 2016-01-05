package net.azurewebsites.sportywarsaw.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import net.azurewebsites.sportywarsaw.MyApplication;
import net.azurewebsites.sportywarsaw.R;
import net.azurewebsites.sportywarsaw.infrastructure.CustomCallback;
import net.azurewebsites.sportywarsaw.models.SportFacilityPlusModel;
import net.azurewebsites.sportywarsaw.services.SportsFacilitiesService;

import javax.inject.Inject;

import retrofit.Call;

public class SportsFacilityDetailsActivity extends AppCompatActivity {

    private int sportsFacilityId;

    @Inject SportsFacilitiesService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports_facility_details);
        ((MyApplication) getApplication()).getServicesComponent().inject(this);

        sportsFacilityId = getIntent().getIntExtra("sportsFacilityId", -1);
        loadModel();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadModel() {
        Call<SportFacilityPlusModel> call = service.getDetails(sportsFacilityId);
        call.enqueue(new CustomCallback<SportFacilityPlusModel>(this) {
            @Override
            public void onSuccess(SportFacilityPlusModel model) {
                Toast.makeText(SportsFacilityDetailsActivity.this, model.getWebsite(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
