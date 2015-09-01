package com.victor.scrips

import groovy.io.FileType

String templatesFolder = 'C:/development/documents/90Templates_0522'

def xmlSlurper 		=	new XmlSlurper()
File aSourceRoot	=	new File(templatesFolder)

//aSourceRoot.eachDirMatch(~/[A-Z]{3}/){ File aFeederDir ->
aSourceRoot.eachDirMatch(~/BOL/){ File aFeederDir ->
	aFeederDir.eachFileMatch(FileType.FILES, ~/.*?\.xml/) { File aPodXmlFile ->
		println "\n${aPodXmlFile.absolutePath}"
		xmlSlurper.parse(aPodXmlFile).FieldGroup.Field
			.findAll{ it.IsMandatory == 'Y' || it.FieldFmt.text() }*.FieldNm.collect{ println it }
	}	
}

