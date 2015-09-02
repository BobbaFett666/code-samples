package com.aexp.gcs.poa.validate.feeder;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.aexp.gcs.poa.validate.template.ABCABCXY0001001;
import com.aexp.gcs.poa.validate.template.ABCABCXY0001002;
import com.aexp.gcs.poa.validate.template.ABCABCXY0001003;
import com.aexp.gcs.poa.validate.template.ABCABCXY0001004;
import com.aexp.gcs.poa.validate.template.ABCABCXY0001005;
import com.aexp.gcs.schema.sendcorr.Money;

public interface ABC {
	
	@NotNull(groups = ABCABCXY0001001.class, message="Variable Test is null")
	String getTest();
	
	void setTest(String Test);
	
	String getVariable1();
	
	void setVariable1(String variable);

	@NotNull(groups= ABCABCXY0001002.class, message="Expected Date is null")
	Date getExpectedDate();
	
	void setExpectedDate(Date ExpectedDate);
	
	@NotNull(groups= ABCABCXY0001003.class, message="Date is null")
	Date getDelieveryDate();
	
	void setDelieveryDate(Date DelieveryDate);
	
	@NotNull(groups = { ABCABCXY0001004.class, ABCABCXY0001005.class }, message = "ExpectedAmount is null")
	Money getExpectedAmount();
	
	void setExpectedAmount(Money ExpectedAmount);
	
	@NotNull(groups = { ABCABCXY0001004.class, ABCABCXY0001005.class }, message = "ExpectedAmount1 is null")
	Money getExpectedAmount1();
	
	void setExpectedAmount1(Money ExpectedAmount1);
	
	@NotNull(groups = { ABCABCXY0001004.class, ABCABCXY0001005.class }, message = "ExpectedAmount2 is null")
	Money getExpectedAmount2();
	
	void setExpectedAmount2(Money ExpectedAmount2);
	
	/**
	 * Test rule will use this to determine how many runtime exceptions to
	 * throw.
	 * 
	 * @return fail count
	 */
	BigDecimal getFailCount();

	void setFailCount(BigDecimal failCount);

}
