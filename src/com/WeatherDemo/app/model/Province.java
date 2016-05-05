package com.WeatherDemo.app.model;

public class Province {
	private int id;
	private String provinceName;
	private String provinceCode;
	public int getID()
	{
		return id;
	}
	public void setId(int Id)
	{
		this.id=id;
	}
	public String getProvinceName()
	{
		return provinceName;
	}
	public void setProvinceNanme(String ProvinceName)
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
