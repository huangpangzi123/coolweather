package com.example.acer.coolweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
/*
* 该类上方的几个包的作用分别为：
* db包：用于存放数据库模型相关的代码
* gson包：用于存放与GSON模型有关的代码
* service包：用于存放于服务有关的代码
* util包：用于存放一些工具类
*/
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
