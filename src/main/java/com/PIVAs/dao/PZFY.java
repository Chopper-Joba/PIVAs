package com.PIVAs.dao;

import java.math.BigDecimal;
import java.util.Date;


public class PZFY {
    private int ID;
    private String PATIENT_ID;
    private String VISIT_ID;
    private String ORDER_ID;
    private String Department_no;
    private String Item_no;
    private String DevNo;
    private int Num;
    private BigDecimal Costs;
    private String DEMO;
    private Date Createtime;
    private String SEQID;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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

    public String getORDER_ID() {
        return ORDER_ID;
    }

    public void setORDER_ID(String ORDER_ID) {
        this.ORDER_ID = ORDER_ID;
    }

    public String getDepartment_no() {
        return Department_no;
    }

    public void setDepartment_no(String department_no) {
        Department_no = department_no;
    }

    public String getItem_no() {
        return Item_no;
    }

    public void setItem_no(String item_no) {
        Item_no = item_no;
    }

    public String getDevNo() {
        return DevNo;
    }

    public void setDevNo(String devNo) {
        DevNo = devNo;
    }

    public int getNum() {
        return Num;
    }

    public void setNum(int num) {
        Num = num;
    }

    public BigDecimal getCosts() {
        return Costs;
    }

    public void setCosts(BigDecimal costs) {
        Costs = costs;
    }

    public String getDEMO() {
        return DEMO;
    }

    public void setDEMO(String DEMO) {
        this.DEMO = DEMO;
    }

    public Date getCreatetime() {
        return Createtime;
    }

    public void setCreatetime(Date createtime) {
        Createtime = createtime;
    }

    public String getSEQID() {
        return SEQID;
    }

    public void setSEQID(String SEQID) {
        this.SEQID = SEQID;
    }

    @Override
    public String toString() {
        return "PZFY{" +
                "ID=" + ID +
                ", PATIENT_ID='" + PATIENT_ID + '\'' +
                ", VISIT_ID='" + VISIT_ID + '\'' +
                ", ORDER_ID='" + ORDER_ID + '\'' +
                ", Department_no='" + Department_no + '\'' +
                ", Item_no='" + Item_no + '\'' +
                ", DevNo='" + DevNo + '\'' +
                ", Num=" + Num +
                ", Costs=" + Costs +
                ", DEMO='" + DEMO + '\'' +
                ", Createtime=" + Createtime +
                ", SEQID='" + SEQID + '\'' +
                '}';
    }
}
