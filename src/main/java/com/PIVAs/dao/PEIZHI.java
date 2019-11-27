package com.PIVAs.dao;

import java.math.BigDecimal;

public class PEIZHI {
    private Number XH;
    private String PATIENT_ID;
    private String VISIT_ID;
    private String ORDER_NO;
    private String USE_DATE;
    private String USE_TIME;
    private String SPID;
    private BigDecimal SHL_PEIZHI;
    private String SEQID;

    public Number getXH() {
        return XH;
    }

    public void setXH(Number XH) {
        this.XH = XH;
    }

    public String getPATIENT_ID() {
        return PATIENT_ID;
    }

    public void setPATIENT_ID(String PATIENT_ID) {
        this.PATIENT_ID = PATIENT_ID;
    }

    public String getVISIT_ID() {
        return VISIT_ID;
    }

    public void setVISIT_ID(String VISIT_ID) {
        this.VISIT_ID = VISIT_ID;
    }

    public String getORDER_NO() {
        return ORDER_NO;
    }

    public void setORDER_NO(String ORDER_NO) {
        this.ORDER_NO = ORDER_NO;
    }

    public String getUSE_DATE() {
        return USE_DATE;
    }

    public void setUSE_DATE(String USE_DATE) {
        this.USE_DATE = USE_DATE;
    }

    public String getUSE_TIME() {
        return USE_TIME;
    }

    public void setUSE_TIME(String USE_TIME) {
        this.USE_TIME = USE_TIME;
    }

    public String getSPID() {
        return SPID;
    }

    public void setSPID(String SPID) {
        this.SPID = SPID;
    }

    public BigDecimal getSHL_PEIZHI() {
        return SHL_PEIZHI;
    }

    public void setSHL_PEIZHI(BigDecimal SHL_PEIZHI) {
        this.SHL_PEIZHI = SHL_PEIZHI;
    }

    public String getSEQID() {
        return SEQID;
    }

    public void setSEQID(String SEQID) {
        this.SEQID = SEQID;
    }
}
