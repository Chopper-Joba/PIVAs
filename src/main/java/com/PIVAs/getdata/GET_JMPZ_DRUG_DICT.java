package com.PIVAs.getdata;

import com.PIVAs.dbconnection.DatabaseConnection;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GET_JMPZ_DRUG_DICT {
    Connection conn=null;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    Document document=null;
    String message;
    private  String  seqId,sourceSystem,messageId;
    public String GET_JMPZ_DRUG_DICT(Document requestxml){
        try {
            conn = DatabaseConnection.getConnection();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            message= "数据库连接失败！";
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
        String sql="select a.药品id, a.编码,a.名称,c.通用名称,a.规格,a.售价单位,a.剂量系数, b.名称,a.批准文号,a.产地,c.剂量单位,a.药库单位" +
                " from  药品目录 a,药品剂型 b,药品信息 c where a.药名ID=c.药名ID and c.剂型=b.编码 ";
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
                //商品 ID
                Element SPID=Rows.addElement("SPID");
                SPID.addText(replaceNullString(resultSet.getString("药品id")));
                //商品编号
                Element SPBH=Rows.addElement("SPBH");
                SPBH.addText(replaceNullString(resultSet.getString("编码")));
                //商品名称
                Element SPMCH=Rows.addElement("SPMCH");
                SPMCH.addText(replaceNullString(resultSet.getString("名称")));
                //英文名称
                Element ENGLISH_NAME=Rows.addElement("ENGLISH_NAME");
                ENGLISH_NAME.addText(replaceNullString(""));
                //通用名称
                Element TONGYMCH=Rows.addElement("TONGYMCH");
                TONGYMCH.addText(replaceNullString(resultSet.getString("通用名称")));
                //商品规格
                Element SHPGG=Rows.addElement("SHPGG");
                SHPGG.addText(replaceNullString(resultSet.getString("规格")));
                //单位
                Element DW=Rows.addElement("DW");
                DW.addText(replaceNullString(resultSet.getString("售价单位")));
                //使用剂量
                Element USEJLGG=Rows.addElement("USEJLGG");
                USEJLGG.addText(replaceNullString(resultSet.getString("剂量系数")));
                //使用单位
                Element USEDW=Rows.addElement("USEDW");
                USEDW.addText(replaceNullString(resultSet.getString("剂量单位")));
                //大包装单位
                Element PACKET_DW=Rows.addElement("PACKET_DW");
                PACKET_DW.addText(replaceNullString(resultSet.getString("药库单位")));
                //剂型
                Element JIXING=Rows.addElement("JIXING");
                JIXING.addText(replaceNullString(resultSet.getString("名称")));
                //拼音码
                Element PYM=Rows.addElement("PYM");
                PYM.addText(replaceNullString(""));
                //批准文号
                Element PIZHWH=Rows.addElement("PIZHWH");
                PIZHWH.addText(replaceNullString(resultSet.getString("批准文号")));
                //药品产地
                Element SHPCHD=Rows.addElement("SHPCHD");
                SHPCHD.addText(replaceNullString(resultSet.getString("产地")));
                //修改时间
                Element XGDATETIME=Rows.addElement("XGDATETIME");
                XGDATETIME.addText(replaceNullString(""));
                //代码
                Element CLASS_CODE=Rows.addElement("CLASS_CODE");
                CLASS_CODE.addText(replaceNullString(""));
                //合理用药审查的药品编号
                Element SPBH_PASS=Rows.addElement("SPBH_PASS");
                SPBH_PASS.addText(replaceNullString(""));
            }
            if (rows==0){
                fail();
            }
        }catch (Exception e){
            message+=e.getMessage();
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
        MESSAGE.setText("失败!"+message);
        Element Rows=Body.addElement("Rows");
        //商品 ID
        Element SPID=Rows.addElement("SPID");
        SPID.addText(replaceNullString(""));
        //商品编号
        Element SPBH=Rows.addElement("SPBH");
        SPBH.addText(replaceNullString(""));
        //商品名称
        Element SPMCH=Rows.addElement("SPMCH");
        SPMCH.addText(replaceNullString(""));
        //英文名称
        Element ENGLISH_NAME=Rows.addElement("ENGLISH_NAME");
        ENGLISH_NAME.addText(replaceNullString(""));
        //通用名称
        Element TONGYMCH=Rows.addElement("TONGYMCH");
        TONGYMCH.addText(replaceNullString(""));
        //商品规格
        Element SHPGG=Rows.addElement("SHPGG");
        SHPGG.addText(replaceNullString(""));
        //单位
        Element DW=Rows.addElement("DW");
        DW.addText(replaceNullString(""));
        //使用剂量
        Element USEJLGG=Rows.addElement("USEJLGG");
        USEJLGG.addText(replaceNullString(""));
        //使用单位
        Element USEDW=Rows.addElement("USEDW");
        USEDW.addText(replaceNullString(""));
        //大包装单位
        Element PACKET_DW=Rows.addElement("PACKET_DW");
        PACKET_DW.addText(replaceNullString(""));
        //剂型
        Element JIXING=Rows.addElement("JIXING");
        JIXING.addText(replaceNullString(""));
        //拼音码
        Element PYM=Rows.addElement("PYM");
        PYM.addText(replaceNullString(""));
        //批准文号
        Element PIZHWH=Rows.addElement("PIZHWH");
        PIZHWH.addText(replaceNullString(""));
        //药品产地
        Element SHPCHD=Rows.addElement("SHPCHD");
        SHPCHD.addText(replaceNullString(""));
        //修改时间
        Element XGDATETIME=Rows.addElement("XGDATETIME");
        XGDATETIME.addText(replaceNullString(""));
        //代码
        Element CLASS_CODE=Rows.addElement("CLASS_CODE");
        CLASS_CODE.addText(replaceNullString(""));
        //合理用药审查的药品编号
        Element SPBH_PASS=Rows.addElement("SPBH_PASS");
        SPBH_PASS.addText(replaceNullString(""));

    }
}
