package com.example.acer.coolweather.util;

import android.text.TextUtils;

import com.example.acer.coolweather.db.City;
import com.example.acer.coolweather.db.County;
import com.example.acer.coolweather.db.Provice;
import com.example.acer.coolweather.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by acer on 2017-12-26.
 * 该类接受的数据都是JSON格式的，该类也是负责解析JSON数据的
 */

public class Utility {
    /*
    * 该方法用于解析从服务器获取的省级数据
    * 参数是服务器传回的JSON数据
    * */
    public static boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allProvinces=new JSONArray(response);
                for(int k=0;k<allProvinces.length();k++){
//                    将第k个省份数据转化为一个Object对象，方便往Province类里存放
                    JSONObject provinceObject=allProvinces.getJSONObject(k);
                    Provice provice=new Provice();
                    provice.setProviceName(provinceObject.getString("name"));
                    provice.setPriviceCode(provinceObject.getInt("id"));
                    provice.save();
                }
                return true;
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }
    /*
    * 该方法用于处理服务器返回的市级数据
    * 第一个参数是：服务器返回的市级数据
    * 第二个参数是：该城市所属省份的编号
    * */
    public static boolean handleCityResponse(String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allCities=new JSONArray(response);
                for(int k=0;k<allCities.length();k++){
//                    将第k个城市数据转化为一个Object对象，方便往City类里存放
                    JSONObject cityObject=allCities.getJSONObject(k);
                    City city=new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
//                    该城市所属省份的编号
                    city.setProviceId(provinceId);
                    city.save();
                }
                return true;
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }
    /*
    * 该方法用于解解析服务器返回的县级数据
    * 第一个参数是：服务器传回的县级数据
    * 第二个参数是：该县所属城市的编号
    * */
    public static boolean handleCountiesResponse(String response,int cityId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allCounties=new JSONArray(response);
                for(int k=0;k<allCounties.length();k++){
//                    将第k个县的数据转化为一个Object对象，方便往County类里存放
                    JSONObject countyObject=allCounties.getJSONObject(k);
                    County county=new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /*
    * 将JSON数据解析成Weather实体类
    * */
    public static Weather handleWeatherResponse(String response){
        try{
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather");
            String weatherContent=jsonArray.getJSONObject(0).toString();
            /*
            * 通过调用fromJson方法可直接将JSON数据转化为Weather对象
            * 第一个参数是：解析出的JSON数据
            * 第二个参数是：要转化为的实体类
            * */
            return new Gson().fromJson(weatherContent,Weather.class);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
