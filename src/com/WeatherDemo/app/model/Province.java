package com.WeatherDemo.app.model;

public class Province {
	private int id;
	private String provinceName;
	private String provinceCode;
	public int getId()
	{
		return id;
	}
	public void setId(int Id)
	{
		this.id=Id;
	}
	public String getProvinceName()
	{
		return provinceName;
	}
	public void setProvinceName(String ProvinceName)
	{
		this.provinceName=ProvinceName;
	}
	public String getProvinceCode()
	{
		return provinceCode;
	}
	public void setProvinceCode(String ProvinceCode)
	{
		this.provinceCode=ProvinceCode;
	}
}
