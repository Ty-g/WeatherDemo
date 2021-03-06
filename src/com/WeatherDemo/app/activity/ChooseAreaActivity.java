package com.WeatherDemo.app.activity;

import java.util.ArrayList;
import java.util.List;

import com.WeatherDemo.app.db.WeatherDemoDB;
import com.WeatherDemo.app.model.City;
import com.WeatherDemo.app.model.County;
import com.WeatherDemo.app.model.Province;
import com.WeatherDemo.app.util.HttpCallbackListener;
import com.WeatherDemo.app.util.HttpUtil;
import com.WeatherDemo.app.util.Utility;

import com.example.weatherdemo.R;

import android.R.anim;
import android.app.Activity;
import android.app.DownloadManager.Query;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity{
	public static final int LEVEL_PROVINCE=0;
	public static final int LEVEL_CITY=1;	
	public static final int LEVEL_COUNTY=2;
	private ProgressDialog progressDialog;
	private TextView titleText;
	private ListView listview;
	private ArrayAdapter<String> adapter;
	private WeatherDemoDB weatherDemoDB;
	private List<String> dataList = new ArrayList<String>();
	/**
	 * 省级列表
	 */
	private List<Province> provinceList;
	/**
	 * 实绩表
	 */
	private List<City> cityList;
	/**
	 * 县级表
	 */
	private List<County> countyList;
	/**
	 * 选中省份
	 */
	private Province selectedProvince;
	/**
	 * 选中城市
	 */
	private City selectedCity;
	/**
	 * 选中县级
	 */
	private County selectedCounty;
	/**
	 * 当前选中级别
	 */
	private int currentLevel;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		listview = (ListView) findViewById(R.id.list_view);
		titleText = (TextView) findViewById(R.id.title_text);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
		listview.setAdapter(adapter);
		weatherDemoDB=weatherDemoDB.getInstance(this);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index,
					long arg3) {
				if (currentLevel == LEVEL_PROVINCE) {
					Log.d("Choose","province_index"+provinceList.get(index));
					selectedProvince = provinceList.get(index);
					queryCities();
				} else if (currentLevel == LEVEL_CITY) {
					selectedCity = cityList.get(index);
					queryCounties();
				} 
			}
		});
		queryProvinces();  // 鍔犺浇鐪佺骇鏁版嵁
	}
	/**
	 * 查询全国所有省，优先从数据库查询，如果没有查询到再去服务器查询
	 */
	private void queryProvinces()
	{
		provinceList =weatherDemoDB.loadProvince();
		if(provinceList.size()>0)
		{
			dataList.clear();
			for(Province province:provinceList)
			{
				dataList.add(province.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			listview.setSelection(0);
			titleText.setText("中国");
			currentLevel = LEVEL_PROVINCE;
		}
		else 
		{
			queryFromServer(null,"province");
		}
	}	
	/**
	 * 查询省内所有城市，优先从数据库查询，如果没有查询到再去服务器查询
	 */
	private void queryCities()
	{
		Log.d("Province","Pronvince:"+selectedProvince.getId());
		cityList = weatherDemoDB.loadCities(selectedProvince.getId());
		if(cityList.size()>0)
		{
			dataList.clear();
			for(City city:cityList)
			{
				dataList.add(city.getCityName());
			}
			adapter.notifyDataSetChanged();
			listview.setSelection(0);
			titleText.setText(selectedProvince.getProvinceName());
			currentLevel=LEVEL_CITY;
		}
		else 
		{
			queryFromServer(selectedProvince.getProvinceCode(),"city");
		}
	}
	/**
	 * 查询城市内所有县，优先从数据库查询，如果没有查询到再去服务器查询
	 */
	private void queryCounties()
	{
		countyList = weatherDemoDB .loadCounty(selectedCity.getId());
		Log.d("Province","County:"+selectedCity.getId());
		if(countyList.size()>0)
		{
			dataList.clear();
			for(County county:countyList)
			{
				dataList.add(county.getCountyName());
			}
			adapter.notifyDataSetChanged();
			listview.setSelection(0);
			titleText.setText(selectedCity.getCityName());
			currentLevel=LEVEL_COUNTY;
		}
		else
		{
			queryFromServer(selectedCity.getCityCode(),"county");
		}
	}
	/**
	 * 根据传入的Code和类型去服务器上查询数据
	 */
	private void queryFromServer(final String code,final String type)
	{
		String address;
		if(!TextUtils.isEmpty(code))
		{
			address="http://www.weather.com.cn/data/list3/city" + code + ".xml";
		}
		else
		{
			address= "http://www.weather.com.cn/data/list3/city.xml";
		}
		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				boolean result =false;
				if("province".equals(type))
				{
					result =Utility.handleProvinceResponce(weatherDemoDB, response);
				}
				else if("city".equals(type))
				{
					result = Utility.handleCitiesResponce(weatherDemoDB, response,selectedProvince.getId());
				}
				else if("county".equals(type))
				{
					result = Utility.handleCountiesResponce(weatherDemoDB, response,selectedCity.getId());
				}
				if(result)
				{
					//通过runOnUiThread发放返回主线程处理逻辑
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							closeProgressDialog();
							if("province".equals(type))
							{
								queryProvinces();
							}
							else if("city".equals(type))
							{
								queryCities();
							}
							else if("county".equals(type))
							{
								queryCounties();
							}
						}
					});
				}
				
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				//通runOnThread方法返回主线程处理逻辑
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
						
					}
				});
				
			}
		});
	}
	private void showProgressDialog()
	{
		if(progressDialog==null)
		{
			progressDialog =new ProgressDialog(this);
			progressDialog.setMessage("正在加载");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}
	private void closeProgressDialog()
	{
		if(progressDialog!=null)
		{
			progressDialog.dismiss();
		}
	}
	/**
	 * 获取Back按键，根基当前级别来判断，此时返回省级，市级，县级，还是直接退出
	 */
	public void onBackPressed()
	{
		if(currentLevel==LEVEL_COUNTY)
		{
			queryCities();
		}
		else if (currentLevel==LEVEL_CITY)
		{
			queryProvinces();
		}
		else
		{
			finish();
		}
	}
}
