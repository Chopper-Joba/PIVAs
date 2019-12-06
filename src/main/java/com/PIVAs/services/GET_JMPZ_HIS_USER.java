package com.PIVAs.services;


import com.PIVAs.util.DBUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
public class GET_JMPZ_HIS_USER {
    private final Logger LOG = LoggerFactory.getLogger(GET_JMPZ_HIS_USER.class);
    Connection conn=null;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    Document document=null;
    StringBuilder errMessage=new StringBuilder("");
    private  String  seqId,sourceSystem,messageId;
    private String JMPZ_ID;
    public GET_JMPZ_HIS_USER(){
        Properties properties = null;
        try {
            properties = PropertiesLoaderUtils.loadAllProperties("application.properties");
        } catch (IOException e) {
           // LOG.error("读取静配中心部门id失败");
            errMessage.append("读取静配中心部门id失败");
        }
        JMPZ_ID=properties.getProperty("JMPZ_DeptId");
    }
    public String GET_JMPZ_HIS_USER(Document requestxml){
        try {
            conn = DBUtil.getConnection();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            //errMessage.append("数据库连接失败！");
            return "数据库连接失败";
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
        String sql= "select a.id as HIS_USERID,a.姓名 as HIS_USERNAME from 人员表 a,部门表 b, 部门人员 c where a.id=c.人员id and b.id=c.部门id and c.部门ID=?";
        LOG.info(sql+" 部门ID="+JMPZ_ID);
        try {
            document = DocumentHelper.createDocument();
            document.setXMLEncoding("utf-8");
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1,Integer.valueOf(JMPZ_ID));
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
            while(resultSet.next()){
                rows++;
                Element Rows=Body.addElement("Rows");
                Element HIS_USERID=Rows.addElement("HIS_USERID");
                HIS_USERID.addText(replaceNullString(resultSet.getString("HIS_USERID")));
                Element HIS_USERNAME=Rows.addElement("HIS_USERNAME");
                HIS_USERNAME.addText(replaceNullString(resultSet.getString("HIS_USERNAME")));
            }
            if (rows==0){
                errMessage.append("没有查询到数据！");
                fail();
            }
        }catch (Exception e){
            //LOG.error(e.getMessage());
            errMessage.append(e.getMessage());
            fail();
        }finally {
            DBUtil.close(conn,preparedStatement,resultSet);
        }
        LOG.error(errMessage.toString());
        return  document.asXML();
    }
    public  String replaceNullString(String str){
        if (str==null){
            return "";
        }
        else
            return str;
    }
    public  void  fail(){
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
        Element HIS_USERID=Rows.addElement("HIS_USERID");
        HIS_USERID.addText(replaceNullString(""));
        Element HIS_USERNAME=Rows.addElement("HIS_USERNAME");
        HIS_USERNAME.addText(replaceNullString(""));
    }
}
