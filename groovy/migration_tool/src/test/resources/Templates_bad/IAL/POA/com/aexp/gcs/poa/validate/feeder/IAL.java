
package com.aexp.gcs.poa.validate.feeder;



/**
 *  This Interface will contain field level validation for different templates
 * 
 */
public interface IAL {


    public String getRelationShipId();

    public void setRelationShipId(String RelationShipId);

    public String getImage();

    public void setImage(String Image);

    public String getAlrtPymtRcdAmt();

    public void setAlrtPymtRcdAmt(String AlrtPymtRcdAmt);

    public String getTodayDate();

    public void setTodayDate(String TodayDate);

    public String getCommunRcpntEmailAddrTxt();

    public void setCommunRcpntEmailAddrTxt(String CommunRcpntEmailAddrTxt);

    public String getCardEndingDigits();

    public void setCardEndingDigits(String CardEndingDigits);

    public String getAlrtPymtRcdDt();

    public void setAlrtPymtRcdDt(String AlrtPymtRcdDt);

    public String getExpiryDt();

    public void setExpiryDt(String ExpiryDt);

    public String getCardUrl();

    public void setCardUrl(String CardUrl);

    public String getCommunRtrvId();

    public void setCommunRtrvId(String CommunRtrvId);

}
