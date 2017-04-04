package com.example.coolweather.android;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.coolweather.android.gson.Forecast;
import com.example.coolweather.android.gson.Weather;
import com.example.coolweather.android.util.DataParseUtil;
import com.example.coolweather.android.util.HttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private ScrollView weatherLayoutView;

    private TextView titleCityText;

    private TextView titleUpdateTimeText;

    private TextView degreeText;

    private TextView weatherInfoText;

    private LinearLayout forecastLayout;

    private TextView aqiText;

    private TextView pm25Text;

    private TextView comfortText;

    private TextView carWashText;

    private TextView sportText;

    private ImageView bingPicImage;


    private final String WEATHER_BASE_URL = "https://free-api.heweather.com/v5/weather";

    private final String KEY = "71829b0977634ed7af9c6ee68feebbd2";

    private final String BING_PIC_URL = "http://guolin.tech/api/bing_pic";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        weatherLayoutView = (ScrollView) findViewById(R.id.weather_layout);
        titleCityText = (TextView) findViewById(R.id.title_city);
        titleUpdateTimeText = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        bingPicImage = (ImageView) findViewById(R.id.bing_pic_img);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String weatherString = preferences.getString("weather", null);
        String bingPicResource = preferences.getString("bing_pic", null);

        if (bingPicResource != null) {
            Glide.with(WeatherActivity.this).load(bingPicResource).into(bingPicImage);
        } else {
            loadBingPic();
        }

        if (weatherString != null) {
            Weather weather = DataParseUtil.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
        } else {
            String weatherId = getIntent().getStringExtra("weather_id");
            weatherLayoutView.setVisibility(View.INVISIBLE);

            RequestBody body = new FormBody.Builder()
                    .add("city", weatherId)
                    .add("key", KEY)
                    .build();
            HttpUtil.sendOkHttpRequest(WEATHER_BASE_URL, body, "POST", new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String responseText = response.body().string();
                    final Weather weather = DataParseUtil.handleWeatherResponse(responseText);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (weather != null && "ok".equals(weather.status)) {
                                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                                editor.putString("weather", responseText);
                                editor.apply();
                                showWeatherInfo(weather);
                            } else {
                                Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }
    }

    public void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature;
        String weatherInfo = weather.now.more.info;

        titleCityText.setText(cityName);
        titleUpdateTimeText.setText(updateTime);
        degreeText.setText(degree + "℃");
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(WeatherActivity.this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);

            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }

        if (weather.aqi != null) {
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        } else {
            aqiText.setText("无");
            pm25Text.setText("无");
        }

        String comfort = "舒适度：\n" + weather.suggestion.comfort.info;
        String carWash = "洗车指数：\n" + weather.suggestion.carWash.info;
        String sport = "运动建议：\n" + weather.suggestion.sport.info;

        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayoutView.setVisibility(View.VISIBLE);
    }

    private void loadBingPic() {
        HttpUtil.sendOkHttpRequest(BING_PIC_URL, null, "GET", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPicResource = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic", bingPicResource);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPicResource).into(bingPicImage);
                    }
                });
            }
        });
    }
}
