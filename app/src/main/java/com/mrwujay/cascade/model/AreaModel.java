package com.mrwujay.cascade.model;

import java.util.List;

public class AreaModel {
	private String name;
	private String id;
	private List<BankModel> districtList;


	public AreaModel(String name, List<BankModel> districtList) {
		super();
		this.name = name;
		this.districtList = districtList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public AreaModel() {
		super();
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<BankModel> getDistrictList() {
		return districtList;
	}

	public void setDistrictList(List<BankModel> districtList) {
		this.districtList = districtList;
	}

	@Override
	public String toString() {
		return "CityModel [name=" + name + ", districtList=" + districtList
				+ "]";
	}
	
}
