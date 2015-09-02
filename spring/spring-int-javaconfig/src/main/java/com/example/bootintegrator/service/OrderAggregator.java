package com.example.bootintegrator.service;

import java.util.List;

import org.springframework.integration.annotation.Aggregator;
import org.springframework.integration.annotation.MessageEndpoint;

import com.example.bootintegrator.domain.Order;
import com.example.bootintegrator.domain.OrderItem;

@MessageEndpoint
public class OrderAggregator
{

	@Aggregator(inputChannel="processedItemsChannel", outputChannel="deliveriesChannel")
	public Order prepareDelivery(List<OrderItem> pOrderItems)
	{
		Order order = new Order();
		
		order.setOrderItems(pOrderItems);
		return order;
	}
}
