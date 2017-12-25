package com.example.acer.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by acer on 2017-12-25.
 * 该类用于存放从网络获取各  县   的信息
 */

public class County extends DataSupport{
    private int id;
//    记录县的名字
    private String countyName;
//    记录县的编号
    private int countyCode;
//    记录该县所属城市的编号
    private int cityCode;
    public void setId(int id){
        this.id=id;
    }
    public int getId(){
        return id;
    }
    public void setCountyName(String countyName){
        this.countyName=countyName;
    }
    public String getCountyName(){
        return countyName;
    }
    public void setCountyCode(int countyCode){
        this.countyCode=countyCode;
    }
    public int getCountyCode(){
        return countyCode;
    }
    public void setCityCode(int cityCode){
        this.cityCode=cityCode;
    }
    public int getCityCode(){
        return cityCode;
    }
}

