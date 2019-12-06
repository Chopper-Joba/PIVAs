package com.PIVAs.services;

import com.PIVAs.util.DBUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GET_JMPZ_BED {
    Connection conn=null;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    Document document=null;
    StringBuilder errMessage=new StringBuilder("");
    Logger log= LoggerFactory.getLogger(GET_JMPZ_BED.class);
    private  String  seqId,sourceSystem,messageId;
    public String GET_JMPZ_BED(Document requestxml){
        try {
            conn = DBUtil.getConnection();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            //errMessage.append("数据库连接失败！");
            return  "数据库连接失败";
        }
        //获取入参的SEQID节点的值
        Element root=requestxml.getRootElement();
        Element seqid=root.element("Body").element("SEQID");
        seqId=replaceNullString(seqid.getText());
        //获取入参SourceSystem的节点的值
        Element sourcesystem=root.element("Header").element("SourceSystem");
        sourceSystem=replaceNullString(sourcesystem.getText());
        //获取入参MessageID的值
        Element messageid=root.element("Header").element("MessageID");
        messageId=replaceNullString(messageid.getText());
        String sql= "select a.病人id as PATIENT_ID,\n" +
                        "       a.主页id as VISIT_ID,\n" +
                        "       a.科室id as DEPARTMENT_NO,\n" +
                        "       e.当前床号 as BED,\n" +
                        "        c.名称 as BEDNAME\n" +
                        "  from 在院病人 a ,病案主页 b, 床位状况记录 d, 床位等级 c,病人信息 e\n" +
                        " where a.病人id = b.病人id\n" +
                        "   and a.主页id = b.主页id\n" +
                        "   and a.病人id = d.病人id\n" +
                        "   and c.序号 = d.等级id\n" +
                        "   and a.病人id=e.病人id\n" +
                        "   and a.主页id=e.主页id";
        try {
            document = DocumentHelper.createDocument();
            document.setXMLEncoding("utf-8");
            preparedStatement = conn.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            Element Request = document.addElement("Request");
            Element Header = Request.addElement("Header");
            Element SourceSystem = Header.addElement("SourceSystem");
            SourceSystem.setText(sourceSystem);
            Element MessageID = Header.addElement("MessageID");
            MessageID.setText(messageId);
            Element Body = Request.addElement("Body");
            Element CODE = Body.addElement("CODE");
            CODE.setText(replaceNullString("0"));
            Element MESSAGE = Body.addElement("MESSAGE");
            MESSAGE.setText("成功");
            Element SEQID = Body.addElement("SEQID");
            SEQID.setText(seqId);
            int rows = 0;
            while (resultSet.next()){
                rows++;
                Element Rows=Body.addElement("Rows");
                //病人id
                Element PATIENT_ID=Rows.addElement("PATIENT_ID");
                PATIENT_ID.addText(replaceNullString(resultSet.getString("PATIENT_ID")));
                //本次住院标识
                Element VISIT_ID=Rows.addElement("VISIT_ID");
                VISIT_ID.addText(replaceNullString(resultSet.getString("VISIT_ID")));
                //部门编号
                Element DEPARTMENT_NO=Rows.addElement("DEPARTMENT_NO");
                DEPARTMENT_NO.addText(replaceNullString(resultSet.getString("DEPARTMENT_NO")));
                //床位
                Element BED=Rows.addElement("BED");
                BED.addText(replaceNullString(resultSet.getString("BED")));
                //床位名称
                Element BEDNAME=Rows.addElement("BEDNAME");
                BEDNAME.addText(replaceNullString(resultSet.getString("BEDNAME")));
            }
            if (rows==0){
                errMessage.append("没有查询到数据!");
                fail();
            }
        }catch (Exception e){
            errMessage.append(e.getMessage());
            fail();
        }finally {
            DBUtil.close(conn,preparedStatement,resultSet);
        }
        log.error(errMessage.toString());
        return document.asXML();
    }
    public  String replaceNullString(String str){
        if ("".equals(str)||str==null){
            return "";
        }
        else
            return str;
    }
    public void fail(){
        document= DocumentHelper.createDocument();
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
        //病人id
        Element PATIENT_ID=Rows.addElement("PATIENT_ID");
        PATIENT_ID.addText(replaceNullString(""));
        //本次住院标识
        Element VISIT_ID=Rows.addElement("VISIT_ID");
        VISIT_ID.addText(replaceNullString(""));
        //部门编号
        Element DEPARTMENT_NO=Rows.addElement("DEPARTMENT_NO");
        DEPARTMENT_NO.addText(replaceNullString(""));
        //床位
        Element BED=Rows.addElement("BED");
        BED.addText(replaceNullString(""));
        //床位名称
        Element BEDNAME=Rows.addElement("BEDNAME");
        BEDNAME.addText(replaceNullString(""));
    }
}
