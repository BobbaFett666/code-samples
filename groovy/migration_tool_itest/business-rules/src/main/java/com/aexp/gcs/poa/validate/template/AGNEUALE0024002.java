package com.aexp.gcs.poa.validate.template;

import javax.validation.constraints.NotNull;

public interface AGNEUALE0024002 {

	@NotNull
	String getTxnAcctNbr();
	@NotNull
	String getAlrtTransDay();
	@NotNull
	String getAlrtTransMnth();
	@NotNull
	String getAlrtTransDate();
	@NotNull
	String getAlrtTransYear();
	@NotNull
	String getSEName();
}
