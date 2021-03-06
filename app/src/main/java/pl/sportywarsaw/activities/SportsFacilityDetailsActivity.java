package pl.sportywarsaw.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.inject.Inject;

import pl.sportywarsaw.MyApplication;
import pl.sportywarsaw.R;
import pl.sportywarsaw.infrastructure.CustomCallback;
import pl.sportywarsaw.models.PositionModel;
import pl.sportywarsaw.models.SportFacilityPlusModel;
import pl.sportywarsaw.services.SportsFacilitiesService;
import pl.sportywarsaw.utils.MapUtils;
import retrofit.Call;

public class SportsFacilityDetailsActivity extends AppCompatActivity {

    private int sportsFacilityId;

    @Inject
    SportsFacilitiesService service;
    private ProgressBar progressBar;
    private LinearLayout contentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports_facility_details);
        ((MyApplication) getApplication()).getServicesComponent().inject(this);
        contentLayout = (LinearLayout) findViewById(R.id.content);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        sportsFacilityId = getIntent().getIntExtra("sportsFacilityId", -1);

        contentLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
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
                loadEverything(model);
                setUpEvents(model);
                contentLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setUpEvents(final SportFacilityPlusModel model) {
        ImageView phoneImageView = (ImageView) findViewById(R.id.phone_image);
        phoneImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + model.getPhoneNumber()));
                dialIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(dialIntent);
            }
        });

        ImageView mapImageView = (ImageView) findViewById(R.id.map_image);
        mapImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapUtils.showMap(model, SportsFacilityDetailsActivity.this);
            }
        });

        ImageView websiteImageView = (ImageView) findViewById(R.id.website_image);
        websiteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = model.getWebsite();
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "http://" + url;
                }
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });
    }

    private void loadEverything(SportFacilityPlusModel model) {
        TextView descriptionView = (TextView) findViewById(R.id.description);
        descriptionView.setText(model.getDescription());
        TextView phoneNumberView = (TextView) findViewById(R.id.phone_number);
        phoneNumberView.setText(model.getPhoneNumber());
        TextView websiteView = (TextView) findViewById(R.id.website);
        websiteView.setText(model.getWebsite());
        TextView addressView = (TextView) findViewById(R.id.address);
        addressView.setText(getAddressString(model));
        TextView typeView = (TextView) findViewById(R.id.type);
        typeView.setText(model.getType().toString());
    }

    @NonNull
    private String getAddressString(SportFacilityPlusModel model) {
        return model.getStreet() + " " + model.getNumber() + ", " + model.getDistrict();
    }
}
