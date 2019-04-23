package com.fixpapa.ffixpapa.VendorPart.HomePart.Fragments;


import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fixpapa.ffixpapa.R;
import com.fixpapa.ffixpapa.VendorPart.HomePart.Fragments.OngoingFragment.VendorCloseFragment;
import com.fixpapa.ffixpapa.VendorPart.HomePart.Fragments.OngoingFragment.VendorOngoingFragment;
import com.fixpapa.ffixpapa.VendorPart.HomePart.Fragments.OngoingFragment.VendorOpenFragment;


public class OngoingJobsFragment extends Fragment implements TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    SwipeRefreshLayout setOnRefreshListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ongoing_jobs, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tabLayout = (TabLayout) v.findViewById(R.id.tabLayoutBooking);

        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) v.findViewById(R.id.pagerBooking);
        PagerAdpter adapter = new PagerAdpter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
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

    public class PagerAdpter extends FragmentStatePagerAdapter

    {
        private String[] tabTitles = new String[]{String.valueOf(getText(R.string.open_text)),
                String.valueOf(getText(R.string.ongoing_text)), String.valueOf(getText(R.string.closed_text))};
        int tabCount;

        public PagerAdpter(FragmentManager fm, int tabCount) {
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
                    VendorOpenFragment tab1 = new VendorOpenFragment();
                    return tab1;
                case 1:
                    VendorOngoingFragment tab2 = new VendorOngoingFragment();
                    return tab2;
                case 2:
                    VendorCloseFragment tab3 = new VendorCloseFragment();
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
