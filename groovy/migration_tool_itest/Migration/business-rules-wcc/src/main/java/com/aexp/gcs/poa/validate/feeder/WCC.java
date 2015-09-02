
package com.aexp.gcs.poa.validate.feeder;



/**
 *  This Interface will contain field level validation for different templates
 * 
 */
public interface WCC {


    public String getTotalExpRcpBalAmt();

    public void setTotalExpRcpBalAmt(String TotalExpRcpBalAmt);

    public String getEnvironment();

    public void setEnvironment(String Environment);

    public String getOffrLink1();

    public void setOffrLink1(String OffrLink1);

    public String getNgopenAmt();

    public void setNgopenAmt(String NgopenAmt);

    public String getTodayDate();

    public void setTodayDate(String TodayDate);

    public String getCommunRcpntEmailAddrTxt();

    public void setCommunRcpntEmailAddrTxt(String CommunRcpntEmailAddrTxt);

    public String getOffrLink2();

    public void setOffrLink2(String pOffrLink2);

    public String getImage();

    public void setImage(String pImage);

    public String getPresURL();

    public void setPresURL(String pPresURL);

    public String getCMAccountNumber();

    public void setCMAccountNumber(String pCMAccountNumber);

    public String getCommunRtrvId();

    public void setCommunRtrvId(String pCommunRtrvId);

}