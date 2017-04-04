package com.example.coolweather.android.util;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.coolweather.android.db.City;
import com.example.coolweather.android.db.County;
import com.example.coolweather.android.db.Province;
import com.example.coolweather.android.gson.JSONCityEntity;
import com.example.coolweather.android.gson.Weather;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * author:Mark
 * date:2017/4/2
 * time:10:34
 */

public class DataParseUtil {

    static List<String> zxs = new ArrayList<>();

    static {
        zxs.add("北京");
        zxs.add("上海");
        zxs.add("天津");
        zxs.add("重庆");
    }

    /**
     * 解析和处理服务器返回的省级数据
     *
     * @param data
     * @return
     */
    public static boolean handleProvinceResponse(String data) {
        if (!TextUtils.isEmpty(data)) {
            List<String> provinceNames = new ArrayList<>();
            List<JSONCityEntity> entityList = JSON.parseArray(data, JSONCityEntity.class);
            for (JSONCityEntity entity : entityList) {
                if (!provinceNames.contains(entity.getProvinceZh())) {
                    provinceNames.add(entity.getProvinceZh());
                    Province province = new Province();
                    province.setProvinceName(entity.getProvinceZh());
                    province.setProvinceCode(entity.getId());
                    province.save();
                }
            }
            return true;
        }
        return false;
    }


    /**
     * 解析和处理服务器返回的市级数据
     *
     * @param data
     * @return
     */
    public static boolean handleCityResponse(String data, int provinceId, String provinceName) {
        if (!TextUtils.isEmpty(data)) {
            List<JSONCityEntity> entityList = JSON.parseArray(data, JSONCityEntity.class);
            for (JSONCityEntity entity : entityList) {
                if (provinceName.equals(entity.getProvinceZh())) {
                    if (zxs.contains(provinceName)) {
                        City city = new City();
                        city.setCityName(entity.getCityZh());
                        city.setCityCode(entity.getId());
                        city.setProvinceId(provinceId);
                        city.save();
                    } else if (entity.getCityZh().equals(entity.getLeaderZh())) {
                        City city = new City();
                        city.setCityName(entity.getCityZh());
                        city.setCityCode(entity.getId());
                        city.setProvinceId(provinceId);
                        city.save();
                    }
                }
            }
            return true;
        }
        return false;
    }


    /**
     * 解析和处理服务器返回的县级数据
     *
     * @param data
     * @return
     */

    public static boolean handleCountyResponse(String data, int cityId, String cityName, String provinceName) {
        if (!TextUtils.isEmpty(data)) {
            List<JSONCityEntity> entityList = JSON.parseArray(data, JSONCityEntity.class);
            for (JSONCityEntity entity : entityList) {
                if (provinceName.equals(entity.getProvinceZh()) && cityName.equals(entity.getLeaderZh()) && !cityName
                        .equals(entity.getCityZh())) {
                    County county = new County();
                    county.setCountyName(entity.getCityZh());
                    county.setCountyCode(entity.getId());
                    county.setWeatherId(entity.getId());
                    county.setCityId(cityId);
                    county.save();
                }
            }
            return true;
        }
        return false;
    }

    public static Weather handleWeatherResponse(String data) {
        JSONObject jsonObject = JSON.parseObject(data);
        JSONArray jsonArray = jsonObject.getJSONArray("HeWeather5");
        return new Gson().fromJson(jsonArray.getJSONObject(0).toString(), Weather.class);
    }
}
