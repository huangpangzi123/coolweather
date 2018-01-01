package com.example.acer.coolweather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.acer.coolweather.db.City;
import com.example.acer.coolweather.db.County;
import com.example.acer.coolweather.db.Provice;
import com.example.acer.coolweather.util.HttpUtils;
import com.example.acer.coolweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by acer on 2017-12-26.
 */

public class ChooseAreaFragment extends Fragment {
        /*
        * 这几个常量表示数据的级别
        * */
    public static final int LEVEL_PROVINCE=0;
    public static final int LEVEL_CITY=1;
    public static final int LEVEL_COUNTY=2;

    /*
    * 声明几个控件
    * */
    private ProgressDialog progressDialog;
    private TextView titleText;
    private Button backButton;
    private ListView listView;

    /*
    * 声明一个适配器和一个
    * 数据数组，用来存放用户选择的省市县的信息
    * */
    private ArrayAdapter<String> adapter;
    private List<String> dataList=new ArrayList<>();

    /*
    *该数组用于存放要显示省份信息
    * */
    private List<Provice> provinceList;

    /*
    * 该数组用于存放要显示城市的信息
    * */
    private List<City> cityList;

    /*
    * 该数组用于存放要显示县的信息
    * */
    private List<County> countyList;

    /*
    * 被选中的省份
    * */
    private Provice selectedProvince;

    /*
    * 被选中的城市
    * */
    private City selectedCity;

    /*
    * 当前选中的级别（值为上边定义的常量值）
    * */
    private int currentLevel;

