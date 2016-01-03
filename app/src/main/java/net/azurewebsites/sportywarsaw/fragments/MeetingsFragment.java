package net.azurewebsites.sportywarsaw.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.azurewebsites.sportywarsaw.R;

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

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.meetings_tab_layout);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.meetings_view_pager);
        MeetingsPagerAdapter pagerAdapter = new MeetingsPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    //TODO: show different types of meetings
    private class MeetingsPagerAdapter extends FragmentStatePagerAdapter {
        private static final int TABS_COUNT = 2;

        public static final int TAB_MY_MEETINGS = 0;
        public static final int TAB_ALL_MEETINGS = 1;

        public MeetingsPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case TAB_MY_MEETINGS:
                    return MeetingsTabFragment.newInstance();
                default:
                    return MeetingsTabFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return TABS_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case TAB_MY_MEETINGS:
                    return "My meetings";
                case TAB_ALL_MEETINGS:
                    return "All meetings";
            }
            return null;
        }
    }
}


