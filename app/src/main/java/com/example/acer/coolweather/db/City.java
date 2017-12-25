package com.example.acer.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by acer on 2017-12-25.
 * 该类用于存放从网络获取各  城市   的信息
 */

public class City extends DataSupport{
    private int id;
//        记录城市的名字
    private String cityName;
//        记录该城市对应的编号
    private int cityCode;
//    该城市所属省份的编号
    private int proviceCode;
    public void setCityName(String proviceName){
        this.cityName=proviceName;
    }
    public String getCityName(){
        return cityName;
    }
    public void setCityCode(int cityCode){
        this.cityCode=cityCode;
    }
    public int getCityCode(){
        return cityCode;
    }
    public void setId(int id){
        this.id=id;
    }
    public int getId(){
        return id;
    }
    public void setPriviceCode(int proviceCode){
        this.proviceCode=proviceCode;
    }
    public int getProviceCode(){
        return proviceCode;
    }
}
