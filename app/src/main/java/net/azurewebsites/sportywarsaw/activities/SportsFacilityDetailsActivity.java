package net.azurewebsites.sportywarsaw.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
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
                // przepisanie wartosci do danych elementow
                loadEverything(model);
                Toast.makeText(SportsFacilityDetailsActivity.this, model.getWebsite(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadEverything(SportFacilityPlusModel model) {
        // załadowuje dane do odpowiednich textview
        TextView descriptionview = (TextView)findViewById(R.id.description);
        descriptionview.setText(model.getDescription());
        TextView phonenumberview = (TextView)findViewById(R.id.phoneNumber);
        phonenumberview.setText(model.getPhoneNumber());
        TextView idview = (TextView)findViewById(R.id.id);
        idview.setText(String.valueOf(model.getId()));
        TextView numberview = (TextView)findViewById(R.id.number);
        numberview.setText(model.getNumber());
        TextView districtview = (TextView)findViewById(R.id.district);
        districtview.setText(model.getDistrict());
        TextView websiteview = (TextView)findViewById(R.id.website);
        websiteview.setText(model.getWebsite());
        TextView streetview = (TextView)findViewById(R.id.street);
        streetview.setText(model.getStreet());

    }
}
