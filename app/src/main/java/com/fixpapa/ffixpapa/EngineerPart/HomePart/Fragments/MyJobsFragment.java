package com.fixpapa.ffixpapa.EngineerPart.HomePart.Fragments;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fixpapa.ffixpapa.EngineerPart.HomePart.Fragments.MyJobFragments.ThisWeekFragment;
import com.fixpapa.ffixpapa.EngineerPart.HomePart.Fragments.MyJobFragments.TodayFragement;
import com.fixpapa.ffixpapa.EngineerPart.HomePart.Fragments.MyJobFragments.TomorrowFragment;
import com.fixpapa.ffixpapa.R;

public class MyJobsFragment extends Fragment implements TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener  {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    TextView titleFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_booking, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tabLayout = (TabLayout) v.findViewById(R.id.tabLayoutBooking);
        titleFragment = (TextView) v.findViewById(R.id.titleFragment);

        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) v.findViewById(R.id.pagerBooking);
        PagerAdpterr adapter = new PagerAdpterr(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        titleFragment.setText(getResources().getText(R.string.myjobs_text));
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setOnTabSelectedListener(this);
        tabLayout.setupWithViewPager(viewPager);
        return v;
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class PagerAdpterr extends FragmentStatePagerAdapter

    {
        private String[] tabTitles = new String[]{String.valueOf(getText(R.string.today_text)),
                String.valueOf(getText(R.string.tomorrow_text)), String.valueOf(getText(R.string.all_job_text))};
        int tabCount;

        public PagerAdpterr(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount = tabCount;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            //Returning the current tabs
            switch (position) {
                case 0:
                    TodayFragement tab1 = new TodayFragement();
                    return tab1;
                case 1:
                    TomorrowFragment tab2 = new TomorrowFragment();
                    return tab2;
                case 2:
                    ThisWeekFragment tab3 = new ThisWeekFragment();
                    return tab3;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return tabCount;

        }

    }


}
