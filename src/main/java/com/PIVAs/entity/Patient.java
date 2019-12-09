package com.PIVAs.entity;

/**
 * @author li_yinghao
 * @version 1.0
 * @date 2019/12/9 10:59
 * @description
 */
public class Patient {
    //病人id
    private Integer PatientID;
    //主页id
    private Integer homeID;
    //标识号
    private Integer typeID;
    //姓名
    private String name;
    //性别
    private String gender;
    //年龄
    private String age;
    //费别
    private String cosType;
    //床号
    private String bedNo;
    //病区id
    private Integer inpatientID;
    //科室id
    private Integer departmentID;

    public Integer getPatientID() {
        return PatientID;
    }

    public void setPatientID(Integer patientID) {
        PatientID = patientID;
    }

    public Integer getHomeID() {
        return homeID;
    }

    public void setHomeID(Integer homeID) {
        this.homeID = homeID;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBedNo() {
        return bedNo;
    }

    public void setBedNo(String bedNo) {
        this.bedNo = bedNo;
    }

    public Integer getInpatientID() {
        return inpatientID;
    }

    public void setInpatientID(Integer inpatientID) {
        this.inpatientID = inpatientID;
    }

    public Integer getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(Integer departmentID) {
        this.departmentID = departmentID;
    }

    public String getCosType() {
        return cosType;
    }

    public void setCosType(String cosType) {
        this.cosType = cosType;
    }

}
