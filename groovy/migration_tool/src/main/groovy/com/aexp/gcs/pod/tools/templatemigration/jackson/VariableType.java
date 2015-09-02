package com.aexp.gcs.pod.tools.templatemigration.jackson;

public class VariableType
{
	private String mapping;
	private String className;
	
	public VariableType()
	{
		//	Default constructor
	}
	
	public VariableType(String pMapping, String pClassName)
	{
		this.mapping	=	pMapping;
		this.className	=	pClassName;
	}

	public String getMapping()
	{
		return this.mapping;
	}

	public void setMapping(String pMapping)
	{
		this.mapping = pMapping;
	}

	public String getClassName()
	{
		return this.className;
	}

	public void setClassName(String pClassName)
	{
		this.className = pClassName;
	}
	
}
