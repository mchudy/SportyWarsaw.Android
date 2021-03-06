package pl.sportywarsaw.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import javax.inject.Inject;

import pl.sportywarsaw.MyApplication;
import pl.sportywarsaw.R;
import pl.sportywarsaw.fragments.MeetingDetailsFragment;
import pl.sportywarsaw.fragments.MeetingParticipantsFragment;
import pl.sportywarsaw.infrastructure.CustomCallback;
import pl.sportywarsaw.models.MeetingPlusModel;
import pl.sportywarsaw.services.MeetingsService;
import retrofit.Call;

public class MeetingDetailsActivity extends AppCompatActivity {
    private int meetingId;
    private MeetingPlusModel model;

    @Inject
    MeetingsService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_details);
        ((MyApplication) getApplication()).getServicesComponent().inject(this);

        meetingId = getIntent().getIntExtra("meetingId", -1);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.meeting_details_tab_layout);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.meeting_details_view_pager);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        progressBar.setVisibility(View.VISIBLE);

        Call<MeetingPlusModel> call = service.getMeetingDetails(meetingId);
        call.enqueue(new CustomCallback<MeetingPlusModel>(this) {
            @Override
            public void onSuccess(MeetingPlusModel model) {
                MeetingDetailsActivity.this.model = model;
                MeetingDetailsPagerAdapter pagerAdapter = new MeetingDetailsPagerAdapter(getSupportFragmentManager(), model);
                viewPager.setAdapter(pagerAdapter);
                tabLayout.setupWithViewPager(viewPager);
                setTitle(model.getTitle());
            }
            @Override
            public void always() {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private class MeetingDetailsPagerAdapter extends FragmentStatePagerAdapter {
        private static final int TABS_COUNT = 2;
        private static final int DETAILS_TAB = 0;
        private static final int PARTICIPANTS_TAB = 1;

        private MeetingPlusModel model;

        public MeetingDetailsPagerAdapter(FragmentManager fragmentManager, MeetingPlusModel model) {
            super(fragmentManager);
            this.model = model;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case DETAILS_TAB:
                    return MeetingDetailsFragment.newInstance(model);
                case PARTICIPANTS_TAB:
                default:
                    return MeetingParticipantsFragment.newInstance(model);
            }
        }

        @Override
        public int getCount() {
            return TABS_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case DETAILS_TAB:
                    return getString(R.string.details);
                case PARTICIPANTS_TAB:
                    return getString(R.string.participants);
            }
            return null;
        }
    }
}
