package com.example.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * author:Mark
 * date:2017/4/4
 * time:14:34
 */

public class Suggestion {

    @SerializedName("comf")
    public Comfort comfort;


    @SerializedName("cw")
    public CarWash carWash;

    public Sport sport;

    public class Comfort {
        @SerializedName("txt")
        public String info;
    }

    public class CarWash {
        @SerializedName("txt")
        public String info;
    }

    public class Sport {
        @SerializedName("txt")
        public String info;
    }

}