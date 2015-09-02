package com.example.bootintegrator.service;

import org.apache.log4j.Logger;

import com.example.bootintegrator.domain.Book;
import com.example.bootintegrator.domain.MusicCD;
import com.example.bootintegrator.domain.OrderItem;
import com.example.bootintegrator.domain.Software;

public class OrderItemRouter
{
	private static final Logger log = Logger.getLogger(OrderItemRouter.class);
	
	public String routeOrder(OrderItem pOrderItem)
	{
		log.debug("*** [OrderItemRouter] ****");
		
		String channel = "";
		if(isBook(pOrderItem)) {
			channel = "bookItemChannel";
		}
		else if(isMusic(pOrderItem)) {
			channel = "musicItemChannel";
		}
		else if(isSoftware(pOrderItem)) {
			channel = "softwareItemChannel";
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
