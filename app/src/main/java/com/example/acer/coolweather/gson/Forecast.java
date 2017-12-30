package com.example.acer.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by acer on 2017-12-30.
 *
 * 该类用于解析JOSN数据中的 ”daily_forecast“ 内容（未来几天天气预报）
 *
 * 由于天气预报包含多天的天气，而且每天的预报都基本相似。
 * 所以这里只需定义出单日的预报实体类就可以了
 */
public class Forecast {
    /*
    * 预报的日期
    * */
    public String date;
    /*
    * 温度
    * */
    @SerializedName("tmp")
    public Temperature temperature;
    public class Temperature{
        /*
        * 最高和最低温度
        * */
        public String max;
        public String min;
    }
    /*
    * 天气状况
    * */
    @SerializedName("cond")
    public More more;
    public class More{
        /*
        * 具体的天气状况
        * */
        @SerializedName("txt_d")
        public String info;
    }
}
