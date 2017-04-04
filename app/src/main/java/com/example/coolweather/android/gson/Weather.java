package com.example.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * author:Mark
 * date:2017/4/4
 * time:14:42
 */

public class Weather {
    public String status;

    public Basic basic;

    public AQI aqi;

    public Now now;

    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
