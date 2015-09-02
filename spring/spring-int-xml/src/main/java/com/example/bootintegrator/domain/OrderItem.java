package com.example.bootintegrator.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderItem implements Serializable
{
	private static final long serialVersionUID = 224927729109230738L;

	private BigDecimal discountedPrice;
	private Item item;
	private int count;
	
	public OrderItem(){
		
	}
	public OrderItem(Item item) {
		this.item = item;
		count = 1;
	}
	
	public BigDecimal getDiscountedPrice() {
		return discountedPrice;
	}
	public void setDiscountedPrice(BigDecimal discountedPrice) {
		this.discountedPrice = discountedPrice;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	public void incrementQuantity() {
		count++;
	}
	
	public void decrementQuantity() {
		count--;
	}
	public boolean isLastOne() {
		return count == 1;
	}
	
	public BigDecimal getTotalPrice() {
		return item.getPrice().multiply(new BigDecimal(count));
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((item == null) ? 0 : item.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderItem other = (OrderItem) obj;
		if (item == null) {
			if (other.item != null)
				return false;
		} else if (!item.equals(other.item))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "\n OrderItem [item=" + item + ", count=" + count + " total cost=" + getTotalPrice() +  "]";
	}
}
