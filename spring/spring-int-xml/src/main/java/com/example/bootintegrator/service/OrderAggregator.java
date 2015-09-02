package com.example.bootintegrator.service;

import java.util.List;

import com.example.bootintegrator.domain.Order;
import com.example.bootintegrator.domain.OrderItem;

public class OrderAggregator
{

	public Order prepareDelivery(List<OrderItem> pOrderItems)
	{
		Order order = new Order();
		
		order.setOrderItems(pOrderItems);
		return order;
	}
}
