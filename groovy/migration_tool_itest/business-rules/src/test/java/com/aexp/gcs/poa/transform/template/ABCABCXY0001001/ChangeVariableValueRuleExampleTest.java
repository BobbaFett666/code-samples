package com.aexp.gcs.poa.transform.template.ABCABCXY0001001;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.aexp.gcs.poa.transform.AbstractTransformationTest;
import com.aexp.gcs.poa.validate.feeder.ABC;

/**
 * Junit for ChangeVariableValueRuleExample class.
 * 
 */
@RunWith(MockitoJUnitRunner.class)
public class ChangeVariableValueRuleExampleTest extends
		AbstractTransformationTest<ChangeVariableValueRuleExample, com.aexp.gcs.poa.validate.feeder.ABC> {

	private static final String VARIABLE1 = "value";

	@Test
	public void testExcecute() throws Exception {
		rule.execute(mockEnvelopeType);
		verify(interfase, times(1)).setVariable1(VARIABLE1 + "Changed");
	}

	@Override
	protected void wireInterfaceMockBehaviors(ABC interfase) {
		when(interfase.getVariable1()).thenReturn(VARIABLE1);
	}

}
