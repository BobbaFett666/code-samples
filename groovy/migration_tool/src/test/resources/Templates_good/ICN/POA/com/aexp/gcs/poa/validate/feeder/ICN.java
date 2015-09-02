
package com.aexp.gcs.poa.validate.feeder;



/**
 *  This Interface will contain field level validation for different templates
 * 
 */
public interface ICN {


    public String getRSVPExpDt();

    public void setRSVPExpDt(String RSVPExpDt);

    public String getImage();

    public void setImage(String Image);

    public String getRSVPCd();

    public void setRSVPCd(String RSVPCd);

    public String getCMName();

    public void setCMName(String CMName);

    public String getCardEndingDigits();

    public void setCardEndingDigits(String CardEndingDigits);

    public String getTotDue();

    public void setTotDue(String TotDue);

    public String getCommunRtrvId();

    public void setCommunRtrvId(String CommunRtrvId);

}
