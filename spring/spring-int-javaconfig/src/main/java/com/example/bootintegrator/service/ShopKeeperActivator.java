package com.example.bootintegrator.service;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import com.example.bootintegrator.domain.Book;
import com.example.bootintegrator.domain.MusicCD;
import com.example.bootintegrator.domain.OrderItem;
import com.example.bootintegrator.domain.Software;

@Component
public class ShopKeeperActivator
{
	private static final Logger log = Logger.getLogger(ShopKeeperActivator.class);
	
	private static final BigDecimal BOOK_DISCOUNT = new BigDecimal(0.05);
	private static final BigDecimal MUSIC_DISCOUNT = new BigDecimal(0.10);
	private static final BigDecimal SOFTWARE_DISCOUNT = new BigDecimal(0.15);

	@ServiceActivator(inputChannel="bookItemChannel", outputChannel="processedItemsChannel")
	public OrderItem processBooks(OrderItem pBookOrderItem)
	{ 
		log.debug("*** [ShopKeeperActivator] processing book : "+ pBookOrderItem.getItem().getTitle() +" ****");
		
		final BigDecimal finalPrice = this.calculateDiscountedPrice(pBookOrderItem, BOOK_DISCOUNT);
		pBookOrderItem.setDiscountedPrice(finalPrice);
		return pBookOrderItem;
	}
	
	@ServiceActivator(inputChannel="musicItemsChannel", outputChannel="processedItemsChannel")
	public OrderItem processMusicCDs(OrderItem pMusicCDOrderItem)
	{ 
		log.debug("*** [ShopKeeperActivator] processing music CD : "+ pMusicCDOrderItem.getItem().getTitle() +" ****");
		
		final BigDecimal finalPrice = this.calculateDiscountedPrice(pMusicCDOrderItem, MUSIC_DISCOUNT);
		pMusicCDOrderItem.setDiscountedPrice(finalPrice);
		return pMusicCDOrderItem;
	}
	
	@ServiceActivator(inputChannel="softwareItemsChannel", outputChannel="processedItemsChannel")
	public OrderItem processSoftware(OrderItem pSoftwareOrderItem)
	{ 
		log.debug("*** [ShopKeeperActivator] processing software : "+ pSoftwareOrderItem.getItem().getTitle() +" ****");
		
		final BigDecimal finalPrice = this.calculateDiscountedPrice(pSoftwareOrderItem, SOFTWARE_DISCOUNT);
		pSoftwareOrderItem.setDiscountedPrice(finalPrice);
		return pSoftwareOrderItem;
	}
	
	private BigDecimal calculateDiscountedPrice(final OrderItem orderItem, final BigDecimal discount) {

		final BigDecimal discountedPrice = this.round(orderItem.getTotalPrice().multiply(discount));
		final BigDecimal finalPrice = this.round(orderItem.getTotalPrice().subtract(discountedPrice));

		log.debug("item (" + this.getItemType(orderItem) + ") " +  
				"item price: " + orderItem.getItem().getPrice() +
				" discount: " + discountedPrice +
				" final price: " + finalPrice);

		return finalPrice;
	}
	
	private BigDecimal round(final BigDecimal value) {
		return value.setScale(2, BigDecimal.ROUND_HALF_EVEN);
	}
	
	private String getItemType(final OrderItem orderItem) {

		String type = "";
		
		if(orderItem.getItem() instanceof Book) {
			type = "Book: " + orderItem.getItem().getTitle(); 
		}
		else if(orderItem.getItem() instanceof MusicCD) {
			type = "MusicCD: " + orderItem.getItem().getTitle(); 			
		}
		else if(orderItem.getItem() instanceof Software) {
			type = "Software: " + orderItem.getItem().getTitle(); 			
		}

		return type;
	}
}
