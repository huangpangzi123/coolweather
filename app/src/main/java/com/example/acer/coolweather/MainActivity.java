package com.example.acer.coolweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

/*
* 该类上方的几个包的作用分别为：
* db包：用于存放数据库模型相关的代码
* gson包：用于存放与GSON模型有关的代码
* service包：用于存放于服务有关的代码
* util包：用于存放一些工具类
*/
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        * 从本地获取天气信息
        * */
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this);
        /*
        * 如果从本地获取到信息则直接进入显示天气界面
        * 如果获取不到信息则让用户选择城市
        * */
        if(preferences.getString("weather",null)!=null){
            Intent intent=new Intent(this,WeatherActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
