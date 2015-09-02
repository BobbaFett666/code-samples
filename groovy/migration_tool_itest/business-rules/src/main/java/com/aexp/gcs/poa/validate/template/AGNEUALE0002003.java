package com.aexp.gcs.poa.validate.template;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import com.aexp.gcs.poa.custom.constraint.DeprecatedPleaseMoveToFeeder;

public interface AGNEUALE0002003 {

	@NotNull
	@DeprecatedPleaseMoveToFeeder
	String getMinDueAmt();
	@NotNull
	String getPymtType();
	@NotNull
	@Digits(integer=5,fraction=0)
	String getCardEndingDigits();
	@NotNull
	String getAlrtPymtDueDate();
	@NotNull
	String getAlrtProdDesc();
	@DeprecatedPleaseMoveToFeeder
	String getAxiomkey();
	@DeprecatedPleaseMoveToFeeder
	String getProductStatus();
	@DeprecatedPleaseMoveToFeeder
    String getCSATSurveySwitch();
	@DeprecatedPleaseMoveToFeeder
    String getOffrLink1();
    @DeprecatedPleaseMoveToFeeder
    String getOffrLink2();
    @DeprecatedPleaseMoveToFeeder
    String getPresURL();
    @DeprecatedPleaseMoveToFeeder
    String getDynamicURL();
    @DeprecatedPleaseMoveToFeeder
    String getEtxTrkURL1();
    @DeprecatedPleaseMoveToFeeder
    String getEtxTrkURL2();
    @DeprecatedPleaseMoveToFeeder
    String getCrosssellStatus();
    @DeprecatedPleaseMoveToFeeder
    @NotNull
    String getPUKey();
    @DeprecatedPleaseMoveToFeeder
    String getReplaceURL(); // value=online.americanexpress.com
    @DeprecatedPleaseMoveToFeeder
    String getReplaceURL1(); // value=www.americanexpress.com
    @DeprecatedPleaseMoveToFeeder
    String getSurveyCode(); // value=PpiwYiuA
}
