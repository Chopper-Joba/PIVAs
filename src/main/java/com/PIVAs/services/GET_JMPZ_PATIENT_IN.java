package com.PIVAs.services;

import com.PIVAs.util.DBUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GET_JMPZ_PATIENT_IN {
    Connection conn=null;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    Document document=null;
    String  seqId,sourceSystem,messageId;
    String errMssage="";//存放错误信息
    public String  GET_JMPZ_PATIENT_IN( Document requestxml){
        try {
            conn = DBUtil.getConnection();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            errMssage+= "数据库连接失败！";
        }
        Element root=requestxml.getRootElement();
        //获取入参的SEQID节点的值
        Element seqid=root.element("Body").element("SEQID");
        seqId=replaceNullString(seqid.getText());
        //获取入参SourceSystem的节点的值
        Element sourcesystem=root.element("Header").element("SourceSystem");
        sourceSystem=replaceNullString(sourcesystem.getText());
        //获取入参MessageID的值
        Element messageid=root.element("Header").element("MessageID");
        messageId=replaceNullString(messageid.getText());
        String sql= "select c.住院号 as PATIENT_NO,\n" +
                        "       c.病人id as PATIENT_ID ,\n" +
                        "       c.主页id as VISIT_ID,\n" +
                        "       c.姓名 as PATIENTNAME ,\n" +
                        "       c.性别 as SEX,\n" +
                        "       b.出生日期 as BIRTHDAY,\n" +
                        "       b.民族 as NATION ,\n" +
                        "       c.入院日期 as LAST_VISIT_DATE,\n" +
                        "       zlspellcode(c.姓名) as PYM,\n" +
                        "       b.身份证号 as IDENTITY,\n" +
                        "       c.费别 as  CHARGE_TYPE ,\n" +
                        "       c.住院医师 as ATTENDING_DOCTOR,\n" +
                        "       a.科室id as DEPT_CODE,\n" +
                        "       c.年龄 as NIANL,\n" +
                        "       null as IS_CHUY,\n" +
                        "       d.诊断描述 as DIAGNOSIS ,\n" +
                        "       c.体重 as PATIENT_WEIGHT ,\n" +
                        "       null as PATIENT_CLASS\n" +
                        "  from 在院病人 a,\n" +
                        "       病人信息 b,\n" +
                        "       病案主页 c,\n" +
                        "       (select 病人id, 主页id, 诊断描述\n" +
                        "          from 病人诊断记录\n" +
                        "         where 记录来源 = 2\n" +
                        "           and 诊断次序 = 1\n" +
                        "           and 诊断类型 in (2, 12)) d\n" +
                        " where a.病人id = c.病人id\n" +
                        "   and a.主页id = c.主页id\n" +
                        "   and a.病人id = b.病人id\n" +
                        "   and a.主页id = b.主页id\n" +
                        "   and a.病人id = d.病人id(+)\n" +
                        "   and a.主页id = d.主页id(+)";
        try{
            document= DocumentHelper.createDocument();
            document.setXMLEncoding("utf-8");
            preparedStatement=conn.prepareStatement(sql);
            resultSet=preparedStatement.executeQuery();
            //拼接出参
            Element Request=document.addElement("Request");
            Element Header=Request.addElement("Header");
            Element SourceSystem=Header.addElement("SourceSystem");
            SourceSystem.setText(sourceSystem);
            Element MessageID=Header.addElement("MessageID");
            MessageID.setText(messageId);
            Element Body=Request.addElement("Body");
            Element CODE=Body.addElement("CODE");
            CODE.setText(replaceNullString("0"));
            Element MESSAGE=Body.addElement("MESSAGE");
            MESSAGE.setText("成功");
            Element SEQID=Body.addElement("SEQID");
            SEQID.setText(seqId);
            int row=0;
            //出参的rows
            while (resultSet.next()){
                row++;
                Element Rows=Body.addElement("Rows");
                //住院号
                Element PATIENT_NO=Rows.addElement("PATIENT_NO");
                PATIENT_NO.setText(replaceNullString(resultSet.getString("PATIENT_NO")));
                //病人id
                Element PATIENT_ID=Rows.addElement("PATIENT_ID");
                PATIENT_ID.setText(replaceNullString(resultSet.getString("PATIENT_ID")));
                //住院标识
                Element VISIT_ID=Rows.addElement("VISIT_ID");
                VISIT_ID.setText(replaceNullString(resultSet.getString("VISIT_ID")));
                //病人名称
                Element PATIENTNAME=Rows.addElement("PATIENTNAME");
                PATIENTNAME.setText(replaceNullString(resultSet.getString("PATIENTNAME")));
                //性别
                Element SEX=Rows.addElement("SEX");
                SEX.setText(replaceNullString(resultSet.getString("SEX")));
                //出生日期
                Element BIRTHDAY=Rows.addElement("BIRTHDAY");
                BIRTHDAY.setText(replaceNullString(resultSet.getString("BIRTHDAY")));
                //民族
                Element NATION=Rows.addElement("NATION");
                NATION.setText(replaceNullString(resultSet.getString("NATION")));
                //入院日期
                Element LAST_VISIT_DATE=Rows.addElement("LAST_VISIT_DATE");
                LAST_VISIT_DATE.setText(replaceNullString(resultSet.getString("LAST_VISIT_DATE")));
                //拼音码
                Element PYM=Rows.addElement("PYM");
                PYM.setText(replaceNullString(resultSet.getString("PYM")));
                //身份证
                Element IDENTITY=Rows.addElement("IDENTITY");
                IDENTITY.setText(replaceNullString(resultSet.getString("IDENTITY")));
                //费别
                Element CHARGE_TYPE=Rows.addElement("CHARGE_TYPE");
                CHARGE_TYPE.setText(replaceNullString(resultSet.getString("CHARGE_TYPE")));
                //主治医生
                Element ATTENDING_DOCTOR=Rows.addElement("ATTENDING_DOCTOR");
                ATTENDING_DOCTOR.setText(replaceNullString(resultSet.getString("ATTENDING_DOCTOR")));
                //科室编号
                Element DEPT_CODE=Rows.addElement("DEPT_CODE");
                DEPT_CODE.setText(replaceNullString(resultSet.getString("DEPT_CODE")));
                //年龄
                Element NIANL=Rows.addElement("NIANL");
                NIANL.setText(replaceNullString(resultSet.getString("NIANL")));
                //病人出院情况
                Element IS_CHUY=Rows.addElement("IS_CHUY");
                IS_CHUY.setText(replaceNullString("否"));
                //主要诊断
                Element DIAGNOSIS=Rows.addElement("DIAGNOSIS");
                DIAGNOSIS.setText(replaceNullString(resultSet.getString("DIAGNOSIS")));
                //体重
                Element PATIENT_WEIGHT=Rows.addElement("PATIENT_WEIGHT");
                PATIENT_WEIGHT.setText(replaceNullString(resultSet.getString("PATIENT_WEIGHT")));
                //类型
                Element PATIENT_CLASS=Rows.addElement("PATIENT_CLASS");
                PATIENT_CLASS.setText(replaceNullString("住院"));
            }
            if (row==0){
                errMssage+="没有查询到数据！";
                fail();
            }
        }catch (Exception e){
            errMssage+=e.getMessage();
            fail();
        }finally {
            try {
                conn.close();
                resultSet.close();
                preparedStatement.close();
            }catch (Exception e){
                errMssage+=e.getMessage();
                fail();
            }
        }
        return document.asXML();
    }
    public  String replaceNullString(String str){
        if (str==null){
            return "";
        }
        else
            return str;
    }
    public  void fail(){
        document=DocumentHelper.createDocument();
        document.setXMLEncoding("utf-8");
        Element Request=document.addElement("Request");
        Element Header=Request.addElement("Header");
        Element SourceSystem=Header.addElement("SourceSystem");
        SourceSystem.setText(sourceSystem);
        Element MessageID=Header.addElement("MessageID");
        MessageID.setText(messageId);
        Element Body=Request.addElement("Body");
        Element CODE=Body.addElement("CODE");
        CODE.setText("1");
        Element MESSAGE=Body.addElement("MESSAGE");
        MESSAGE.setText("失败"+errMssage);
        Element Rows=Body.addElement("Rows");
        Element PATIENT_NO=Rows.addElement("PATIENT_NO");
        PATIENT_NO.setText(replaceNullString(""));
        Element PATIENT_ID=Rows.addElement("PATIENT_ID");
        PATIENT_ID.setText(replaceNullString(""));
        Element VISIT_ID=Rows.addElement("VISIT_ID");
        VISIT_ID.setText(replaceNullString(""));
        Element PATIENTNAME=Rows.addElement("PATIENTNAME");
        PATIENTNAME.setText(replaceNullString(""));
        Element SEX=Rows.addElement("SEX");
        SEX.setText(replaceNullString(""));
        Element BIRTHDAY=Rows.addElement("BIRTHDAY");
        BIRTHDAY.setText(replaceNullString(""));
        Element NATION=Rows.addElement("NATION");
        NATION.setText(replaceNullString(""));
        Element LAST_VISIT_DATE=Rows.addElement("LAST_VISIT_DATE");
        NATION.setText(replaceNullString(""));
        Element PYM=Rows.addElement("PYM");
        PYM.setText(replaceNullString(""));
        Element IDENTITY=Rows.addElement("IDENTITY");
        IDENTITY.setText(replaceNullString(""));
        Element CHARGE_TYPE=Rows.addElement("CHARGE_TYPE");
        CHARGE_TYPE.setText(replaceNullString(""));
        Element ATTENDING_DOCTOR=Rows.addElement("ATTENDING_DOCTOR");
        ATTENDING_DOCTOR.setText(replaceNullString(""));
        Element DEPT_CODE=Rows.addElement("DEPT_CODE");
        DEPT_CODE.setText(replaceNullString(""));
        Element NIANL=Rows.addElement("NIANL");
        NIANL.setText(replaceNullString(""));
        Element IS_CHUY=Rows.addElement("IS_CHUY");
        IS_CHUY.setText(replaceNullString(""));
        Element DIAGNOSIS=Rows.addElement("DIAGNOSIS");
        DIAGNOSIS.setText(replaceNullString(""));
        Element PATIENT_WEIGHT=Rows.addElement("PATIENT_WEIGHT");
        PATIENT_WEIGHT.setText(replaceNullString(""));
        Element PATIENT_CLASS=Rows.addElement("PATIENT_CLASS");
        PATIENT_CLASS.setText(replaceNullString(""));
    }
}
