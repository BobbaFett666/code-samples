package com.example.bootintegrator.service;

import org.springframework.integration.annotation.Gateway;

import com.example.bootintegrator.domain.Order;

public interface ShopGateway
{
	@Gateway(requestChannel="ordersChannel")
	public void placeOrder(Order pOrder);
}
