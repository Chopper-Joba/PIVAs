package com.PIVAs.services;

import com.PIVAs.entity.Patient;
import com.PIVAs.util.DBUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;

/**
 * @author li_yinghao
 * @version 1.0
 * @date 2019/12/9 10:07
 * @description
 */
public class PushJiZhang {
    Connection conn;
    CallableStatement cbs=null;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    public PushJiZhang() throws IOException {
        this.conn=DBUtil.getConnection();
    }

    public void PushZhuYuanJiZhang(String PATIENT_ID,String Department_no,String Item_no,String DevNo,int Num,String Costs,String Createtime) throws SQLException {
        //获取病人信息
        Patient patient=getPatient(PATIENT_ID);
        //生成单据号
        String NoId=getNoId();
        //获取单据号对应的序号
        String XH=getXH(NoId);
        //操作人姓名
        String devName=getDevName(DevNo);
        //收费类别
        String feesType=getFeesType("27845");
        String sql="{call ZL_住院记帐记录_INSERT(?,?,?,?,?,?,?,?,?,?" +
                ",?,?,?,?,?,?,?,?,?,?" +
                ",?,?,?,?,?,?,?,?,?,?" +
                ",?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,?,?" +
                ",?)}";
        cbs=conn.prepareCall(sql);
        //病人信息
        cbs.setString(1,NoId);
        cbs.setInt(2,Integer.valueOf(XH));
        cbs.setInt(3,patient.getPatientID());
        cbs.setInt(4,patient.getHomeID());
        cbs.setInt(5,patient.getTypeID());
        cbs.setString(6,patient.getName());
        cbs.setString(7,patient.getGender());
        cbs.setString(8,patient.getAge());
        cbs.setString(9,patient.getBedNo());
        cbs.setString(10,patient.getCosType());
        cbs.setInt(11,patient.getInpatientID());
        cbs.setInt(12,patient.getDepartmentID());
        //加班标志,婴儿费
        cbs.setInt(13,0);
        cbs.setInt(14,0);
        //开单人
        cbs.setInt(15,Integer.valueOf(Department_no));
        cbs.setString(16,devName);
        //从属父号
        cbs.setInt(17,0);
        //收费项目
        cbs.setInt(18,27845);
        cbs.setString(19,feesType);
        //计算单位
        cbs.setString(20,null);
        //保险项目否
        cbs.setInt(21,0);
        //保险大类id
        cbs.setInt(22,10);
        //保险编码
        cbs.setString(23,null);
        cbs.setInt(24,1);
        cbs.setInt(25,Num);
        //附加标志
        cbs.setInt(26,0);
        cbs.setInt(27,Integer.valueOf(Department_no));
        //价格父号
        cbs.setInt(28,0);
        //收入项目
        cbs.setInt(29,Integer.valueOf(Item_no));
        cbs.setString(30,null);
        cbs.setInt(31,0);
        cbs.setBigDecimal(32,new BigDecimal(Costs));
        cbs.setBigDecimal(33,new BigDecimal(Costs));
        cbs.setInt(34,0);
        //发生时间
        cbs.setString(35,Createtime);
        cbs.setString(36,Createtime);
        //药品摘要
        cbs.setString(37,null);
        //划价
        cbs.setInt(38,0);
        //操作员
        cbs.setInt(39,Integer.valueOf(DevNo));
        cbs.setString(40,devName);
        cbs.setInt(41,0);

        cbs.execute();
        DBUtil.close(conn,preparedStatement,resultSet);
        DBUtil.close(cbs);
    }
    //获取病人相关信息
    private Patient getPatient(String PATIENT_ID) throws SQLException {
        Patient patient=new Patient();
        String sql="select 病人ID,主页ID,住院号,姓名,性别,年龄,当前床号,费别,当前病区ID,当前科室ID from 病人信息 where 病人ID=?";
        preparedStatement=conn.prepareStatement(sql);
        preparedStatement.setInt(1,Integer.valueOf(PATIENT_ID));
        resultSet=preparedStatement.executeQuery();
        while (resultSet.next()){
            patient.setPatientID(resultSet.getInt("病人ID"));
            patient.setHomeID(resultSet.getInt("主页ID"));
            patient.setTypeID(resultSet.getInt("住院号"));
            patient.setName(resultSet.getString("姓名"));
            patient.setGender(resultSet.getString("性别"));
            patient.setAge(resultSet.getString("年龄"));
            patient.setBedNo(resultSet.getString("当前床号"));
            patient.setCosType(resultSet.getString("费别"));
            patient.setInpatientID(resultSet.getInt("当前病区ID"));
            patient.setDepartmentID(resultSet.getInt("当前科室ID"));
        }
        return patient;
    }
    //获取住院费用单据号
    private String getNoId() throws SQLException {
        String NoId="";
        String sql="select NEXTNO(12) as NoId from DUAL";
        preparedStatement=conn.prepareStatement(sql);
        resultSet=preparedStatement.executeQuery();
        while (resultSet.next()){
            NoId = resultSet.getString("NoId");
        }
        return NoId;
    }
    //获取住院费用序号
    private String getXH(String NoId) throws SQLException {
        String XH="";
        String sql="select max(序号)+1 as No from 住院费用记录 where NO=?";
        preparedStatement=conn.prepareStatement(sql);
        preparedStatement.setString(1,NoId);
        resultSet=preparedStatement.executeQuery();
        while (resultSet.next()){
            XH=resultSet.getString("No");
        }
        return XH;
    }
    //获取操作员
    private String getDevName(String DevNo) throws SQLException {
        String DevName="";
        String sql="select 姓名 as name from 人员表 where id=?";
        preparedStatement=conn.prepareStatement(sql);
        preparedStatement.setInt(1,Integer.valueOf(DevNo));
        resultSet=preparedStatement.executeQuery();
        while (resultSet.next()){
            DevName=resultSet.getString("name");
        }
        return DevName;
    }
    //获取费别
    private String getFeesType(String feesID) throws SQLException {
        String feesType="";
        String sql="select 类别 as type from 收费细目 where ID=?";
        preparedStatement=conn.prepareStatement(sql);
        preparedStatement.setInt(1,Integer.valueOf(feesID));
        resultSet=preparedStatement.executeQuery();
        while (resultSet.next()){
            feesType=resultSet.getString("type");
        }
        return feesType;
    }
}
