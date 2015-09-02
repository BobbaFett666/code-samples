package com.example.bootintegrator.service;

import org.springframework.integration.splitter.AbstractMessageSplitter;
import org.springframework.messaging.Message;

import com.example.bootintegrator.domain.Order;

public class OrderSplitter extends AbstractMessageSplitter
{
	@Override
	protected Object splitMessage(Message<?> pOrderMsg)
	{
		return ((Order)pOrderMsg.getPayload()).getOrderItems();
	}
}
