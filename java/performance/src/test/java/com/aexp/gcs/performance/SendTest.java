package com.aexp.gcs.performance;

import org.junit.Test;

public class SendTest {

	@Test
	public void testMain() throws Exception {
		int duration;
		
		//duration = 60 * 60 * 1000; 	// One Hour
		duration = 60 * 1000; 	// One Minute
		
		Send.main(new String[] { "--dir=src/test/resources/", "--rate=500", "--duration=" + duration,
//		"--ws.url=http://localhost:18080/gcs-poa-web/ws/SendCorrespondenceWSV4.0" });
		"--ws.url=http://LPDCLDWA00270.phx.aexp.com:8080/gcs-poa-web/ws/SendCorrespondenceWSV4.0" });
	}

}
