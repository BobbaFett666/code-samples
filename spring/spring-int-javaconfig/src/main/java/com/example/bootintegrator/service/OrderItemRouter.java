package com.example.bootintegrator.service;

import org.apache.log4j.Logger;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Router;

import com.example.bootintegrator.domain.Book;
import com.example.bootintegrator.domain.MusicCD;
import com.example.bootintegrator.domain.OrderItem;
import com.example.bootintegrator.domain.Software;

@MessageEndpoint
public class OrderItemRouter
{
	private static final Logger log = Logger.getLogger(OrderItemRouter.class);
	
	@Router(inputChannel="orderItemsChannel")
	public String routeOrder(OrderItem pOrderItem)
	{
		log.debug("*** [OrderItemRouter] ****");
		
		String channel = "";
		if(isBook(pOrderItem)) {
			channel = "bookItemsChannel";
		}
		else if(isMusic(pOrderItem)) {
			channel = "musicItemsChannel";
		}
		else if(isSoftware(pOrderItem)) {
			channel = "softwareItemsChannel";
		}

		log.debug("*** [OrderItemRouter] sending item : " + pOrderItem.getItem().getTitle() + " to "+ channel +  " ****");

		return channel;
	}
	
	private Boolean isBook(OrderItem orderItem) {
		return orderItem.getItem() instanceof Book;			
	}
	private Boolean isMusic(OrderItem orderItem) {
		return orderItem.getItem() instanceof MusicCD;			
	}
	private Boolean isSoftware(OrderItem orderItem) {
		return orderItem.getItem() instanceof Software;			
	}
}
