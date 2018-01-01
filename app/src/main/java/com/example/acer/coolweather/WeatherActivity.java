package com.example.acer.coolweather;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.acer.coolweather.gson.Basic;
import com.example.acer.coolweather.gson.Forecast;
import com.example.acer.coolweather.gson.Weather;
import com.example.acer.coolweather.util.HttpUtils;
import com.example.acer.coolweather.util.Utility;

import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/*
* 该活动是显示天气信息的界面
* */
public class WeatherActivity extends AppCompatActivity {
        /*
        * 声明各个控件
        * */
        private ScrollView weatherLayout;
        private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastlayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private ImageView bingPicImg;
    /*
    * 与手动刷新有关的变量
    * */
    public SwipeRefreshLayout swipeRefreshLayout;
    private String mWeatherId;
    /*
    * 与左侧滑动菜单有关
    * */
    public DrawerLayout drawerLayout;
    private Button navButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        * 该判断的作用是将背景图融合到一起
        * 只有Android5.0以上才支持这种融合
        * 5.0以下的版本不执行if语句里的方法
        * */
        if(Build.VERSION.SDK_INT>=21){
            View decorView=getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        /*
        * 初始化各个控件
        * */
        weatherLayout=(ScrollView)findViewById(R.id.weather_layout);
        titleCity=(TextView)findViewById(R.id.title_city);
        titleUpdateTime=(TextView)findViewById(R.id.title_update_time);
        degreeText=(TextView)findViewById(R.id.degree_text);
        weatherInfoText=(TextView)findViewById(R.id.weather_info_text);
        forecastlayout=(LinearLayout)findViewById(R.id.forecast_layout);
        aqiText=(TextView)findViewById(R.id.aqi_text);
        pm25Text=(TextView)findViewById(R.id.pm25_text);
        comfortText=(TextView)findViewById(R.id.comfort_text);
        carWashText=(TextView)findViewById(R.id.car_wash_text);
        sportText=(TextView)findViewById(R.id.sport_text);
        bingPicImg=(ImageView)findViewById(R.id.bing_pic_img);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        navButton=(Button)findViewById(R.id.nav_button);
        /*
        * 设置刷新条的颜色
        * */
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        /*
        * 尝试从本地读取天气数据
        * */
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString=preferences.getString("weather",null);
//        从本地获取背景图片
        String bingPic=preferences.getString("bing_pic",null);
        if(weatherString!=null){
            /*
            *从本地获取到了数据
            * 直接解析本地存储的数据，并调用显示方法将天气信息显示出来
            * */
            Weather weather= Utility.handleWeatherResponse(weatherString);
            /*
            * 将该城市的天气ID读取出来，刷新时可以准确获取该城市天气信息
            * */
            mWeatherId=weather.basic.weatherId;
            showWeatherinfo(weather);
        }else{
            /*
            * 本地无缓存，去服务器获取数据（本地无数据说明是初次进入该软件，则进行选择城市）
            * 从主界面进入该界面是，会使用Intent传递天气的ID
            * */
            mWeatherId=getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId);
        }
        /*
        * 设置手动刷新事件
        * */
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
            }
        });
        /*
        * 设置点击标题栏左侧的按钮，召唤出左侧滑动菜单
        * */
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              设置根据系统语言从左侧或右侧滑出
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        /*
        * 判断是否从本地获取到了图片
        * */
        if(bingPic!=null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else{
            /*
            * 获取不到则从网络获取
            * */
            loadBingPic();
        }
    }

    /*
    * 该方法根据天气ID从服务器获取天气信息
    * */
    public void requestWeather(final String weatherId){
        String weatherUrl="http://guolin.tech/api/weather?cityid="+weatherId+
                "&key=62e95098664d4c99a39c6a61b48a64de";
        HttpUtils.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
//                返回的JSOn数据
                final String responseText=response.body().string();
//                将JSON解析为Weather对象
                final Weather weather=Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather!=null&&"ok".equals(weather.status)){
                            /*
                            * 如果获取数据成功，则将数据保存到本地
                            * */
                            SharedPreferences.Editor editor=PreferenceManager.
                                    getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather",responseText);
                            editor.apply();
                            showWeatherinfo(weather);
                        }else{
                            Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
        /*
        * 从网络获取背景图
        * */
        loadBingPic();
    }

    /*
    * 该方法将天气信息显示出来
    * */
    private void showWeatherinfo(Weather weather){
        /*
        * 获取相关信息
        * */
        String cityName=weather.basic.cityName;
        String updateTime=weather.basic.update.updateTime.split(" ")[1];
        String degree=weather.now.temperature+"℃";
        String weatherInfo=weather.now.more.info;
        /*
        * 将信息显示出来
        * */
        titleCity.setText(cityName);
        titleUpdateTime.setText("更新于："+updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        /*
        * 显示未来几天的天气预报
        * 用for循环来处理
        * */
        forecastlayout.removeAllViews();
        for(Forecast forecast : weather.forecastList){
            View view= LayoutInflater.from(this).inflate(R.layout.forecast_item_layout,
                    forecastlayout,false);
            TextView dateText=(TextView)view .findViewById(R.id.date_text);
            TextView infotext=(TextView)view .findViewById(R.id.info_text);
            TextView maxText=(TextView)view.findViewById(R.id.max_text);
            TextView minText=(TextView)view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infotext.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastlayout.addView(view);
        }
        /*
        * 继续显示空气质量和生活建议
        * */
        if(weather.aqi!=null){
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }
        String comfort="舒适度："+weather.suggestion.comfort.info;
        String carWash="洗车建议："+weather.suggestion.carWash.info;
        String sport="运动建议："+weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        /*
        * 将下拉条显示出来
        * */
        weatherLayout.setVisibility(View.VISIBLE);
    }
    /*
    * 从必应获取图片
    * */
    public void loadBingPic(){
        String requestBingPic="http://guolin.tech/api/bing_pic";
        HttpUtils.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic=response.body().string();
                /*
                * 将图片保存到本地
                * */
                SharedPreferences.Editor editor=PreferenceManager.
                        getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                /*
                * 将图片显示出来
                * */
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }
}
