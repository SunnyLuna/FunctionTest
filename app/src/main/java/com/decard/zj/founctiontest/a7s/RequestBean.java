package com.decard.zj.founctiontest.a7s;

public class RequestBean {

    public RequestBean(String merchantNo, String terminalNo, String sign) {
        this.merchantNo = merchantNo;
        this.terminalNo = terminalNo;
        this.sign = sign;
    }

    /**
     * merchantNo : 94734010742Z06Z
     * terminalNo : 01000209
     * sign : 391C4C80CDD851F791C890B74CAB18FB
     */



    private String merchantNo;
    private String terminalNo;
    private String sign;

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getTerminalNo() {
        return terminalNo;
    }

    public void setTerminalNo(String terminalNo) {
        this.terminalNo = terminalNo;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
