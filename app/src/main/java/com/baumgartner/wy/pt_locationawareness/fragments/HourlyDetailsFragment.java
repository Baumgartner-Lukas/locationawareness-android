package com.baumgartner.wy.pt_locationawareness.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baumgartner.wy.pt_locationawareness.MainActivity;
import com.baumgartner.wy.pt_locationawareness.R;
import com.baumgartner.wy.pt_locationawareness.adapter.HourAdapter;
import com.baumgartner.wy.pt_locationawareness.weather.Hour;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HourlyDetailsFragment extends android.support.v4.app.Fragment {
    private Hour[] mHours;

    @BindView(R.id.hourRecyclerView)
    RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hourly_details_fragment, container, false);
        ButterKnife.bind(this, view);

        MainActivity activity = (MainActivity) getActivity();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mHours = activity.getHours();

        HourAdapter adapter = new HourAdapter(getActivity(), mHours);
        mRecyclerView.setAdapter(adapter);
        return view;
    }
}
