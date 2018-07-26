package com.baumgartner.wy.pt_locationawareness.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baumgartner.wy.pt_locationawareness.R;

public class HourlyDetailsFragment extends android.support.v4.app.Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.hourly_details_fragment, container, false);
    }
}
