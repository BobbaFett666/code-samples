package com.victor.scrips

import groovy.io.FileType

String sendCorr3Schema = 'C:/development/examples/groovy-scripts/src/SendCorrespondence3.4Schema.xsd'
String templatesFolder = 'C:/development/documents/90Templates/Templates'

def xmlSchema = new XmlSlurper()
						.parse(new File(sendCorr3Schema))
						.declareNamespace('xs':'http://www.w3.org/2001/XMLSchema')
File aSourceRoot	=	new File(templatesFolder)
Map aStatics 		=	[:]

aSourceRoot.eachDirMatch(~/[A-Z]{3}/){ File aFeederDir ->
	
	File aPoaFolder = new File("${aFeederDir.absolutePath}/POA")
	
	if (aPoaFolder.exists() && aPoaFolder.isDirectory()) {
		aPoaFolder.eachFileMatch(FileType.FILES, ~/.*?\.txt/) { File aTxtFile ->
			
			def aMatcher = aTxtFile.text =~ /(?m)^(.*)?:.*$/
			
			aMatcher.each { aMatch ->
				String aVariableName = aMatch[1]
				def aResult = xmlSchema.'xs:element'.find{ it.name() == 'element' && it.@name == aVariableName }
				
				if (aResult.size()) {
					if (!aStatics[aVariableName]) {
						aStatics[aVariableName] = []
					}
//					aStatics[aVariableName] << aTxtFile.getName().substring(0, aTxtFile.getName().lastIndexOf('.'))
					
					String aTempName = aTxtFile.getName().substring(0, aTxtFile.getName().lastIndexOf('.'))
					if (!aStatics[aVariableName].contains(aTempName))
						aStatics[aVariableName] << aTempName
				}
//					println "${aTxtFile.getName().substring(0, aTxtFile.getName().lastIndexOf('.'))} - ${aVariableName} - ${aResult.size() ? 'STATIC' : 'DYNAMIC'}"
			}
		}
	}
}

aStatics.each { String varName, List temps ->
	println "Static Variable: ${varName} (count: ${temps.size()})"
	temps.each { println "\t${it}"} 
	println ""
}
