package com.example.bootintegrator.domain;

import java.io.Serializable;

public class Address implements Serializable 
{
	private static final long serialVersionUID = 7953189280727158942L;
	
	protected enum AddressType {
		BILLING_ADDRESS,
		SHIPPING_ADDRESS;
	}
	
	private String firstLineOfAddress;
	private String city;
	private String postCode;
	protected AddressType type;
	
	public Address() {
	}
	
	public Address(String pLine, String pCity, String pPostCode) {
		this.firstLineOfAddress = pLine;
		this.city = pCity;
		this.postCode = pPostCode;
	}
	
	public String getFirstLineOfAddress() {
		return firstLineOfAddress;
	}
	public void setFirstLineOfAddress(String firstLineOfAddress) {
		this.firstLineOfAddress = firstLineOfAddress;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public AddressType getType() {
		return type;
	}
	public void setType(AddressType type) {
		this.type = type;
	}
}
