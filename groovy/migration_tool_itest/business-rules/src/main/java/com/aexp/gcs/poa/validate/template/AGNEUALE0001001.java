package com.aexp.gcs.poa.validate.template;

import javax.validation.constraints.NotNull;

import com.aexp.gcs.poa.custom.constraint.DeprecatedPleaseMoveToFeeder;

public interface AGNEUALE0001001 {

	@DeprecatedPleaseMoveToFeeder
	@NotNull
    String getDateType();
    @NotNull
    String getAlrtPymtRcdAmt();
    @NotNull
    String getCardEndingDigits();
    @NotNull
    String getAlrtProdDesc();
    @DeprecatedPleaseMoveToFeeder
    String getPaymentType();
    @NotNull
    String getNfcLiftOff();
    @DeprecatedPleaseMoveToFeeder
    String getCSATSurveySwitch();
    @DeprecatedPleaseMoveToFeeder
    String getFooterYear();
    @DeprecatedPleaseMoveToFeeder
    String getDynamicURL();
    @DeprecatedPleaseMoveToFeeder
    String getOffrLink1();
    @DeprecatedPleaseMoveToFeeder
    String getOffrLink2();
    @DeprecatedPleaseMoveToFeeder
    String getPresURL();
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
    String getSurveyCode(); // value=vSxXNlEB

}
