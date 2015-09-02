package com.example.bootintegrator.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.bootintegrator.domain.Book;
import com.example.bootintegrator.domain.MusicCD;
import com.example.bootintegrator.domain.Order;
import com.example.bootintegrator.domain.OrderItem;
import com.example.bootintegrator.domain.Software;
import com.example.bootintegrator.service.ShopGateway;

@RestController
public class DefaultController
{

	@Autowired
	private ShopGateway shopGateway;
	
	@RequestMapping(value="index", method=RequestMethod.GET)
	public String index()
	{
		Order anOrder = this.createOrder();
		
		this.shopGateway.placeOrder(anOrder);
		
		return anOrder.toString();
	}
	
	private Order createOrder()
	{
		Book book 		= new Book("title", "publisher", new BigDecimal("10.1254567"), 1988, "Sigmund Freud");
		MusicCD cd 		= new MusicCD("Off the Wall", "publisher", new BigDecimal("5.00"), 1975, "Michael Jackson");
		Software macos	= new Software("Mavericks", "publisher", new BigDecimal("110"), 2014, "10.6.1");
		
		OrderItem bookItems = new OrderItem(book);
		bookItems.incrementQuantity();
		
		OrderItem cdItems = new OrderItem(cd);
		cdItems.incrementQuantity();
		cdItems.incrementQuantity();
		
		OrderItem swItems = new OrderItem(macos);
		
		List<OrderItem> orderItems = new ArrayList<OrderItem>();
		orderItems.add(bookItems);
		orderItems.add(cdItems);
		orderItems.add(swItems);
		
		Order order = new Order();
		order.setOrderItems(orderItems);
//		System.out.println("Order: " + order);
//		System.out.println("Total : "+ order.getTotalCost());
		
		return order;
		
	}
	
}
