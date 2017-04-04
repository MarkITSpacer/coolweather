package com.example.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * author:Mark
 * date:2017/4/4
 * time:14:21
 */

public class Basic {
    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String cityId;

    public Update update;

    public class Update {
        @SerializedName("loc")
        public String updateTime;
    }
}
