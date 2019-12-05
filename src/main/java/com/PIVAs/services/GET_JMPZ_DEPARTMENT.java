package com.PIVAs.services;

import com.PIVAs.util.DBUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GET_JMPZ_DEPARTMENT {
    Connection conn=null;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    Document document=null;
    private String errMessage="";
    private  String  seqId,sourceSystem,messageId;
    public  String GET_JMPZ_DEPARTMENT(Document requestxml){
        try {
            conn = DBUtil.getConnection();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            errMessage= "数据库连接失败！";
        }  Element root=requestxml.getRootElement();
        Element seqid=root.element("Body").element("SEQID");
//           获取入参的SEQID节点的值
        seqId=replaceNullString(seqid.getText());
        Element sourcesystem=root.element("Header").element("SourceSystem");
//            获取入参SourceSystem的节点的值
        sourceSystem=replaceNullString(sourcesystem.getText());
        Element messageid=root.element("Header").element("MessageID");
//            获取入参MessageID的值
        messageId=replaceNullString(messageid.getText());
        String sql="select id as DEPARTMENT_NO,名称 as DEPARTMENTNAME from 部门表";
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
                Element DEPARTMENT_NO=Rows.addElement("DEPARTMENT_NO");
                DEPARTMENT_NO.setText(replaceNullString(resultSet.getString("DEPARTMENT_NO")));
                Element DEPARTMENTNAME=Rows.addElement("DEPARTMENTNAME");
                DEPARTMENTNAME.setText(replaceNullString(resultSet.getString("DEPARTMENTNAME")));
                Element CLASS_NO=Rows.addElement("CLASS_NO");
                CLASS_NO.setText(replaceNullString(""));
            }
            if (rows==0){
                errMessage+="没有查询到数据！";
                fail();
            }
        }catch (Exception e){
            errMessage=e.getMessage();
            fail();
        }finally {
            try {
                conn.close();
                resultSet.close();
                preparedStatement.close();
            }catch (Exception e){
                errMessage+=e.getMessage();
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
    public void fail(){
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
        Element DEPARTMENT_NO=Rows.addElement("DEPARTMENT_NO");
        DEPARTMENT_NO.setText(replaceNullString(""));
        Element DEPARTMENTNAME=Rows.addElement("DEPARTMENTNAME");
        DEPARTMENTNAME.setText(replaceNullString(""));
        Element CLASS_NO=Rows.addElement("CLASS_NO");
        CLASS_NO.setText(replaceNullString(""));
    }
}
