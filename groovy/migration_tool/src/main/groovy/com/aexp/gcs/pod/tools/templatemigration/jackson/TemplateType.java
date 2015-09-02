package com.aexp.gcs.pod.tools.templatemigration.jackson;

import java.util.ArrayList;
import java.util.List;

public class TemplateType
{
	private String feeder;
	private String lob;
	private String template;
	private List<VariableType> variables;
	
	public TemplateType()
	{
		this.variables	=	new ArrayList<VariableType>();
	}
	
	public TemplateType(String pFeeder, String pLob, String pTemplate)
	{
		this.feeder		=	pFeeder;
		this.lob		=	pLob;
		this.template	=	pTemplate;
		this.variables	=	new ArrayList<VariableType>();
	}
	
	public String getFeeder()
	{
		return feeder;
	}
	public void setFeeder(String feeder)
	{
		this.feeder = feeder;
	}
	public String getLob()
	{
		return lob;
	}
	public void setLob(String lob)
	{
		this.lob = lob;
	}
	public String getTemplate()
	{
		return template;
	}
	public void setTemplate(String template)
	{
		this.template = template;
	}
	public List<VariableType> getVariables()
	{
		return variables;
	}
	public void setVariables(List<VariableType> variables)
	{
		this.variables = variables;
	}
	public void addVariable(VariableType pVariable)
	{
		this.variables.add(pVariable);
	}
}
