package com.example.acer.coolweather.db;

import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * Created by acer on 2017-12-25.
 * 该类用于存放从网络获取各  省份   的信息
 */

public class Provice extends DataSupport{
    private int id;
//    记录省份的名字
    private String proviceName;
//    记录该省对应的编号
    private int proviceCode;
    public void setProviceName(String proviceName){
        this.proviceName=proviceName;
    }
    public String getProviceName(){
        return proviceName;
    }
    public void setPriviceCode(int proviceCode){
        this.proviceCode=proviceCode;
    }
    public int getProviceCode(){
        return proviceCode;
    }
    public void setId(int id){
        this.id=id;
    }
    public int getId(){
        return id;
    }
}
