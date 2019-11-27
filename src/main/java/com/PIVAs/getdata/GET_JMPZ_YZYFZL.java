package com.PIVAs.getdata;

import com.PIVAs.dbconnection.DatabaseConnection;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GET_JMPZ_YZYFZL {
    Connection conn=null;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    Document document=null;
    private  String  seqId,sourceSystem,messageId;
    public String GET_JMPZ_YZYFZL(Document requestxml){
        try {
            conn = DatabaseConnection.getConnection();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            return "数据库连接失败！";
        }
        Element root=requestxml.getRootElement();
        Element seqid=root.element("Body").element("SEQID");
        //获取入参的SEQID节点的值
        seqId=replaceNullString(seqid.getText());
        Element sourcesystem=root.element("Header").element("SourceSystem");
        //获取入参SourceSystem的节点的值
        sourceSystem=replaceNullString(sourcesystem.getText());
        Element messageid=root.element("Header").element("MessageID");
        //获取入参MessageID的值
        messageId=replaceNullString(messageid.getText());
        String sql="";
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
            while (resultSet.next()) {
                rows++;
                Element Rows = Body.addElement("Rows");
                //医嘱用法编号
                Element YFBH=Rows.addElement("YFBH");
                YFBH.addText(replaceNullString(""));
                //医嘱用法名称
                Element YFNAME=Rows.addElement("YFNAME");
                YFNAME.addText(replaceNullString(""));
                //用法次数/频率，N 次
                Element YFNUM=Rows.addElement("YFNUM");
                YFNUM.addText(replaceNullString(""));
                //用法天数，N 天
                Element YFTS=Rows.addElement("YFTS");
                YFTS.addText(replaceNullString(""));
                //用法执行时间
                Element YFZXSJ=Rows.addElement("YFZXSJ");
                YFZXSJ.addText(replaceNullString(""));
                //合理用药用医嘱用法
                Element frequency_pass=Rows.addElement("frequency_pass");
                frequency_pass.addText(replaceNullString(""));
            }
            if (rows==0){
                fail();
            }
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
        //医嘱用法编号
        Element YFBH=Rows.addElement("YFBH");
        YFBH.addText(replaceNullString(""));
        //医嘱用法名称
        Element YFNAME=Rows.addElement("YFNAME");
        YFNAME.addText(replaceNullString(""));
        //用法次数/频率，N 次
        Element YFNUM=Rows.addElement("YFNUM");
        YFNUM.addText(replaceNullString(""));
        //用法天数，N 天
        Element YFTS=Rows.addElement("YFTS");
        YFTS.addText(replaceNullString(""));
        //用法执行时间
        Element YFZXSJ=Rows.addElement("YFZXSJ");
        YFZXSJ.addText(replaceNullString(""));
        //合理用药用医嘱用法
        Element frequency_pass=Rows.addElement("frequency_pass");
        frequency_pass.addText(replaceNullString(""));

    }
}
