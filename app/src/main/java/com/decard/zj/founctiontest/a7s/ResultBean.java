package com.decard.zj.founctiontest.a7s;

public class ResultBean {


    /**
     * resultCode : 00
     * resultMessage : 签到成功
     * batchNo : 000003
     * sessionID : 7521A94047CDBC0978296ADF6BDDC244
     * doubMchntFlag : 3
     * singAmtLimit : 100000
     * keyLength : 32
     * algType : 0
     * macKey : 6AC46B6A13B17129E1A432535D29C1BC
     * macKeycheckValue : B7A452A6DDE5DB05
     * pik : 027A6095B6874D5DE19A10267EEE8C41
     * pikCheckValue : 62287EA56D4A5FE2
     * tdk : FDB620EB7046CD072221C0F71C504D46
     * tdkCheckValue : DA60AE56CFD52BDE
     */

    private String resultCode;
    private String resultMessage;
    private String batchNo;
    private String sessionID;
    private String doubMchntFlag;
    private String singAmtLimit;
    private String keyLength;
    private String algType;
    private String macKey;
    private String macKeycheckValue;
    private String pik;
    private String pikCheckValue;
    private String tdk;
    private String tdkCheckValue;

    @Override
    public String toString() {
        return "ResultBean{" +
                "resultCode='" + resultCode + '\'' +
                ", resultMessage='" + resultMessage + '\'' +
                ", batchNo='" + batchNo + '\'' +
                ", sessionID='" + sessionID + '\'' +
                ", doubMchntFlag='" + doubMchntFlag + '\'' +
                ", singAmtLimit='" + singAmtLimit + '\'' +
                ", keyLength='" + keyLength + '\'' +
                ", algType='" + algType + '\'' +
                ", macKey='" + macKey + '\'' +
                ", macKeycheckValue='" + macKeycheckValue + '\'' +
                ", pik='" + pik + '\'' +
                ", pikCheckValue='" + pikCheckValue + '\'' +
                ", tdk='" + tdk + '\'' +
                ", tdkCheckValue='" + tdkCheckValue + '\'' +
                '}';
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getDoubMchntFlag() {
        return doubMchntFlag;
    }

    public void setDoubMchntFlag(String doubMchntFlag) {
        this.doubMchntFlag = doubMchntFlag;
    }

    public String getSingAmtLimit() {
        return singAmtLimit;
    }

    public void setSingAmtLimit(String singAmtLimit) {
        this.singAmtLimit = singAmtLimit;
    }

    public String getKeyLength() {
        return keyLength;
    }

    public void setKeyLength(String keyLength) {
        this.keyLength = keyLength;
    }

    public String getAlgType() {
        return algType;
    }

    public void setAlgType(String algType) {
        this.algType = algType;
    }

    public String getMacKey() {
        return macKey;
    }

    public void setMacKey(String macKey) {
        this.macKey = macKey;
    }

    public String getMacKeycheckValue() {
        return macKeycheckValue;
    }

    public void setMacKeycheckValue(String macKeycheckValue) {
        this.macKeycheckValue = macKeycheckValue;
    }

    public String getPik() {
        return pik;
    }

    public void setPik(String pik) {
        this.pik = pik;
    }

    public String getPikCheckValue() {
        return pikCheckValue;
    }

    public void setPikCheckValue(String pikCheckValue) {
        this.pikCheckValue = pikCheckValue;
    }

    public String getTdk() {
        return tdk;
    }

    public void setTdk(String tdk) {
        this.tdk = tdk;
    }

    public String getTdkCheckValue() {
        return tdkCheckValue;
    }

    public void setTdkCheckValue(String tdkCheckValue) {
        this.tdkCheckValue = tdkCheckValue;
    }
}
