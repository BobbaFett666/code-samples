package com.aexp.gcs.poa.validate.feeder;

import java.util.List;

import javax.validation.constraints.Size;

import com.aexp.gcs.validator.DynamicGroup;

public interface ConditionCodesDeclineReasonAndSubScoring extends
		DynamicGroup<DeclineReasonAndSubScoring> {
	// TODO figure out why this is being ignored, it's only seeing the parent
	// for now just use the constraints.xml to configure
	@Size(min = 1, max = 1)
	List<DeclineReasonAndSubScoring> getVariables();
}
