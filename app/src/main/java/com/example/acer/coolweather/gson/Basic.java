package com.example.acer.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by acer on 2017-12-30.
 * 该类用于解析JOSN数据中的 ”basic“ 内容（城市基本信息）
 */
public class Basic {
    /*
    * SerializedName注解可以解决自己定义的java对象里的属性名跟json里的字段名不一样的情况.
    * 使用@SerializedName注解来将对象里的属性跟json里字段对应值匹配起来
    * 注解后边括号内的字段都与JSON数据里的字段一样
    * */
    /*
    * JSON数据中的城市名
    * */
    @SerializedName("city")
    public String cityName;
    /*
    * JSON数据中的该城市对应的天气ID
    * */
    @SerializedName("id")
    public String weatherId;
    /*
    * 因为在JSON数据中update也是包含其他内容的，所以这里另外建立一个类
    * */
    public Update update;
    public class Update{
        /*
        * JSON数据中的更新时间（loc）
        * */
        @SerializedName("loc")
        public String updateTime;
    }
}
