package com.aexp.gcs.poa.transform.template.AGNEUBBK0002003;

import com.aexp.gcs.poa.transform.feeder.BBK.DynamicSubjectAndFields;
import com.aexp.gcs.schema.sendcorr.EnvelopeType;

public class ImportantNoticeYourStatement extends AGNEUBBK0002003 {

	@Override
	public void execute(EnvelopeType envelope) {
		DynamicSubjectAndFields.add(envelope, "Important Notice: Your ", " Statement");
	}

}
