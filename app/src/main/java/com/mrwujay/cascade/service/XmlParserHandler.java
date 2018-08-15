package com.mrwujay.cascade.service;

import com.mrwujay.cascade.model.AreaModel;
import com.mrwujay.cascade.model.BankModel;
import com.mrwujay.cascade.model.CityModel;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class XmlParserHandler extends DefaultHandler {
	private List<CityModel> provinceList = new ArrayList<CityModel>();

	public XmlParserHandler() {

	}

	public List<CityModel> getDataList() {
		return provinceList;
	}

	@Override
	public void startDocument() throws SAXException {
	}

	CityModel provinceModel = new CityModel();
	AreaModel cityModel = new AreaModel();
	BankModel districtModel = new BankModel();

	@Override
	public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
		if (qName.equals("City")) {
			provinceModel = new CityModel();
			provinceModel.setName(attributes.getValue(0));
			provinceModel.setId(attributes.getValue(1));
			provinceModel.setCityList(new ArrayList<AreaModel>());
		} else if (qName.equals("Region")) {
			cityModel = new AreaModel();
			cityModel.setName(attributes.getValue(0));
			cityModel.setId(attributes.getValue(1));
			cityModel.setDistrictList(new ArrayList<BankModel>());
		} else if (qName.equals("Bank")) {
			districtModel = new BankModel();
			districtModel.setName(attributes.getValue(0));
			districtModel.setZipcode(attributes.getValue(1));
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (qName.equals("Bank")) {
			cityModel.getDistrictList().add(districtModel);
		} else if (qName.equals("Region")) {
			provinceModel.getCityList().add(cityModel);
		} else if (qName.equals("City")) {
			provinceList.add(provinceModel);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
	}

}
