package com.PIVAs.services;

import com.PIVAs.util.DBUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GET_JMPZ_CKSPKC {
    Connection conn=null;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    Document document=null;
    String errMessage="";
    private  String  seqId,sourceSystem,messageId;
    public String GET_JMPZ_CKSPKC(Document requestxml){
        try {
            conn = DBUtil.getConnection();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            errMessage+= "数据库连接失败！";
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
        String sql= "select a.药品id as SPID, a.库房id as CKID,a.上次产地 as SHENGCCJ ,null as IS_HEGE,a.可用数量 as CKSHL ," +
                " a.实际数量 as CKSHL_actual, null as KCSX,null as KCXX,b.指导零售价 as COSTS_DJ,null as JWH " +
                "from  药品目录 b,药品库存 a,药品信息 c where a.药品id=b.药品id  and b.药名ID=c.药名ID  and c.剂型 in (2, 3, 13, 20, 21, 28, 32)\n";
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
                Element SPID=Rows.addElement("SPID");
                SPID.addText(replaceNullString(resultSet.getString("SPID")));
                Element CKID=Rows.addElement("CKID");
                CKID.addText(replaceNullString(resultSet.getString("CKID")));
                Element SHENGCCJ=Rows.addElement("SHENGCCJ");
                SHENGCCJ.addText(replaceNullString(resultSet.getString("SHENGCCJ")));
                Element IS_HEGE=Rows.addElement("IS_HEGE");
                IS_HEGE.addText("是");
                Element CKSHL=Rows.addElement("CKSHL");
                CKSHL.addText(replaceNullString(resultSet.getString("CKSHL")));
                Element CKSHL_actual=Rows.addElement("CKSHL_actual");
                CKSHL_actual.addText(replaceNullString(resultSet.getString("CKSHL_actual")));
                Element KCSX=Rows.addElement("KCSX");
                KCSX.addText(replaceNullString(""));
                Element KCXX=Rows.addElement("KCXX");
                KCXX.addText(replaceNullString(""));
                Element COSTS_DJ=Rows.addElement("COSTS_DJ");
                COSTS_DJ.addText(replaceNullString(resultSet.getString("COSTS_DJ")));
                Element JWH=Rows.addElement("JWH");
                JWH.addText(replaceNullString(""));
            }
            if (rows==0){
                errMessage+="没有查询到数据！";
                fail();
            }
        }catch (Exception e){
            errMessage+=e.getMessage();
            fail();
        }
        finally {
              DBUtil.close(conn);
              DBUtil.close(preparedStatement);
              DBUtil.close(resultSet);
        }
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
        Element SPID=Rows.addElement("SPID");
        SPID.addText(replaceNullString(""));
        Element CKID=Rows.addElement("CKID");
        CKID.addText(replaceNullString(""));
        Element SHENGCCJ=Rows.addElement("SHENGCCJ");
        SHENGCCJ.addText(replaceNullString(""));
        Element IS_HEGE=Rows.addElement("IS_HEGE");
        IS_HEGE.addText(replaceNullString(""));
        Element CKSHL=Rows.addElement("CKSHL");
        CKSHL.addText(replaceNullString(""));
        Element CKSHL_actual=Rows.addElement("CKSHL_actual");
        CKSHL_actual.addText(replaceNullString(""));
        Element KCSX=Rows.addElement("KCSX");
        KCSX.addText(replaceNullString(""));
        Element KCXX=Rows.addElement("KCXX");
        KCXX.addText(replaceNullString(""));
        Element COSTS_DJ=Rows.addElement("COSTS_DJ");
        COSTS_DJ.addText(replaceNullString(""));
        Element JWH=Rows.addElement("JWH");
        JWH.addText(replaceNullString(""));
    }
}
