package com.baumgartner.wy.pt_locationawareness.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baumgartner.wy.pt_locationawareness.MainActivity;
import com.baumgartner.wy.pt_locationawareness.R;
import com.baumgartner.wy.pt_locationawareness.adapter.DayAdapter;
import com.baumgartner.wy.pt_locationawareness.weather.Day;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DailyDetailsFragment extends Fragment{

    private Day[] mDays;

    @BindView(R.id.dayRecyclerView)
    RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.daily_details_fragment, container, false);
        ButterKnife.bind(this, view);

        MainActivity activity = (MainActivity) getActivity();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mDays = activity.getDays();

        DayAdapter adapter = new DayAdapter(getActivity(), mDays);

        mRecyclerView.setAdapter(adapter);


        return view;
    }

}
