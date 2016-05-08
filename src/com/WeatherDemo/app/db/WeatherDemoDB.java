package com.WeatherDemo.app.db;

import java.util.ArrayList;
import java.util.List;

import com.WeatherDemo.app.db.WeatherOpenHelper;
import com.WeatherDemo.app.model.City;
import com.WeatherDemo.app.model.County;
import com.WeatherDemo.app.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class WeatherDemoDB {

	/**
	 * 数据库名
	 */
	public static final String DB_NAME="Weather_Demo";
	/**
	 * 数据库版本
	 */
	public static final int VERSION=1;
	private static WeatherDemoDB Weatherdemodb;
	private SQLiteDatabase db;
	/*
	 * 构造方法私有化
	 */
	private WeatherDemoDB(Context context)
	{
		WeatherOpenHelper dbHelper =new WeatherOpenHelper(context, DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}
	/**
	 * 获取WeatherDB实例
	 */
	public synchronized static WeatherDemoDB getInstance(Context context)
	{
		if(Weatherdemodb==null)
		{
			Weatherdemodb 	=new WeatherDemoDB(context);
		}
		return Weatherdemodb;
		
	}
	/**
	 * 将Province实例存储到数据库
	 */
	public void saveProvince(Province province)
	{
		if(province!=null)
		{
			ContentValues values = new ContentValues();
			//Log.d("Province_save","id:"+province.getId());
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("Province", null, values);
		}
	}
	/**
	 * 从数据库中读取全国省份信息
	 */
	public List<Province> loadProvince()
	{
		List<Province> list = new ArrayList<Province>();
		Cursor cursor  =db.query("Province", null, null, null, null,null, null);
		if(cursor.moveToFirst())
		{
			do
			{
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
			//	Log.d("Province_load","id:"+province.getId());
				list.add(province);
				
			}while(cursor.moveToNext());
		}
		return list;
		
	}
	/**
	 * 将City实例存入数据库
	 */
	public void saveCity(City city)
	{
		if(city!=null)
		{
			ContentValues values = new ContentValues();
		//	Log.d("City_save","id:"+city.getProvinceId());
			values.put("city_name",city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_id", city.getProvinceId());
			db.insert("City", null, values);
		}
	}
	/**
	 * 从数据库读取城市信息
	 */
	public List<City> loadCities(int provinceId)
	{
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_id=?", new String[]{String.valueOf(provinceId)}, null,null, null);
		if(cursor.moveToFirst())
		{
			do
			{
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProvinceId(provinceId);
			//	Log.d("City_load","ProvinceID:"+city.getProvinceId());
			//	Log.d("City_load","id:"+city.getId());
				list.add(city);
			}while(cursor.moveToNext());
		}
		return list;
		
	}
	/**
	 * 将县级信息存入数据库
	 */
	public void saveCounty(County county)
	{
		if(county!=null)
		{
			ContentValues values = new ContentValues();
		//	Log.d("County_save","id:"+county.getCityId());
			values.put("county_name",county.getCountyName());
			values.put("county_code", county.getCountyCode());
			values.put("city_id", county.getCityId());
			db.insert("County", null, values);
		}
	}
	/**
	 * 从数据库读取县级信息
	 */
	public List<County> loadCounty(int cityId)
	{
		List<County> list = new ArrayList<County>();
		Cursor cursor=db.query("County", null, "city_id=?", new String[]{String.valueOf(cityId)}, null, null, null);
		if(cursor.moveToFirst())
		{
			do
			{
				County county = new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
				county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
				county.setCityId(cityId);
			//	Log.d("County_load","id:"+county.getId());
			//	Log.d("County_load","id:"+county.getCityId());
				list.add(county);
			}while(cursor.moveToNext());
		}
		return list;
	}
}
