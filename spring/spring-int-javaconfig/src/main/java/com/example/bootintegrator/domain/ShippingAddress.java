package com.example.bootintegrator.domain;

import java.io.Serializable;

public class ShippingAddress extends Address implements Serializable
{
	private static final long serialVersionUID = 2021972128810969724L;

	public ShippingAddress()
	{
		this.type = AddressType.SHIPPING_ADDRESS;
	}
	
	public ShippingAddress(String pLine, String pCity, String pCode)
	{
		super(pLine, pCity, pCode);
		this.type	=	AddressType.SHIPPING_ADDRESS;
	}
}
