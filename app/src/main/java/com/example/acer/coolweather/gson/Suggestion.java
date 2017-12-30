package com.example.acer.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by acer on 2017-12-30.
 *
 * 该类用于解析JOSN数据中的 ”suggestion“ 内容（生活建议）
 */

public class Suggestion {
    /*
    * 舒适度建议
    * */
    @SerializedName("comf")
    public Comfort comfort;
    public class Comfort{
        /*
        * 建议的具体内容
        * */
        @SerializedName("txt")
        public String info;
    }
    /*
    * 洗车建议
    * */
    @SerializedName("cw")
    public CarWash carWash;
    public class CarWash{
        /*
       * 建议的具体内容
       * */
        @SerializedName("txt")
        public String info;
    }
    /*
    * 运动建议
    * 因为这里定义的变量名与JSON中的一样，所以不需要在使用注解
    * */
    public Sport sport;
    public class Sport{
        /*
       * 建议的具体内容
       * */
        @SerializedName("txt")
        public String info;
    }
}
