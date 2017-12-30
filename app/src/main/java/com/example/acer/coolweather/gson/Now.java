package com.example.acer.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by acer on 2017-12-30.
 *
 * 该类用于解析JOSN数据中的 ”now“ 内容（当前天气信息）
 */

public class Now {
    /*
    * JOSN数据中的温度信息
    * */
    @SerializedName("tmp")
    public String temperature;
    /*
    * 当天的天气信息
    * */
    @SerializedName("cond")
    public More more;
    public class More{
        /*
        * 具体的天气信息
        * */
        @SerializedName("txt")
        public String info;
    }
}
