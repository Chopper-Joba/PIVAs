package com.PIVAs.webservice;

import com.PIVAs.getdata.*;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.springframework.stereotype.Component;
import javax.jws.WebService;


@WebService(serviceName = "webservice",targetNamespace = "http://webservice.PIVAs.com")
@Component
public class webServiceImpl implements webService {
    //药品基本信息接口
    @Override
    public String GET_JMPZ_DRUG_DICT(String xml) {
        GET_JMPZ_DRUG_DICT dict=new GET_JMPZ_DRUG_DICT();
        String responseXML="";
        Document document=null;
        try{
            document= DocumentHelper.parseText(xml);
        }catch (DocumentException e){
            return e.getMessage();
        }
        responseXML=dict.GET_JMPZ_DRUG_DICT(document);
        return responseXML;
    }
    //药品属性信息接口
    @Override
    public String GET_JMPZ_DRUG_CLASS(String xml) {
        GET_JMPZ_DRUG_CLASS drugClass =new GET_JMPZ_DRUG_CLASS();
        String responseXML="";
        Document document=null;
        try{
            document= DocumentHelper.parseText(xml);
        }catch (DocumentException e){
            return e.getMessage();
        }
        responseXML=drugClass.GET_JMPZ_DRUG_CLASS(document);
        return responseXML;
    }
    ///* 医生基本信息接口*/
    @Override
    public String GTET_JMPZ_DOCTOR(String xml) {
        GTET_JMPZ_DOCTOR doctor=new GTET_JMPZ_DOCTOR();
        String responseXML="";
        Document document=null;
        try{
            document= DocumentHelper.parseText(xml);
        }catch (DocumentException e){
            return e.getMessage();
        }
        responseXML=doctor.getDoctor(document);
        return responseXML;
    }
    //在院病人基本信息接口
    @Override
    public String GET_JMPZ_PATIENT_IN(String xml) {
        GET_JMPZ_PATIENT_IN patientInHospital =new GET_JMPZ_PATIENT_IN();
        String reponseXML="";
        Document document=null;
        try{
            document=DocumentHelper.parseText(xml);
        }catch (DocumentException e){
            return e.getMessage();
        }
        reponseXML=patientInHospital.GET_JMPZ_PATIENT_IN(document);
        return reponseXML;
    }

    @Override
//    科室基本信息接口
    public String GET_JMPZ_DEPARTMENT(String xml) {
        GET_JMPZ_DEPARTMENT department= new GET_JMPZ_DEPARTMENT();
        String responseXML="";
        Document document=null;
        try{
            document= DocumentHelper.parseText(xml);
        }catch (DocumentException e){
            return e.getMessage();
        }
        responseXML=department.GET_JMPZ_DEPARTMENT(document);
        return responseXML;
    }
    //药局（房）基本信息接口：
    @Override
    public String GET_JMPZ_CKZL(String xml) {
        GET_JMPZ_CKZL ckzl=new GET_JMPZ_CKZL();
        String responseXML="";
        Document document=null;
        try{
            document= DocumentHelper.parseText(xml);
        }catch (DocumentException e){
            return e.getMessage();
        }
        responseXML=ckzl.GET_JMPZ_CKZL(document);
        return responseXML;
    }
    //给药方式基本信息接口：
    @Override
    public String GET_JMPZ_ROUTENAME(String xml) {
        GET_JMPZ_ROUTENAME routename=new GET_JMPZ_ROUTENAME();
        String responseXML="";
        Document document=null;
        try{
            document= DocumentHelper.parseText(xml);
        }catch (Exception e){
            return e.getMessage();
        }
        responseXML=routename.GET_JMPZ_ROUTENAME(document);
        return responseXML;
    }

