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

public class GET_JMPZ_CKZL {
    private static final Logger LOG= LoggerFactory.getLogger(GET_JMPZ_CKZL.class);
    Connection conn=null;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    Document document=null;
    StringBuilder errMessage=new StringBuilder("");
    private  String  seqId,sourceSystem,messageId;
    public String GET_JMPZ_CKZL(Document requestxml){
        try {
            conn = DBUtil.getConnection();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            //errMessage.append("数据库连接失败！");
            return "数据库连接失败！";
        }
        Element root=requestxml.getRootElement();
        Element seqid=root.element("Body").element("SEQID");
//           获取入参的SEQID节点的值
        seqId=replaceNullString(seqid.getText());
        Element sourcesystem=root.element("Header").element("SourceSystem");
//            获取入参SourceSystem的节点的值
        sourceSystem=replaceNullString(sourcesystem.getText());
        Element messageid=root.element("Header").element("MessageID");
//            获取入参MessageID的值
        messageId=replaceNullString(messageid.getText());
        String sql= "select  distinct a.id as CKID,a.编码 as CKBH,a.名称 as CKNAME,null as CLASS_NO,null as BEACTIVE,null as NOTE " +
                " from 部门表 a,部门性质说明 b where a.id=b.部门id and b.工作性质 in ('试剂库房','配置中心','中药库','西药库','成药库','中药房','西药房','成药房','院外药房'）";
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
                //药房id
                Element CKID= Rows.addElement("CKID");
                CKID.addText(replaceNullString(resultSet.getString("CKID")));
                //药房编码
                Element CKBH= Rows.addElement("CKBH");
                CKBH.addText(replaceNullString(resultSet.getString("CKBH")));
                //药房名称
                Element CKNAME= Rows.addElement("CKNAME");
                CKNAME.addText(replaceNullString(resultSet.getString("CKNAME")));
                //分类
                Element CLASS_NO= Rows.addElement("CLASS_NO");
                CLASS_NO.addText(replaceNullString(""));
                //是否活动
                Element BEACTIVE= Rows.addElement("BEACTIVE");
                BEACTIVE.addText(replaceNullString("是"));
                //备注
                Element NOTE= Rows.addElement("NOTE");
                NOTE.addText(replaceNullString(""));
            }
            if ( rows==0)
            {
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
        Element CKID= Rows.addElement("CKID");
        CKID.addText(replaceNullString(""));
        //药房编码
        Element CKBH= Rows.addElement("CKBH");
        CKBH.addText(replaceNullString(""));
        //药房名称
        Element CKNAME= Rows.addElement("CKBH");
        CKNAME.addText(replaceNullString(""));
        //分类
        Element CLASS_NO= Rows.addElement("CLASS_NO");
        CLASS_NO.addText(replaceNullString(""));
        //是否活动
        Element BEACTIVE= Rows.addElement("BEACTIVE");
        BEACTIVE.addText(replaceNullString(""));
        //备注
        Element NOTE= Rows.addElement("NOTE");
        NOTE.addText(replaceNullString(""));
    }
}
