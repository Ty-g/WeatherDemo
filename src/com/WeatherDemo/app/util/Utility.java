package com.WeatherDemo.app.util;

import android.text.TextUtils;
import android.util.Log;

import com.WeatherDemo.app.db.WeatherDemoDB;
import com.WeatherDemo.app.model.City;
import com.WeatherDemo.app.model.County;
import com.WeatherDemo.app.model.Province;

public class Utility {
	/**
	 * 解析和处理服务器返回来的省级数据
	 */
	public synchronized static boolean handleProvinceResponce(WeatherDemoDB weatherDemoDB,String response)
	{
		if (!TextUtils.isEmpty(response)) {
			String[] allProvinces = response.split(",");
			if (allProvinces != null && allProvinces.length > 0) {
				for (String p : allProvinces) {
					String[] array = p.split("\\|");
					Province province = new Province();
				//	Log.d("Utility","array[0]:"+array[0]);
					//Log.d("Utility","array[1]:"+array[1]);
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					
					weatherDemoDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
		
	}
	/**
	 * 解析服务器返回来的市级数据
	 */
	public synchronized static boolean handleCitiesResponce(WeatherDemoDB weatherDemoDB,String response,int provinceId)
	{
		if (!TextUtils.isEmpty(response)) {
			String[] allCities = response.split(",");
			if (allCities != null && allCities.length > 0) {
				for (String c : allCities) {
					String[] array = c.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					Log.d("Utility_City","array[0]:"+array[0]);
					Log.d("Utility_City","array[1]:"+array[1]);
					Log.d("Utility_provinceId","Utility_provinceId"+provinceId);
					city.setProvinceId(provinceId);
					
					weatherDemoDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}
	/**
	 * 解析服务器返回的县级数据
	 */
	public synchronized static boolean handleCountiesResponce(WeatherDemoDB weatherDemoDB,String responce,int cityId)
	{
		if(!TextUtils.isEmpty(responce))
		{
			String[] allCounties =responce.split(",");
			if(allCounties!=null&&allCounties.length>0)
			{
				for(String c:allCounties)
				{
					String[] array=c.split("\\|");
					County county=new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityId(cityId);
					weatherDemoDB.saveCounty(county);
					
				}
				return true;
			}
		}
		return false;
	}
}
