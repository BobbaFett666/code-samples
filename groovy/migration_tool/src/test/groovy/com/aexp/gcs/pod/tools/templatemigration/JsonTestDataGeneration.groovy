package com.aexp.gcs.pod.tools.templatemigration

import groovy.json.JsonOutput
import spock.lang.Specification

class JsonTestDataGeneration extends Specification
{
	/*
	 The following are not mapped:
	 	CommunDestLocCd--Envelope.Body.Request.DeliveryInformation.DestinationCode--Default Value :SROC
	 	"If feeder do not pass the value in input for that field then it would take the default value. 
	 	Default value should be in transaction info xml. I don’t think we need a rule / validation for this." - Kapil's Email 06/02/2015
 
		TODAY--Probably Dynamic--com.americanexpress.commutil.validator.TodayValidator -- Formatter -- CMPLTDATE    
		"CMPLDATE" does not map to a valid valid class name
	*/
	def "Generate JSON test data for BOL - AGNEUBOL0030001"() 
	{
		given:
			Map m = [template: 'AGNEUBOL0030001', feeder: 'BOL', lob: 'BOL',
					variables:[
							[   mapping: 'Envelope.Body.Request.DeliveryInformation.DestinationCode',
								className: 'com.aexp.gcs.schema.sendcorr.DeliveryInformationType',
								validations: [
									[validator: 'javax.validation.constraints.NotNull'],
									[validator: 'javax.validation.constraints.Size',
									 elements: [
										 max: 4
									 ]
									]
								]
							],
							
							[   mapping: 'Envelope.Body.Request.CommunicationInformation.RequestDate',
								className: 'com.aexp.gcs.schema.sendcorr.CommunicationInformationType',
								"business-rules": [
									[rule: 'com.americanexpress.commutil.formatter.EffDateFormatter']
								]
							],
							
							[
								mapping: 'Envelope.Body.Request.CustomerPII.PreferredLanguageCode',
								className: 'com.aexp.gcs.schema.sendcorr.CustomerPIIType',
								validations: [
									[validator: 'com.americanexpress.commutil.validator.LanguageValidator']
								]
							],
							
							[
								mapping: 'Envelope.Body.Request.CustomerPII.RecipientContactDetails.ContactDetails.NameDetails.FullName',
								className: 'com.aexp.gcs.schema.sendcorr.NameDetailsType',
								validations: [
									[validator: 'javax.validation.constraints.NotNull']
								],
								"business-rules": [
									[rule: 'com.americanexpress.commutil.formatter.CustNameFormatter']
								]
							],
							
							[
								mapping: 'CommunRcpntAddrLineTxt1--Probably Dynamic',
								validations: [
									[validator: 'javax.validation.constraints.NotNull'],
									[validator: 'javax.validation.constraints.Size', elements: [max: 40]]
								],
								"business-rules": [
									[rule: 'com.americanexpress.commutil.formatter.ScottishIrishFormatter']
								]
							],
							
							[
								mapping: 'CommunRcpntAddrLineTxt2--Probably Dynamic',
								"business-rules": [
									[rule: 'com.americanexpress.commutil.formatter.ScottishIrishFormatter']
								]
							],
							
							[
								mapping: 'CommunRcpntAddrLineTxt3--Probably Dynamic',
								"business-rules": [
									[rule: 'com.americanexpress.commutil.formatter.ScottishIrishFormatter']
								]
							],
							
							[
								mapping: 'Envelope.Body.Request.CustomerPII.RecipientContactDetails.ContactDetails.PhysicalAddress.City',
								className: 'com.aexp.gcs.schema.sendcorr.PhysicalAddressType',
								validations: [
									[validator: 'javax.validation.constraints.NotNull'],
									[validator: 'javax.validation.constraints.Size', elements: [max: 20]]
								],
								"business-rules": [
									[rule: 'com.americanexpress.commutil.formatter.ScottishIrishFormatter']
								]
							],
							 
							[
								mapping: 'Envelope.Body.Request.CustomerPII.RecipientContactDetails.ContactDetails.PhysicalAddress.StateCode',
								className: 'com.aexp.gcs.schema.sendcorr.PhysicalAddressType',
								validations: [
									[validator: 'javax.validation.constraints.Size', elements: [max: 3]]
								],
								"business-rules": [
									[rule: 'com.americanexpress.commutil.formatter.UpperCaseFormatter']
								]
							],
							 
							[
								mapping: 'Envelope.Body.Request.CustomerPII.RecipientContactDetails.ContactDetails.PhysicalAddress.PostalCode',
								className: 'com.aexp.gcs.schema.sendcorr.PhysicalAddressType',
								validations: [
									[validator: 'javax.validation.constraints.Size', elements: [max: 20]]
								]
							],
							 
							[
								mapping: 'Envelope.Body.Request.CustomerPII.RecipientContactDetails.ContactDetails.PhysicalAddress.CountryCode',
								className: 'com.aexp.gcs.schema.sendcorr.PhysicalAddressType',
								"business-rules": [
									[rule: 'com.americanexpress.commutil.formatter.UpperCaseFormatter']
								]
							],
							 
							[
								mapping: 'RelationShipId--Probably Dynamic',
								validations: [
									[validator: 'javax.validation.constraints.NotNull'],
									[validator: 'com.americanexpress.commutil.validator.RelationshipIdValidator']
								],
								"business-rules": [
									[rule: 'com.americanexpress.commutil.formatter.ECRAccountEndingDigitsFormatter']
								]
							],
							
							[
								mapping: 'TODAY--Probably Dynamic',
								validations: [
									[validator: 'com.americanexpress.commutil.validator.TodayValidator']
								]
							],
							
							[
								mapping: 'Envelope.Body.Request.CommunicationInformation.CommunicationTrackingID',
								className: 'com.aexp.gcs.schema.sendcorr.CommunicationInformationType',
								validations: [
									[validator: 'javax.validation.constraints.NotNull']
								]
							]
						]
					]
		
		expect:
			println JsonOutput.prettyPrint(JsonOutput.toJson(m))
	}
}
