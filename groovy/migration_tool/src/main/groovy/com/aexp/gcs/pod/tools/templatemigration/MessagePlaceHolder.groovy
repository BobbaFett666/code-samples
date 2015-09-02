package com.aexp.gcs.pod.tools.templatemigration

import groovy.io.FileType
import java.util.regex.Matcher
import java.util.regex.Pattern

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class MessagePlaceHolder {

	@Autowired
	AppSettings appSettings
	

	/**
	 * @param pPlaceholder
	 * @return
	 */
	public boolean placeholderExists(String pPlaceholder)
	{
		//	In practice we might only need to load the text once and reload on every addition we make to the properties file
		String aFileText	=	new File("${appSettings.validationMessagesFile}").text
		String aStrPattern	=	"^" + pPlaceholder + "\\s*?=.*\$"
		Pattern aRegex		=	Pattern.compile(aStrPattern, Pattern.MULTILINE)
		Matcher aMatcher	=	aRegex.matcher(aFileText)
		
		aMatcher.size() > 0
	}
	
	/**
	 * @param pPlaceholder
	 */
	public void addPlaceholder(String pPlaceholder)
	{
		if (!placeholderExists(pPlaceholder)) {
			new File("${appSettings.validationMessagesFile}").append("\n${pPlaceholder} = ")
		}
	}
}
