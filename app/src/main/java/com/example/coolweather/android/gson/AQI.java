package com.example.coolweather.android.gson;

/**
 * author:Mark
 * date:2017/4/4
 * time:14:28
 */

public class AQI {

    public AQICity city;

    public class AQICity {
        public String aqi;

        public String pm25;
    }
}
