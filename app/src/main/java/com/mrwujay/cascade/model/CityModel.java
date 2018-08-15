package com.mrwujay.cascade.model;

import java.util.List;

public class CityModel {
	private String name;
	private String id;
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private List<AreaModel> cityList;
	
	public CityModel() {
		super();
	}

	public CityModel(String name, List<AreaModel> cityList) {
		super();
		this.name = name;
		this.cityList = cityList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<AreaModel> getCityList() {
		return cityList;
	}

	public void setCityList(List<AreaModel> cityList) {
		this.cityList = cityList;
	}

	@Override
	public String toString() {
		return "ProvinceModel [name=" + name + ", cityList=" + cityList + "]";
	}
	
}
