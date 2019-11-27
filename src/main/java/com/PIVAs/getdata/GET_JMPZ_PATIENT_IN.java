package com.PIVAs.getdata;

import com.PIVAs.dbconnection.DatabaseConnection;
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

    public String  GET_JMPZ_PATIENT_IN( Document requestxml){
        try {
            conn = DatabaseConnection.getConnection();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            return "数据库连接失败！";
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
        String sql="select c.住院号,c.病人id,c.主页id,c.姓名,c.性别,b.出生日期,b.民族,c.体重,c.入院日期,zlspellcode(c.姓名) as 简拼码," +
                "b.身份证号,c.费别,c.住院医师,a.科室id,d.诊断描述, c.年龄 from  在院病人 a,病人信息 b,病案主页 c," +
                "(select 病人id,主页id,诊断描述 from 病人诊断记录 where 记录来源=2 and 诊断次序=1 and 诊断类型 in (2,12) ) d " +
                "where a.病人id=c.病人id and a.主页id=c.主页id  and a.病人id=b.病人id and a.主页id=b.主页id and a.病人id=d.病人id(+) and a.主页id=d.主页id(+)";
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
                PATIENT_NO.setText(replaceNullString(resultSet.getString("住院号")));
                //病人id
                Element PATIENT_ID=Rows.addElement("PATIENT_ID");
                PATIENT_ID.setText(replaceNullString(resultSet.getString("病人id")));
                //住院标识
                Element VISIT_ID=Rows.addElement("VISIT_ID");
                VISIT_ID.setText(replaceNullString(resultSet.getString("主页id")));
                //病人名称
                Element PATIENTNAME=Rows.addElement("PATIENTNAME");
                PATIENTNAME.setText(replaceNullString(resultSet.getString("姓名")));
                //性别
                Element SEX=Rows.addElement("SEX");
                SEX.setText(replaceNullString(resultSet.getString("性别")));
                //出生日期
                Element BIRTHDAY=Rows.addElement("BIRTHDAY");
                BIRTHDAY.setText(replaceNullString(resultSet.getString("出生日期")));
                //民族
                Element NATION=Rows.addElement("NATION");
                NATION.setText(replaceNullString(resultSet.getString("民族")));
                //入院日期
                Element LAST_VISIT_DATE=Rows.addElement("LAST_VISIT_DATE");
                LAST_VISIT_DATE.setText(replaceNullString(resultSet.getString("入院日期")));
                //拼音码
                Element PYM=Rows.addElement("PYM");
                PYM.setText(replaceNullString(resultSet.getString("简拼码")));
                //身份证
                Element IDENTITY=Rows.addElement("IDENTITY");
                IDENTITY.setText(replaceNullString(resultSet.getString("身份证号")));
                //费别
                Element CHARGE_TYPE=Rows.addElement("CHARGE_TYPE");
                CHARGE_TYPE.setText(replaceNullString(resultSet.getString("费别")));
                //主治医生
                Element ATTENDING_DOCTOR=Rows.addElement("ATTENDING_DOCTOR");
                ATTENDING_DOCTOR.setText(replaceNullString(resultSet.getString("住院医师")));
                //科室编号
                Element DEPT_CODE=Rows.addElement("DEPT_CODE");
                DEPT_CODE.setText(replaceNullString(resultSet.getString("科室id")));
                //年龄
                Element NIANL=Rows.addElement("NIANL");
                NIANL.setText(replaceNullString(resultSet.getString("年龄")));
                //病人出院情况
                Element IS_CHUY=Rows.addElement("IS_CHUY");
                IS_CHUY.setText(replaceNullString("否"));
                //主要诊断
                Element DIAGNOSIS=Rows.addElement("DIAGNOSIS");
                DIAGNOSIS.setText(replaceNullString(resultSet.getString("诊断描述")));
                //体重
                Element PATIENT_WEIGHT=Rows.addElement("PATIENT_WEIGHT");
                PATIENT_WEIGHT.setText(replaceNullString(resultSet.getString("体重")));
                //类型
                Element PATIENT_CLASS=Rows.addElement("PATIENT_CLASS");
                PATIENT_CLASS.setText(replaceNullString("住院"));
            }
            if (row==0){
                fail();
            }
        }catch (Exception e){
            fail();
        }finally {
            try {
                conn.close();
                resultSet.close();
                preparedStatement.close();
            }catch (Exception e){
                e.printStackTrace();
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
        MESSAGE.setText("失败");
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
