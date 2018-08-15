package com.mrwujay.cascade.addressModel;

import java.util.List;

public class ProvinceModel {
	private String name;
	private String code;
	private List<CityModel> cityList;

	public ProvinceModel() {
		super();
	}

	public ProvinceModel(String name, String provinceCode, List<CityModel> cityList) {
		super();
		this.name = name;
		this.code=provinceCode;
		this.cityList = cityList;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String provinceCode) {
		this.code = provinceCode;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CityModel> getCityList() {
		return cityList;
	}

	public void setCityList(List<CityModel> cityList) {
		this.cityList = cityList;
	}

	@Override
	public String toString() {
		return "ProvinceModel{" +
				"name='" + name + '\'' +
				", provinceCode='" + code + '\'' +
				", cityList=" + cityList +
				'}';
	}
}