package pl.sportywarsaw.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.sportywarsaw.R;
import pl.sportywarsaw.activities.AddMeetingActivity;

/**
 * Base fragment for showing lists of meetings
 *
 * @author Marcin Chudy
 */
public class MeetingsFragment extends Fragment {

    public MeetingsFragment() {}

    public static MeetingsFragment newInstance() {
        return new MeetingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meetings, container, false);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.meetings_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddMeetingActivity.class);
                startActivity(intent);
            }
        });

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.meetings_tab_layout);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.meetings_view_pager);
        MeetingsPagerAdapter pagerAdapter = new MeetingsPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    private class MeetingsPagerAdapter extends FragmentStatePagerAdapter {
        private static final int TABS_COUNT = 2;

        public MeetingsPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case MeetingsTabFragment.MY_MEETINGS:
                    return MeetingsTabFragment.newInstance(MeetingsTabFragment.MY_MEETINGS);
                default:
                    return MeetingsTabFragment.newInstance(MeetingsTabFragment.OTHER_MEETINGS);
            }
        }

        @Override
        public int getCount() {
            return TABS_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case MeetingsTabFragment.MY_MEETINGS:
                    return getActivity().getString(R.string.my_meetings);
                case MeetingsTabFragment.OTHER_MEETINGS:
                    return getActivity().getString(R.string.other_meetings);
            }
            return null;
        }
    }
}