    /*
    * 该方法是启动碎片时就执行的
    * 在该方法中实例化了控件，并将适配器应用到ListView中
    * */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.choose_area_layout,container,false);
        titleText=(TextView)view.findViewById(R.id.title_text);
        backButton=(Button)view.findViewById(R.id.back_button);
        listView=(ListView)view.findViewById(R.id.list_view);
        adapter=new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        return view;
    }

    /*
    * 当活动被建立时执行该方法
    * 用于给ListView和backButton设置点击事件
    * */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*
                * 判断当前界面属于哪一级别，进行判断从而进入不同界面
                * */
                if(currentLevel==LEVEL_PROVINCE){
                    /*
                    * 将被选中的item的编号传递给selecteProvince
                    * */
                    selectedProvince=provinceList.get(position);
                    /*
                    * 进入选择城市界面
                    * */
                    queryCities();
                }else if(currentLevel==LEVEL_CITY){
                    selectedCity=cityList.get(position);
                    /*
                    * 进入选择县的界面
                    * */
                    queryCounties();
                }else if(currentLevel==LEVEL_COUNTY){
                    /*
                    * 如果当前级别为县级，则进入显示天气界面
                    * */
                    String weathrrId=countyList.get(position).getWeatherId();
                    /*
                    * 判断当前界面是主界面还是显示天气界面
                    * */
                    if(getActivity() instanceof MainActivity){
                        /*
                        * 如果当前是主界面(进入显示天气界面，关闭当前界面)
                        * */
                        Intent intent=new Intent(getActivity(),WeatherActivity.class);
                        intent.putExtra("weather_id",weathrrId);
                        startActivity(intent);
                        getActivity().finish();
                    }else if(getActivity() instanceof WeatherActivity){
                        WeatherActivity weatherActivity=(WeatherActivity)getActivity();
                        /*
                        * 关闭左侧滑动菜单
                        * */
                        weatherActivity.drawerLayout.closeDrawers();
                        /*
                        * 显示刷新条（提示正在加载当前城市天气信息）
                        * 然后重新加载天气信息
                        * */
                        weatherActivity.swipeRefreshLayout.setRefreshing(true);
                        weatherActivity.requestWeather(weathrrId);
                    }
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                * 如果级别为县级则返回市级界面
                * */
                if(currentLevel==LEVEL_COUNTY){
                    queryCities();
                }else if(currentLevel==LEVEL_CITY){
                    queryProvince();
                }
            }
        });
        queryProvince();
    }

    /*
    * 查询省份的数据，优先从数据库查询，如果在数据库查询不到，则从服务器查询
    * */
        private void queryProvince(){
            titleText.setText("中国");
            /*
            *将返回按钮隐藏起来
            * */
            backButton.setVisibility(View.GONE);
            provinceList= DataSupport.findAll(Provice.class);
            /*
            * 判断数据库中有没有数据，如果有则从数据库中读取数据
            * 如果没有，则调用queryFromServer方法从服务器获取数据
            * */
            if(provinceList.size()>0){
                dataList.clear();
                for(Provice province : provinceList){
                    dataList.add(province.getProviceName());
                }
                adapter.notifyDataSetChanged();
                listView.setSelection(0);
                currentLevel=LEVEL_PROVINCE;
            }else{
                String address="http://guolin.tech/api/china";
                queryFromServer(address,"province");
            }
        }

        /*
        * 查询城市的数据，优先从数据库查询，如果在数据库查询不到，则从服务器查询
        * */
        private void queryCities(){
            titleText.setText(selectedProvince.getProviceName());
            backButton.setVisibility(View.VISIBLE);
            /*
            * 查询指定省份ID内的城市数据
            * */
            cityList=DataSupport.where("proviceid=?",
                    String.valueOf(selectedProvince.getId())).find(City.class);
            if(cityList.size()>0){
                dataList.clear();
                for(City city : cityList){
                    dataList.add(city.getCityName());
                }
                adapter.notifyDataSetChanged();
                listView.setSelection(0);
                currentLevel=LEVEL_CITY;
            }else{
                int provinceId=selectedProvince.getProviceCode();
                String address="http://guolin.tech/api/china/"+provinceId;
                queryFromServer(address,"city");
            }
        }

        /*
        * 查询县的数据，优先从数据库查询，如果在数据库查询不到，则从服务器查询
        * */
        private void queryCounties(){
            titleText.setText(selectedCity.getCityName());
            backButton.setVisibility(View.VISIBLE);
            /*
            * 查询指定省份ID内的城市数据
            * */
            countyList=DataSupport.where("cityid=?",
                    String.valueOf(selectedCity.getId())).find(County.class);
            if(countyList.size()>0){
                dataList.clear();
                for(County county : countyList){
                    dataList.add(county.getCountyName());
                }
                adapter.notifyDataSetChanged();
                listView.setSelection(0);
                currentLevel=LEVEL_COUNTY;
            }else{
                int provinceId=selectedProvince.getProviceCode();
                int cityId=selectedCity.getCityCode();
                String address="http://guolin.tech/api/china/"+provinceId+"/"+cityId;
                queryFromServer(address,"county");
            }
        }
    /*
    * 该方法用于从服务器获取数据
    * （用户第一次使用该软件时，就需要从服务器获取数据）
    * 第一个参数是：服务器的地址
    * 第二个参数是：数据的级别（省？市？县？）
    * */
    private void queryFromServer(String address,final String type){
        showProgressDialog();
        /*
        * 调用连接服务器的方法
        * */
        HttpUtils.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText=response.body().string();
                boolean result=false;
                /*
                * 判断数据来自哪一界面，从而调用不同的处理方法
                * 来获取不同的数据（省？市？县？）
                * */
                if("province".equals(type)){
                    result= Utility.handleProvinceResponse(responseText);
                }else if("city".equals(type)){
                    result=Utility.handleCityResponse(responseText,selectedProvince.getId());
                }else if("county".equals(type)){
                    result=Utility.handleCountiesResponse(responseText,selectedCity.getId());
                }
                 /*
                  * 对返回的结果进行判断（如果为真则在主线程内进行显示数据操作）
                  * */
                if(result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if("province".equals(type)){
                            /*
                            * 进入选择省份界面
                            *
                            * */
                            queryProvince();
                            }else if("city".equals(type)){
                             /*
                            * 进入选择城市界面
                            * */
                             queryCities();
                            }else if("county".equals(type)){
                                 /*
                            * 进入选择县的界面
                            * */
                                queryCounties();
                            }
                        }
                    });
                }
        }
            /*
            * 获取数据失败时调用该方法
            * */
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /*
    * 显示进度条的方法
    * */
    private void showProgressDialog(){
        if(progressDialog==null){
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("加载中......");
            /*
            * 设置点击空白处不能关闭该进度条
            * */
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /*
    * 关闭进度条的方法（从服务器获取完数据之后调用该方法）
    * */
    private void closeProgressDialog(){
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }
}
