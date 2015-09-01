package com.victor.examples

import groovy.xml.MarkupBuilder

/*	Example 1 - Creating elements	*/

//def xmlWriter = new StringWriter()
//def xmlMarkup = new MarkupBuilder(xmlWriter)
//
//xmlMarkup.movie("The Godfather")
//
//println xmlWriter.toString()

/*	Example 2 - Creating XML	*/

def writer = new StringWriter()
def xml = new MarkupBuilder(writer)

xml.records() {
	car(name:'HSV Maloo', make:'Holden', year:2006) {
		country('Australia')
		record(type:'speed', 'Production Pickup Truck with speed of 271kph')
	}
	car(name:'Royale', make:'Bugatti', year:1931) {
		country('France')
		record(type:'price', 'Most Valuable Car at $15 million')
	}
	automobile(name: 'custom', "some custom car")
}

//def records = new XmlSlurper().parseText(writer.toString())

//assert records.car.first().name.text() == 'HSV Maloo'
//assert records.car.last().name.text() == 'Royale'

println writer.toString()