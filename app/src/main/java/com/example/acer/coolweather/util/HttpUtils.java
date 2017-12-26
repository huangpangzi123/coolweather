package com.example.acer.coolweather.util;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by acer on 2017-12-26.
 * 该类用于从服务器获取数据
 */

public class HttpUtils {
//    参数是传入的地址和一个返回实例
    public static  void sendOkHttpRequest(String address,Callback callback){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
