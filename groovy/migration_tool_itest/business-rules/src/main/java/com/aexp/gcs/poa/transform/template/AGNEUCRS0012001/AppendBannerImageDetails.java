package com.aexp.gcs.poa.transform.template.AGNEUCRS0012001;

import org.springframework.beans.factory.annotation.Autowired;

import com.aexp.gcs.poa.custom.transform.BannerImage;
import com.aexp.gcs.schema.sendcorr.EnvelopeType;

public class AppendBannerImageDetails extends AGNEUCRS0012001 {

	@Autowired
	BannerImage bannerImage;

	@Override
	public void execute(EnvelopeType envelope) {
		bannerImage.appendBannerImageDetails(envelope, "IT", "CONSUMER");
	}

}
