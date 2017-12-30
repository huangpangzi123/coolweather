package com.example.acer.coolweather.gson;

/**
 * Created by acer on 2017-12-30.
 *
 * 该类用于解析JOSN数据中的 ”aqi“ 内容（空气质量）
 */

public class AQI {
    public AQICity city;
    public class AQICity{
        public String aqi;
        public String pm25;
    }
}
