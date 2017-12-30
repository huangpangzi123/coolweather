package com.example.acer.coolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by acer on 2017-12-30.
 *
 * 该类用于管理所有解析JOSN数据的类
 */

public class Weather {
    /*
    * 这个变量存放是否成功获取天气
    * 成功返回：ok
    * 失败返回具体失败原因
    * */
    public String status;
    /*
    * 实例化那几个天气信息类
    * */
    public Basic basic;
    public AQI aqi;
    public Now now;
    public Suggestion suggestion;
    /*
    * 因为天气预报的数据比较多，所以这里定义一个数组来存放天气预报实体
    * */
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
