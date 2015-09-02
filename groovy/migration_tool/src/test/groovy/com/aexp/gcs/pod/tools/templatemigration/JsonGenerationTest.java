package com.aexp.gcs.pod.tools.templatemigration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.aexp.gcs.pod.tools.templatemigration.jackson.TemplateType;
import com.aexp.gcs.pod.tools.templatemigration.jackson.VariableType;

public class JsonGenerationTest
{

	@Test
	public void generateJsonFromMapTest()
	{
		Map <String, Object> aMap = new HashMap<String, Object>();
		
		aMap.put("template", "ALEENALEOBC0002");
		aMap.put("feeder", "ALE");
		aMap.put("loa", "ALE");
		
		aMap.put("variables", new ArrayList<Map<String, Object>>());
		
		Map <String, Object> aVar1 = new HashMap<String, Object>();
		aVar1.put("mapping", "Envelope.Body.Request.DeliveryInformation.DestinationCode");
		aVar1.put("className", "com.aexp.gcs.schema.sendcorr.DeliveryInformationType");
		aVar1.put("validations", new ArrayList<Map<String, Object>>());
		
		Map <String, Object> aVa11 = new HashMap<String, Object>();
		aVa11.put("validator", "javax.validation.constraints.NotNull");
		
		Map <String, Object> aVa12 = new HashMap<String, Object>();
		aVa12.put("validator", "javax.validation.constraints.Size");
		aVa12.put("elements", new ArrayList<Map<String, Object>>());
		
		List<Map<String, Object>> val2Elems = (List<Map<String, Object>>)aVa12.get("elements");
		
		for (String aVarItem : Arrays.asList("min:1", "max:32")) {
			String[] t = aVarItem.split(":");
			Map<String, Object> elem = new HashMap<String, Object>();
			
			elem.put(t[0], t[1]);
			val2Elems.add(elem);
		}
		
		((List<Map<String, Object>>)aVar1.get("validations")).add(aVa11);
		((List<Map<String, Object>>)aVar1.get("validations")).add(aVa12);
		
		((List<Map<String, Object>>)aMap.get("variables")).add(aVar1);
		
		System.out.println(groovy.json.JsonOutput.prettyPrint(groovy.json.JsonOutput.toJson(aMap)));
	}
	
	
	@Test
	public void generateJsonFromClassesTest()
	{
		TemplateType aTempl	=	new TemplateType("ALE", "ALE", "ALEENALEOBC0002");
		
		aTempl.addVariable(new VariableType("Envelope.Body.Request.DeliveryInformation.DestinationCode", "com.aexp.gcs.schema.sendcorr.DeliveryInformationType"));
		aTempl.addVariable(new VariableType("Probably Dynamic", ""));
		

		String jsonString = groovy.json.JsonOutput.prettyPrint(groovy.json.JsonOutput.toJson(aTempl));
		/*
		 * To use Jackson, add dependency and replace previous line with:
		 * 		ObjectMapper aMapper =	new ObjectMapper()
		 * 		String jsonString 	=	aMapper.writeValueAsString(aTempl)
		 */
		
		
		System.out.println(jsonString);
	}
}