    //在院病人床位基本信息接口：
    @Override
    public String GET_JMPZ_BED(String xml) {
        GET_JMPZ_BED bed=new GET_JMPZ_BED();
        String responseXML="";
        Document document=null;
        try{
            document= DocumentHelper.parseText(xml);
        }catch (Exception e){
            return e.getMessage();
        }
        responseXML=bed.GET_JMPZ_BED(document);
        return responseXML;
    }
   //HIS 操作员信息接口
    @Override
    public String GET_JMPZ_HIS_USER(String xml) {
        GET_JMPZ_HIS_USER hisUser=new GET_JMPZ_HIS_USER();
        String responseXML="";
        Document docment=null;
        try{
            docment= DocumentHelper.parseText(xml);
        }catch (DocumentException e){
            return e.getMessage();
        }
        responseXML=hisUser.GET_JMPZ_HIS_USER(docment);
        return  responseXML;
    }
    //药局（房）库存基本信息接口：
    @Override
    public String GET_JMPZ_CKSPKC(String xml) {
        GET_JMPZ_CKSPKC ckspkc=new GET_JMPZ_CKSPKC();
        String responseXML="";
        Document document=null;
        try {
            document=DocumentHelper.parseText(xml);
        }catch (DocumentException e){
            return e.getMessage();
        }
        responseXML=ckspkc.GET_JMPZ_CKSPKC(document);
        return responseXML;
    }
    //医嘱用法资料信息接口：
    @Override
    public String GET_JMPZ_YZYFZL(String xml) {
        GET_JMPZ_YZYFZL yzyfzl=new GET_JMPZ_YZYFZL();
        String responseXML="";
        Document document=null;
        try {
            document=DocumentHelper.parseText(xml);
        }catch (DocumentException e){
            return e.getMessage();
        }
        responseXML=yzyfzl.GET_JMPZ_YZYFZL(document);
        return responseXML;
    }
    //医嘱信息接口：
    @Override
    public String GET_JMPZ_ORDERS(String xml) {
        GET_JMPZ_ORDERS orders=new GET_JMPZ_ORDERS();
        String responseXML="";
        Document document=null;
        try {
            document=DocumentHelper.parseText(xml);
        }catch (DocumentException e){
            return e.getMessage();
        }
        responseXML=orders.GET_JMPZ_ORDERS(document);
        return responseXML;
    }

    @Override
    public String GET_JMPZ_DISPENSE_REC_COUNT(String xml) {
        GET_JMPZ_DISPENSE_REC_COUNT dispenseRecCount=new GET_JMPZ_DISPENSE_REC_COUNT();
        String responseXML="";
        Document document=null;
        try {
            document=DocumentHelper.parseText(xml);
        }catch (DocumentException e){
            return e.getMessage();
        }
        responseXML=dispenseRecCount.GET_JMPZ_DISPENSE_REC_COUNT(document);
        return responseXML;
    }

    @Override
    public String GET_JMPZ_DISPENSE_REC(String xml) {
        GET_JMPZ_DISPENSE_REC dispenseRec=new GET_JMPZ_DISPENSE_REC();
        String responseXML="";
        Document document=null;
        try {
            document=DocumentHelper.parseText(xml);
        }catch (DocumentException e){
            return e.getMessage();
        }
        responseXML=dispenseRec.GET_JMPZ_DISPENSE_REC(document);
        return responseXML;
    }

    @Override
    public String GET_JMPZ_DISPENSE_REC_TYSQ(String xml) {
        GET_JMPZ_DISPENSE_REC_TYSQ dispenseRecTysq=new GET_JMPZ_DISPENSE_REC_TYSQ();
        String responseXML="";
        Document document=null;
        try {
            document=DocumentHelper.parseText(xml);
        }catch (DocumentException e){
            return e.getMessage();
        }
        responseXML=dispenseRecTysq.GET_JMPZ_DISPENSE_REC_TYSQ(document);
        return responseXML;
    }
    @Override
    public String PUSH_JMPZ_ORDERS_CHECK(String xml) {
        PUSH_JMPZ_ORDERS_CHECK ordersCheck=new PUSH_JMPZ_ORDERS_CHECK();
        String responseXML="";
        Document document=null;
        try {
            document=DocumentHelper.parseText(xml);
        }catch (DocumentException e){
            return e.getMessage();
        }
        responseXML=ordersCheck.PUSH_JMPZ_ORDERS_CHECK(document);
        return responseXML;
    }
    @Override
    public String PUSH_JMPZ_PZFY(String xml) {
        PUSH_JMPZ_PZFY pzfy=new PUSH_JMPZ_PZFY();
        String responseXML="";
        Document document=null;
        try {
            document=DocumentHelper.parseText(xml);
        }catch (DocumentException e){
            return e.getMessage();
        }
        responseXML=pzfy.PUSH_JMPZ_PZFY(document);
        return responseXML;
    }

    @Override
    public String PUSH_JMPZ_PEIZHI(String xml) {
        PUSH_JMPZ_PEIZHI peizhi=new PUSH_JMPZ_PEIZHI();
        String responseXML="";
        Document document=null;
        try {
            document=DocumentHelper.parseText(xml);
        }catch (DocumentException e){
            return e.getMessage();
        }
        responseXML=peizhi.PUSH_JMPZ_PEIZHI(document);
        return  responseXML;
    }
}
