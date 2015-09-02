package com.aexp.gcs.pod.tools.fieldextraction

import groovy.io.FileType
import groovy.json.JsonOutput

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class PodFieldsExtractor
{
	@Autowired
	AppSettings appSettings
	
	XmlSlurper xmlSlurper = new XmlSlurper()
	
	public void getAllPODFields()
	{
		def json = JsonOutput.toJson(this.processAllTemplates(this.retrievePODTemplateFiles()))
		String aJsonResult = JsonOutput.prettyPrint(json)
		
		println aJsonResult
		new File(this.appSettings.outputFile).withWriter{ it << aJsonResult }
	}
	
	protected Map processAllTemplates(List pTemplates)
	{
		Map aFieldMap = [:]
		
		pTemplates.each{ aTemplateRecord ->
			getTemplateFields(aTemplateRecord).each{ aTempField ->
				if (!aFieldMap[aTempField.name]) {
					aFieldMap[aTempField.name] = []
				}
				aFieldMap[aTempField.name] << aTempField.fieldData
			}
		}
		
		aFieldMap.sort{ k1, k2 -> k1.toString().toLowerCase() <=> k2.toString().toLowerCase()} 
	}
	
	protected List getTemplateFields(Map pTemplateRecord)
	{
		File aTemplateFile	= new File(pTemplateRecord.templatePath)
		def LetterInfo		= new XmlSlurper().parse(aTemplateFile)
		List aResult		= []

		LetterInfo.FieldGroup.Field.findAll{ it.name() == 'Field' }.each{ aField ->
			aResult	<<	[	name: aField.FieldNm.toString(), 
							fieldData: [
								feeder: pTemplateRecord.feeder,
								template: pTemplateRecord.templateId,
								location: pTemplateRecord.templatePath,
								validation: (aField.FieldVldtn.toString() ? aField.FieldVldtn.toString() : 'none'),
								mandatory: (aField.IsMandatory.toString() ? aField.IsMandatory.toString() : 'N'),
								DBColumn: (aField.DBColNm.toString() ? aField.DBColNm.toString() : '')
							]
						]
		}
		
		aResult
	}

	protected List retrievePODTemplateFiles()
	{
		File aSourceRoot = new File(this.appSettings.sourceFolder)
		List aResult		=	[]
		
		aSourceRoot.eachDirMatch(~/[A-Z]{3}/){ File aFeederDir ->
			aFeederDir.eachFileMatch(FileType.FILES, ~/.*?\.xml/) {File aTemplateFile ->
				String aTmpltName = aTemplateFile.getName()
				aResult << [
								templateId: aTmpltName.substring(0, aTmpltName.lastIndexOf('.')),
								templatePath: aTemplateFile.getAbsolutePath(),
								feeder:aFeederDir.getName()
							]
			}
		}
		
		aResult
	}
}
