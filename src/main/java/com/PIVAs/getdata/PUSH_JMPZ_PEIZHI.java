package com.PIVAs.getdata;

import com.PIVAs.dao.ORDERS_CHECK;
import com.PIVAs.dao.PEIZHI;
import com.PIVAs.dbconnection.DatabaseConnection;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PUSH_JMPZ_PEIZHI {
    Connection conn=null;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    Document document=null;
    CallableStatement proc=null;
    ORDERS_CHECK ordersCheck=new ORDERS_CHECK();
    private  String  seqId,sourceSystem,messageId;
    PEIZHI peizhi=new PEIZHI();
    public String PUSH_JMPZ_PEIZHI(Document requestxml){
        try {
            conn = DatabaseConnection.getConnection();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            return "数据库连接失败！";
        }
        Element root=requestxml.getRootElement();
        //获取入参SourceSystem的节点的值
        Element sourcesystem=root.element("Header").element("SourceSystem");
        sourceSystem=replaceNullString(sourcesystem.getText());
        //获取入参MessageID的值
        Element messageid=root.element("Header").element("MessageID");
        messageId=replaceNullString(messageid.getText());
        //获取入参的费用序号
        Element xh=root.element("Body").element("XH");
        peizhi.setXH(Integer.valueOf(xh.getText()).intValue());
        //获取入参的PATIENT_ID,病人ID
        Element patient=root.element("Body").element("PATIENT_ID");
        peizhi.setPATIENT_ID(patient.getText());
        //住院次数,VISIT_ID
        Element visitid=root.element("Body").element("VISIT_ID");
        peizhi.setVISIT_ID(visitid.getText());
        //医嘱号,ORDER_NO
        Element orderno=root.element("Body").element("ORDER_NO");
        peizhi.setORDER_NO(orderno.getText());
        //使用日期,USE_DATE
        Element userdate=root.element("Body").element("USE_DATE");
        peizhi.setUSE_DATE(userdate.getText());
        //使用时间,USE_TIME
        Element usertime=root.element("Body").element("USE_TIME");
        peizhi.setUSE_TIME(usertime.getText());
        //药品id,SPID
        Element spid=root.element("Body").element("SPID");
        peizhi.setSPID(spid.getText());
        //配置药品数量,SHL_PEIZHI
        Element shlpeizhi=root.element("Body").element("SHL_PEIZHI");
        peizhi.setSHL_PEIZHI(new BigDecimal(shlpeizhi.getText()));
        //SEQID
        Element seqid=root.element("Body").element("SEQID");
        peizhi.setSEQID((seqid.getText()));
        try {
            document = DocumentHelper.createDocument();
            document.setXMLEncoding("utf-8");
            proc = conn.prepareCall("{call PUSH_JMPZ_PEIZHI_INS(?,?,?,?,?,?,?,?,?)}");
            proc.setInt(1, (Integer) peizhi.getXH());
            proc.setString(2, peizhi.getPATIENT_ID());
            proc.setString(3, peizhi.getVISIT_ID());
            proc.setString(4, peizhi.getORDER_NO());
            proc.setString(5, peizhi.getUSE_DATE());
            proc.setString(6, peizhi.getUSE_TIME());
            proc.setString(7, peizhi.getSPID());
            proc.setBigDecimal(8, peizhi.getSHL_PEIZHI());
            proc.setString(9, peizhi.getSEQID());
            proc.execute();
        }catch (Exception e){
            fail();
        }
        return null;
    }
    public  String replaceNullString(String str){
        if (str==null){
            return "";
        }
        else
            return str;
    }
    public void fail(){

    }
}
