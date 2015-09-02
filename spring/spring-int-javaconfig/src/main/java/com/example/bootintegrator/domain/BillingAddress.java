package com.example.bootintegrator.domain;

import java.io.Serializable;

public class BillingAddress extends Address implements Serializable
{

	private static final long serialVersionUID = 1132682806159961028L;

	public BillingAddress() {
		this.type = AddressType.BILLING_ADDRESS;
	}
	
	public BillingAddress(String pLine, String pCity, String pCode)
	{
		super(pLine, pCity, pCode);
		this.type	=	AddressType.BILLING_ADDRESS;
	}
}
