package com.victor.examples

class CreateXmlGroovy
{
	static main(args)
	{
		def xml = new groovy.xml.MarkupBuilder()
		xml.langs(type:"current"){
		  language("Java")
		  language("Groovy")
		  language("JavaScript")
		}
	}
}
