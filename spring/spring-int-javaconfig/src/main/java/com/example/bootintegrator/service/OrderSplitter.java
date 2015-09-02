package com.example.bootintegrator.service;

import java.util.List;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Splitter;
import org.springframework.messaging.Message;

import com.example.bootintegrator.domain.Order;
import com.example.bootintegrator.domain.OrderItem;

@MessageEndpoint
public class OrderSplitter
{
	@Splitter(inputChannel="ordersChannel", outputChannel="orderItemsChannel")
	public List<OrderItem> splitOrderMessage(Message<Order> pOrderMsg)
	{
		return ((Order)pOrderMsg.getPayload()).getOrderItems();
	}
}
