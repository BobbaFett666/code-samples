package com.example.bootintegrator.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public class Item implements Serializable
{
	private static final long serialVersionUID = -2082911254365129389L;
	
	protected String title;
	protected String publisher;
	protected BigDecimal price;
	protected int yearPublished;
	
	public Item()
	{
	}
	
	public Item(String pTitle, String pPublisher, BigDecimal pPrice, int pYear)
	{
		this.title		=	pTitle;
		this.publisher	=	pPublisher;
		this.price		=	round(pPrice);
		this.yearPublished	=	pYear;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public int getYearPublished() {
		return yearPublished;
	}
	public void setYearPublished(int yearPublished) {
		this.yearPublished = yearPublished;
	}
	
	private BigDecimal round(BigDecimal pValue)
	{
		return pValue.setScale(2, BigDecimal.ROUND_HALF_EVEN);
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result + ((publisher == null) ? 0 : publisher.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + yearPublished;
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		Item other = (Item) obj;
		
		if (price == null) {
			if (other.price != null) return false;
		} else if (!price.equals(other.price))
			return false;
		
		if (publisher == null) {
			if (other.publisher != null) return false;
		} else if (!publisher.equals(other.publisher))
			return false;
		
		if (title == null) {
			if (other.title != null) return false;
		} else if (!title.equals(other.title))
			return false;
		
		if (yearPublished != other.yearPublished)
			return false;
		
		return true;
	}
	
	@Override
	public String toString() {
		return "Item [title=" + title + ", publisher=" + publisher + ", price="
				+ price + ", yearPublished=" + yearPublished + "]";
	}

}
