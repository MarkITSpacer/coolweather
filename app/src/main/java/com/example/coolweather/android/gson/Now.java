package com.example.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * author:Mark
 * date:2017/4/4
 * time:14:31
 */

public class Now {
    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More {
        @SerializedName("txt")
        public String info;
    }
}
