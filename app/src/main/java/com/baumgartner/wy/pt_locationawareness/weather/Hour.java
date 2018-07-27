package com.baumgartner.wy.pt_locationawareness.weather;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Hour {

    private long mTime;
    private String mSummary;
    private double mTemperature;
    private String mIcon;
    private String mTimezone;

    public Hour(){
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public int getTemperature() {
        double celsius = ((mTemperature - 32) * 0.5555555556);
        return (int) Math.round(celsius);
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public int getIconId(){
        return Forecast.getIconId(mIcon);
    }

    public String getTimezone() {
        return mTimezone;
    }

    public void setTimezone(String timezone) {
        mTimezone = timezone;
    }

    public String getHour(){
        SimpleDateFormat formatter = new SimpleDateFormat("kk:mm");
        Date date = new Date(mTime * 1000);
        return formatter.format(date);
    }
}
