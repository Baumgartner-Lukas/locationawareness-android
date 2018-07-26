package com.baumgartner.wy.pt_locationawareness.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.baumgartner.wy.pt_locationawareness.R;

public class ViewPagerFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager, container, false);

        final DailyDetailsFragment dailyDetailsFragment = new DailyDetailsFragment();
        final HourlyDetailsFragment hourlyDetailsFragment = new HourlyDetailsFragment();

        ViewPager viewPager = view.findViewById(R.id.viewpager);
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                return position == 0 ?  dailyDetailsFragment : hourlyDetailsFragment;
            }

            @Override
            public CharSequence getPageTitle(int position){
                return position == 0 ?  getString(R.string.daily_pager_caption) : getString(R.string.hourly_pager_caption) ;
            }

            @Override
            public int getCount() {
                return 2;
            }

        });
     TabLayout tabLayout = view.findViewById(R.id.tabLayout);
     tabLayout.setupWithViewPager(viewPager);


        return view;
    }
}
