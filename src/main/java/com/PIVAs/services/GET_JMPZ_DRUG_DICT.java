package com.PIVAs.services;

import com.PIVAs.util.DBUtil;
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
    String errMessage="";
    private  String  seqId,sourceSystem,messageId;
    public String GET_JMPZ_DRUG_DICT(Document requestxml){
        try {
            conn = DBUtil.getConnection();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            errMessage+= "数据库连接失败！";
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
        String sql= "select a.药品id   as SPID,\n" +
                        "       a.编码     as SPBH,\n" +
                        "       a.名称     as SPMCH,\n" +
                        "       null       as ENGLISH_NAME,\n" +
                        "       c.通用名称 as TONGYMCH,\n" +
                        "       a.规格     as SHPGG,\n" +
                        "       a.售价单位 as DW,\n" +
                        "       a.剂量系数 as USEJLGG,\n" +
                        "        c.剂量单位   as USEDW,\n" +
                        "       a.药库单位 as PACKET_DW,\n" +
                        "       b.名称 as  JIXING,\n" +
                        "       zlspellcode( a.名称) as PYM,"+
                        "       a.批准文号 as PIZHWH,\n" +
                        "       a.产地 as SHPCHD,\n" +
                        "       null as XGDATETIME,\n" +
                        "       null  as CLASS_CODE,\n" +
                        "       null as SPBH_PASS\n" +
                        "  from 药品目录 a, 药品剂型 b, 药品信息 c\n" +
                        "  where a.药名ID = c.药名ID\n" +
                        "   and c.剂型 = b.编码\n" +
                        "   and b.编码 in (2, 3, 13, 20, 21, 28, 32)";
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
                SPID.addText(replaceNullString(resultSet.getString("SPID")));
                //商品编号
                Element SPBH=Rows.addElement("SPBH");
                SPBH.addText(replaceNullString(resultSet.getString("SPBH")));
                //商品名称
                Element SPMCH=Rows.addElement("SPMCH");
                SPMCH.addText(replaceNullString(resultSet.getString("SPMCH")));
                //英文名称
                Element ENGLISH_NAME=Rows.addElement("ENGLISH_NAME");
                ENGLISH_NAME.addText(replaceNullString("ENGLISH_NAME"));
                //通用名称
                Element TONGYMCH=Rows.addElement("TONGYMCH");
                TONGYMCH.addText(replaceNullString(resultSet.getString("TONGYMCH")));
                //商品规格
                Element SHPGG=Rows.addElement("SHPGG");
                SHPGG.addText(replaceNullString(resultSet.getString("SHPGG")));
                //单位
                Element DW=Rows.addElement("DW");
                DW.addText(replaceNullString(resultSet.getString("DW")));
                //使用剂量
                Element USEJLGG=Rows.addElement("USEJLGG");
                USEJLGG.addText(replaceNullString(String.valueOf(resultSet.getDouble("USEJLGG"))));
               // System.out.println(String.valueOf(resultSet.getDouble("USEJLGG")));
                //使用单位
                Element USEDW=Rows.addElement("USEDW");
                USEDW.addText(replaceNullString(resultSet.getString("USEDW")));
                //大包装单位
                Element PACKET_DW=Rows.addElement("PACKET_DW");
                PACKET_DW.addText(replaceNullString(resultSet.getString("PACKET_DW")));
                //剂型
                Element JIXING=Rows.addElement("JIXING");
                JIXING.addText(replaceNullString(resultSet.getString("JIXING")));
                //拼音码
                Element PYM=Rows.addElement("PYM");
                PYM.addText(replaceNullString("PYM"));
                //批准文号
                Element PIZHWH=Rows.addElement("PIZHWH");
                PIZHWH.addText(replaceNullString(resultSet.getString("PIZHWH")));
                //药品产地
                Element SHPCHD=Rows.addElement("SHPCHD");
                SHPCHD.addText(replaceNullString(resultSet.getString("SHPCHD")));
                //修改时间
                Element XGDATETIME=Rows.addElement("XGDATETIME");
                XGDATETIME.addText(replaceNullString(resultSet.getString("XGDATETIME")));
                //代码
                Element CLASS_CODE=Rows.addElement("CLASS_CODE");
                CLASS_CODE.addText(replaceNullString(resultSet.getString("CLASS_CODE")));
                //合理用药审查的药品编号
                Element SPBH_PASS=Rows.addElement("SPBH_PASS");
                SPBH_PASS.addText(replaceNullString(resultSet.getString("SHPCHD")));
            }
            if (rows==0){
                fail();
                errMessage+="没有查询到数据！";
            }
        }catch (Exception e){
            errMessage+=e.getMessage();
            fail();
        }
        return document.asXML();
    }
    public  String replaceNullString(String str){
//        if (str==null){
        if ("".equals(str)||str==null){
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
        MESSAGE.setText("失败!"+errMessage);
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
