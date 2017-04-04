package com.example.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * author:Mark
 * date:2017/4/4
 * time:14:38
 */

public class Forecast {
    public String date;

    @SerializedName("tmp")
    public Temperature temperature;

    @SerializedName("cond")
    public More more;

    public class Temperature{
        public String max;

        public String min;
    }

    public class More{
        @SerializedName("txt_d")
        public String info;
    }
}
