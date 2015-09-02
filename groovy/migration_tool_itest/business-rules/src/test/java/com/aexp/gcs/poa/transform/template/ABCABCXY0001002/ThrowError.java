package com.aexp.gcs.poa.transform.template.ABCABCXY0001002;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aexp.gcs.poa.validate.feeder.ABC;
import com.aexp.gcs.schema.sendcorr.EnvelopeType;

public class ThrowError extends ABCABCXY0001002 {

	private static final Logger LOG = LoggerFactory.getLogger(ThrowError.class);
	private final Map<String, BigDecimal> failCount = new HashMap<String, BigDecimal>();

	@Override
	public void execute(EnvelopeType envelope) {
		LOG.warn("Starting to execute rule ThrowError, map=" + failCount);
		ABC dto = getCommunicationVariablesDto(envelope, ABC.class);
		if (dto != null && dto.getFailCount() != null) {
			String key = getCommunicationTrackingID(envelope);
			BigDecimal current = failCount.get(key);
			if (current == null) {
				current = BigDecimal.ZERO;
				failCount.put(key, current);
			}
			if (current.compareTo(dto.getFailCount()) < 0) {
				current = current.add(BigDecimal.ONE);
				failCount.put(key, current);
				throw new RuntimeException("Failed based on request data.");
			}
		}
	}

}
