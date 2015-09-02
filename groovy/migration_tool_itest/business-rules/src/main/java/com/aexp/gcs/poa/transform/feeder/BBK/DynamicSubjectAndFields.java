package com.aexp.gcs.poa.transform.feeder.BBK;

import java.util.List;

import com.aexp.gcs.poa.transform.AbstractTransformation;
import com.aexp.gcs.schema.sendcorr.EnvelopeType;
import com.aexp.gcs.schema.sendcorr.VariableType;

public class DynamicSubjectAndFields {
	public static void add(EnvelopeType envelope, String prefix, String suffix) {
		List<VariableType> vars = AbstractTransformation.getDynamicVariables(envelope);
		if (vars != null) {
			String month = null, year = null, strEstmtDueDateLit = null, variableInd = null;
			for (VariableType var : vars) {
				if ("EstmtStatementMonth".equals(var.getVariableName()))
					month = (String) var.getVariableValue();
				else if ("EstmtStatementYear".equals(var.getVariableName()))
					year = (String) var.getVariableValue();
				else if ("EstmtDueDateLit".equals(var.getVariableName()))
					strEstmtDueDateLit = (String) var.getVariableValue();
			}

			String dynamicSubject = prefix + month + " " + year + suffix;
			VariableType dynamicSubjectLine = new VariableType();
			dynamicSubjectLine.setVariableName("EmailSubject");
			dynamicSubjectLine.setVariableValue(dynamicSubject);
			vars.add(dynamicSubjectLine);

			VariableType JulianDate = new VariableType();
			JulianDate.setVariableName("JulianDate");
			JulianDate.setVariableValue(month + " " + year);
			vars.add(JulianDate);

			if (strEstmtDueDateLit != null && !strEstmtDueDateLit.trim().substring(0, 13).equalsIgnoreCase("")) {
				if (strEstmtDueDateLit.trim().substring(0, 13).equalsIgnoreCase("No payment is")) {
					variableInd = "3";
				} else if (strEstmtDueDateLit.substring(0, 13).trim().equalsIgnoreCase("Please pay im")) {
					variableInd = "4";
				} else if (strEstmtDueDateLit.substring(0, 13).trim().equalsIgnoreCase("Please pay up")) {
					variableInd = "5";
				} else if (strEstmtDueDateLit.substring(0, 13).equalsIgnoreCase("Your payment ")) {
					variableInd = "1";
				} else if (strEstmtDueDateLit.substring(0, 13).trim().equalsIgnoreCase("Please pay by")) {
					variableInd = "2";
				}
				VariableType VariableInd = new VariableType();
				VariableInd.setVariableName("VariableInd");
				VariableInd.setVariableValue(variableInd);
				vars.add(VariableInd);
			}
		}
	}
}
