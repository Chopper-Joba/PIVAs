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
import java.sql.Timestamp;
import java.util.Date;
import java.util.TimeZone;

public class GET_JMPZ_DISPENSE_REC_COUNT {
    private static final Logger LOG = LoggerFactory.getLogger(GET_JMPZ_DISPENSE_REC_COUNT.class);
    Connection conn=null;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    Document document=null;
    private  String  seqId,sourceSystem,messageId;
    public String GET_JMPZ_DISPENSE_REC_COUNT(Document requestxml){
        try {
            conn = DBUtil.getConnection();
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
        StringBuilder sql=new StringBuilder("select distinct a.NO as DISPENSING_XH, a.配药日期 as DISPENSING_DATE_TIME,a.库房ID as DISPENSARY,b.序号 as DISPENSE_AMOUNT\n" +
                "from 药品收发记录 a,(select NO, max(序号) as 序号 from 药品收发记录 where 入出系数=-1 group by NO) b\n" +
                "where 入出系数=-1 and a.NO=b.NO and a.序号=b.序号");

        long current = System.currentTimeMillis();
        long todyZero = current / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();
        long todyTwelve = todyZero + 24 * 60 * 60 * 1000 - 1;
        //昨天零点
        Date startTime=new Timestamp(todyZero-24 * 60 * 60 * 1000);
        //今晚12点
        Date endTime=new Timestamp(todyTwelve);
        sql.append(" and to_char(a.配药日期,'yyyy-mm-dd HH24:mm:ss')>=?");
        sql.append(" and to_char(a.配药日期,'yyyy-mm-dd HH24:mm:ss')<=?");
        LOG.info(sql.toString());
        try {
            document = DocumentHelper.createDocument();
            document.setXMLEncoding("utf-8");
            preparedStatement = conn.prepareStatement(sql.toString());
            preparedStatement.setString(1,startTime.toString());
            preparedStatement.setString(2,endTime.toString());
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
                //摆药单号
                Element DISPENSING_XH=Rows.addElement("DISPENSING_XH");
                DISPENSING_XH.addText(replaceNullString(resultSet.getString("DISPENSING_XH")));
                //摆药日期时间
                Element DISPENSING_DATE_TIME=Rows.addElement("DISPENSING_DATE_TIME");
                DISPENSING_DATE_TIME.addText(replaceNullString(resultSet.getString("DISPENSING_DATE_TIME")));
                //发药药房 ID
                Element DISPENSARY=Rows.addElement("DISPENSARY");
                DISPENSARY.addText(replaceNullString(resultSet.getString("DISPENSARY")));
                //时间段内发药总条数
                Element DISPENSE_AMOUNT=Rows.addElement("DISPENSE_AMOUNT");
                DISPENSE_AMOUNT.addText(replaceNullString(resultSet.getString("DISPENSE_AMOUNT")));
            }
            if (rows==0){
                fail();
            }
        }catch (Exception e){
            LOG.error(e.getMessage());
            fail();
        }finally {
            DBUtil.close(conn,preparedStatement,resultSet);
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
    public void fail() {
        document = DocumentHelper.createDocument();
        document.setXMLEncoding("utf-8");
        Element Request = document.addElement("Request");
        Element Header = Request.addElement("Header");
        Element SourceSystem = Header.addElement("SourceSystem");
        SourceSystem.setText(sourceSystem);
        Element MessageID = Header.addElement("MessageID");
        MessageID.setText(messageId);
        Element Body = Request.addElement("Body");
        Element CODE = Body.addElement("CODE");
        CODE.setText("1");
        Element MESSAGE = Body.addElement("MESSAGE");
        MESSAGE.setText("失败");
        Element Rows = Body.addElement("Rows");
        //摆药单号
        Element DISPENSING_XH=Rows.addElement("DISPENSING_XH");
        DISPENSING_XH.addText(replaceNullString(""));
        //摆药日期时间
        Element DISPENSING_DATE_TIME=Rows.addElement("DISPENSING_DATE_TIME");
        DISPENSING_DATE_TIME.addText(replaceNullString(""));
        //发药药房 ID
        Element DISPENSARY=Rows.addElement("DISPENSARY");
        DISPENSARY.addText(replaceNullString(""));
        //时间段内发药总条数
        Element DISPENSE_AMOUNT=Rows.addElement("DISPENSE_AMOUNT");
        DISPENSE_AMOUNT.addText(replaceNullString(""));
    }
}
