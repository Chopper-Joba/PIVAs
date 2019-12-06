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

public class GET_JMPZ_ROUTENAME {
    private static final Logger LOG= LoggerFactory.getLogger(GET_JMPZ_ROUTENAME.class);
    Connection conn=null;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    Document document=null;
    StringBuilder errMessage=new StringBuilder("");
    private  String  seqId,sourceSystem,messageId;
    public String GET_JMPZ_ROUTENAME(Document requestxml){
        try {
            conn = DBUtil.getConnection();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
           // errMessage.append( "数据库连接失败！");
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
        String sql="select id as ROUTENAMEID,编码 as ROUTENAMEBH,名称 as ROUTENAME,null as ROUTENAME_PASS " +
                  "from 诊疗项目目录 where  名称 like '%静脉输液%'";
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
                //给药方式id
                Element ROUTENAMEID=Rows.addElement("ROUTENAMEID");
                ROUTENAMEID.addText(replaceNullString(resultSet.getString("ROUTENAMEID")));
                //给药方式编码
                Element ROUTENAMEBH=Rows.addElement("ROUTENAMEBH");
                ROUTENAMEBH.addText(replaceNullString(resultSet.getString("ROUTENAMEBH")));
                //给药方式名称
                Element ROUTENAME=Rows.addElement("ROUTENAME");
                ROUTENAME.addText(replaceNullString(resultSet.getString("ROUTENAME")));
                //合理用药给药方式
                Element ROUTENAME_PASS=Rows.addElement("ROUTENAME_PASS");
                ROUTENAME_PASS.addText(replaceNullString(""));
            }
            if (rows==0){
                errMessage.append("没有查询到数据！");
                fail();
            }
        }catch (Exception e){
            errMessage.append(e.getMessage());
            fail();
        }finally {
            DBUtil.close(conn,preparedStatement,resultSet);
        }
        LOG.error(errMessage.toString());
        return document.asXML();
    }
    public  String replaceNullString(String str)
    {
        if ("".equals(str)||str==null){
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
        //给药方式id
        Element ROUTENAMEID=Rows.addElement("ROUTENAMEID");
        ROUTENAMEID.addText(replaceNullString(""));
        //给药方式编码
        Element ROUTENAMEBH=Rows.addElement("ROUTENAMEBH");
        ROUTENAMEBH.addText(replaceNullString(""));
        //给药方式名称
        Element ROUTENAME=Rows.addElement("ROUTENAME");
        ROUTENAME.addText(replaceNullString(""));
        //合理用药给药方式
        Element ROUTENAME_PASS=Rows.addElement("ROUTENAME_PASS");
        ROUTENAME_PASS.addText(replaceNullString(""));
    }
}
