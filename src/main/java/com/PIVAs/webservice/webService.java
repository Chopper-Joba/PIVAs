package com.PIVAs.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
@WebService(name = "webservice",targetNamespace = "http://webservice.PIVAs.com")
public interface webService {
    @WebMethod
    //药品基本信息
    public String GET_JMPZ_DRUG_DICT(@WebParam(name = "String")String xml);
    @WebMethod
    //药品属性信息
    public String GET_JMPZ_DRUG_CLASS(@WebParam(name = "String")String xml);
    @WebMethod
    //医生基本信息
    public String GTET_JMPZ_DOCTOR(@WebParam(name = "String")String xml);
    @WebMethod
    //在院病人基本信息
    public String GET_JMPZ_PATIENT_IN(@WebParam(name = "String")String xml);
    @WebMethod
    //科室基本信息
    public String GET_JMPZ_DEPARTMENT(@WebParam(name = "String")String xml);
    @WebMethod
    //药局（房）基本信息
    public String GET_JMPZ_CKZL(@WebParam(name = "String")String xml);
    @WebMethod
    //给药方式基本信息
    public String GET_JMPZ_ROUTENAME(@WebParam(name = "String")String xml);
    @WebMethod
    //在院病人床位基本信息
    public String GET_JMPZ_BED(@WebParam(name = "String")String xml);
    @WebMethod
    //HIS 操作员信息
    public String GET_JMPZ_HIS_USER(@WebParam(name = "String")String xml);
    @WebMethod
    //药局（房）库存基本信息
    public String GET_JMPZ_CKSPKC(@WebParam(name = "String")String xml);
    @WebMethod
    //医嘱用法资料信息接口
    public String GET_JMPZ_YZYFZL(@WebParam(name = "String")String xml);
    @WebMethod
    //医嘱信息接
    public String GET_JMPZ_ORDERS(@WebParam(name = "String")String xml);
    @WebMethod
    //发药汇总信息
    public String GET_JMPZ_DISPENSE_REC_COUNT(@WebParam(name = "String")String xml);
    @WebMethod
    //发药信息
    public String GET_JMPZ_DISPENSE_REC(@WebParam(name = "String")String xml);
    @WebMethod
    //科室退药申请信息
    public String GET_JMPZ_DISPENSE_REC_TYSQ(@WebParam(name = "String")String xml);
    @WebMethod
    //医嘱审查结果回传 HIS
    public String PUSH_JMPZ_ORDERS_CHECK(@WebParam(name = "String")String xml);
    @WebMethod
    //配置费用收取接
    public String PUSH_JMPZ_PZFY(@WebParam(name = "String")String xml);
    @WebMethod
    //静配输液单配置状态
    public String PUSH_JMPZ_PEIZHI(@WebParam(name = "String")String xml);
}
