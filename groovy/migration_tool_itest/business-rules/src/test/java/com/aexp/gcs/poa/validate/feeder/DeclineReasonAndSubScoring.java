package com.aexp.gcs.poa.validate.feeder;

import javax.validation.constraints.NotNull;

public interface DeclineReasonAndSubScoring {
	@NotNull
	String getDeclineCodeMI();

	void setDeclineCodeMI(String declineCodeMI);

	@NotNull
	String getDeclineCodeOC();

	void setDeclineCodeOC(String declineCodeOC);
}
