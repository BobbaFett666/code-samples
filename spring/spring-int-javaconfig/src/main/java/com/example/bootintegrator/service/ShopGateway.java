package com.example.bootintegrator.service;

import org.springframework.integration.annotation.MessagingGateway;

import com.example.bootintegrator.domain.Order;

@MessagingGateway(name="shopGateway", defaultRequestChannel="ordersChannel")
public interface ShopGateway
{
	public void placeOrder(Order pOrder);
}
