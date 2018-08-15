package com.mrwujay.cascade.addressModel;

import java.util.List;

public class CityModel {
	private String name;
	private String code;
	private List<DistrictModel> districtList;
	
	public CityModel() {
		super();
	}

	public CityModel(String name, String cityCode, List<DistrictModel> districtList) {
		super();
		this.name = name;
		this.code=cityCode;
		this.districtList = districtList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String cityCode) {
		this.code = cityCode;
	}
	public List<DistrictModel> getDistrictList() {
		return districtList;
	}

	public void setDistrictList(List<DistrictModel> districtList) {
		this.districtList = districtList;
	}

	@Override
	public String toString() {
		return "CityModel{" +
				"name='" + name + '\'' +
				", cityCode='" + code + '\'' +
				", districtList=" + districtList +
				'}';
	}
}
