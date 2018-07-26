package com.baumgartner.wy.pt_locationawareness.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baumgartner.wy.pt_locationawareness.R;
import com.baumgartner.wy.pt_locationawareness.weather.Day;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {
    private Day[] mDays;
    private Context mContext;

    public DayAdapter(Context context, Day[] days) {
        this.mContext = context;
        this.mDays = days;
    }

    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weather_list_item, parent, false);
        return new DayViewHolder(view);
    }


    @Override
    public void onBindViewHolder(DayViewHolder holder, int position) {
        holder.bindDay(mDays[position]);
    }

    @Override
    public int getItemCount() {
        return mDays.length;
    }

    class DayViewHolder extends RecyclerView.ViewHolder {
//        TextView mDay;
//        TextView mTemperature;
//        ImageView mIconImageView;

        TextView mTimeLabel;
        TextView mSummary;
        TextView mTemperature;
        ImageView mIconImageView;

        DayViewHolder(View itemView) {
            super(itemView);

            mTimeLabel = itemView.findViewById(R.id.listTimeLabel);
            //mSummary = itemView.findViewById(R.id.listSummaryLabel);
            mTemperature = itemView.findViewById(R.id.listTemperatureLabel);
            mIconImageView = itemView.findViewById(R.id.listIconImageView);

//            mDay = itemView.findViewById(R.id.dayLabel);
//            mTemperature = itemView.findViewById(R.id.dayTemperatureLabel);
//            mIconImageView = itemView.findViewById(R.id.dayIconImageView);
        }

        void bindDay(Day day){
            mTimeLabel.setText(day.getDayOfTheWeek());
           //mSummary.setText(day.getSummary());
            mTemperature.setText(String.format("%d°C", day.getTemperatureMax()));
            mIconImageView.setImageResource(day.getIconId());
//            mDay.setText(day.getDayOfTheWeek());
//            mTemperature.setText(day.getTemperatureMax() + "");
//            mIconImageView.setImageResource(day.getIconId());
        }
    }
}
