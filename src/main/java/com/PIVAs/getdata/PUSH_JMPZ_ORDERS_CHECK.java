package com.PIVAs.getdata;

import com.PIVAs.dao.ORDERS_CHECK;
import com.PIVAs.dbconnection.DatabaseConnection;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PUSH_JMPZ_ORDERS_CHECK {
    Connection conn=null;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    Document document=null;
    CallableStatement proc=null;
    ORDERS_CHECK ordersCheck=new ORDERS_CHECK();
    private  String  seqId,sourceSystem,messageId;
    public String PUSH_JMPZ_ORDERS_CHECK(Document requestxml){
        try {
            conn = DatabaseConnection.getConnection();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            return "数据库连接失败！";
        }
        Element root=requestxml.getRootElement();
        Element sourcesystem=root.element("Header").element("SourceSystem");
        //获取入参SourceSystem的节点的值
        sourceSystem=replaceNullString(sourcesystem.getText());
        Element messageid=root.element("Header").element("MessageID");
        //获取入参MessageID的值
        messageId=replaceNullString(messageid.getText());
        //获取入参PATIENT_ID的值
        Element patientid=root.element("Body").element("PATIENT_ID");
        ordersCheck.setPATIENT_ID(patientid.getText());
        //获取入参VISIT_ID
        Element vicitid=root.element("Body").element("VISIT_ID");
        ordersCheck.setVISIT_ID(vicitid.getText());
        //获取入参ORDER_ID的值
        Element orderid=root.element("Body").element("ORDER_ID");
        ordersCheck.setORDER_ID(orderid.getText());
        //获取入参SHBZ
        Element shbz=root.element("Body").element("SHBZ");
       ordersCheck.setSHBZ(shbz.getText());
        //获取入参SHENGFANGZT
        Element shengfangzt=root.element("Body").element("SHENGFANGZT");
        ordersCheck.setSHENGFANGZT(shengfangzt.getText());
        //获取入参SEQID
        Element seqid=root.element("Body").element("SEQID");
        ordersCheck.setSEQID(seqid.getText());
        try {
            document = DocumentHelper.createDocument();
            document.setXMLEncoding("utf-8");
            proc = conn.prepareCall("{call PUSH_JMPZ_ORDERS_CHECK_INS(?,?,?,?,?,?)}");
            proc.setString(1,ordersCheck.getPATIENT_ID());
            proc.setString(2,ordersCheck.getVISIT_ID());
            proc.setString(3,ordersCheck.getORDER_ID());
            proc.setString(4,ordersCheck.getSHBZ());
            proc.setString(5,ordersCheck.getSHENGFANGZT());
            proc.setString(6,ordersCheck.getSEQID());
            proc.execute();
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
            SEQID.setText(replaceNullString(ordersCheck.getSEQID()));
        }catch (Exception e){
            fail();
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
        MESSAGE.setText("失败");
        Element SEQID = Body.addElement("SEQID");
        SEQID.setText(ordersCheck.getSEQID());
    }
}
