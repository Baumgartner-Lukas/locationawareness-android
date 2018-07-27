package com.baumgartner.wy.pt_locationawareness.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baumgartner.wy.pt_locationawareness.R;
import com.baumgartner.wy.pt_locationawareness.weather.Hour;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HourAdapter extends RecyclerView.Adapter<HourAdapter.HourViewHolder> {
    private Hour[] mHours;
    private Context mContext;


    public HourAdapter(Context context, Hour[] hours) {
        mHours = hours;
        mContext = context;
    }


    @NonNull
    @Override
    public HourViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hourly_list_item, parent, false);
        return new HourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HourViewHolder holder, int position) {
        holder.bindHour(mHours[position]);
    }

    @Override
    public int getItemCount() {
        return mHours.length;
    }

    class HourViewHolder extends RecyclerView.ViewHolder{

        TextView mTimeLabel;
        ImageView mIconImageView;
        TextView mSummaryLabel;
        TextView mTemperaturLabel;

        public HourViewHolder(View itemView) {
            super(itemView);

            mTimeLabel = itemView.findViewById(R.id.hourlyTimeLabel);
            mIconImageView = itemView.findViewById(R.id.hourlyIconImageView);
            mSummaryLabel = itemView.findViewById(R.id.hourlySummaryLabel);
            mTemperaturLabel = itemView.findViewById(R.id.hourlyTemperatureLabel);
        }

        void bindHour(Hour hour){
            mTimeLabel.setText(hour.getHour());
            mIconImageView.setImageResource(hour.getIconId());
            mSummaryLabel.setText(hour.getSummary());
            mTemperaturLabel.setText(String.format("%d°C",hour.getTemperature()));

        }
    }
}
