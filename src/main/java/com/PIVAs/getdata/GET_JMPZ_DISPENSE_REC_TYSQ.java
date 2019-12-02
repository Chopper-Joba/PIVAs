package com.PIVAs.getdata;

import com.PIVAs.dbconnection.DatabaseConnection;
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

public class GET_JMPZ_DISPENSE_REC_TYSQ {
    private static final Logger LOG= LoggerFactory.getLogger(GET_JMPZ_DISPENSE_REC_TYSQ.class);
    Connection conn=null;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    Document document=null;
    private  String  seqId,sourceSystem,messageId;
    public String GET_JMPZ_DISPENSE_REC_TYSQ(Document requestxml){
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
        StringBuilder sql=new StringBuilder("select");
        sql.append(" a.NO as TYSEQ,a.填制日期  as TYSQ_DATE_TIME,a.配药日期 as DISPENSING_DATE_TIME,a.对方部门ID as ORDERED_BY,");
        sql.append(" b.病人ID as PATIENT_ID,b.主页ID as VISIT_ID,a.序号 as XH,null as ORDER_NO,a.序号 as ORDER_SUB_NO,a.药品ID as DRUG_CODE,c.规格 as DRUG_SPEC,");
        sql.append(" c.住院单位 as DRUG_UNITS,a.产地 as FIRM_ID,ABS(a.实际数量) as TYSQSL,ABS(a.零售金额) as COSTS,decode(d.医嘱期效,0,1,1,0,null)as REPEAT_INDICATOR,");
        sql.append(" d.医嘱内容 as ROUTENAME,d.开始执行时间 as USEDATE,null as USE_TIME,a.库房id as CKID");
        sql.append(" from 药品收发记录 a,住院费用记录 b,药品目录 c,病人医嘱记录 d");
        sql.append(" where a.记录状态=2 and a.费用ID=b.ID and a.药品ID=c.药品ID and b.病人id=d.病人id and b.主页id=d.主页id and a.药品id = d.收费细目id");

        long current = System.currentTimeMillis();
        long todyZero = current / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();
        long todyTwelve = todyZero + 24 * 60 * 60 * 1000 - 1;
        //昨天零点
        Date startTime=new Timestamp(todyZero-24 * 60 * 60 * 1000);
        //今晚12点
        Date endTime=new Timestamp(todyTwelve);
        sql.append(" and to_char(a.填制日期,'yyyy-mm-dd HH24:mm:ss')>=?");
        sql.append(" and to_char(a.填制日期,'yyyy-mm-dd HH24:mm:ss')<=?");
        LOG.info(sql.toString()+" 时间范围:"+startTime+"--"+endTime);
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
            while (resultSet.next()){
                Element Rows = Body.addElement("Rows");
                //退药序号
                Element TYSEQ=Rows.addElement("TYSEQ");
                TYSEQ.addText(replaceNullString(resultSet.getString("TYSEQ")));
                //日期
                Element TYSQ_DATE_TIME=Rows.addElement("TYSQ_DATE_TIME");
                TYSQ_DATE_TIME.addText(replaceNullString(resultSet.getString("TYSQ_DATE_TIME")));
                //摆药/发药日期及时间
                Element DISPENSING_DATE_TIME=Rows.addElement("DISPENSING_DATE_TIME");
                DISPENSING_DATE_TIME.addText(replaceNullString(resultSet.getString("DISPENSING_DATE_TIME")));
                //申请科室
                Element ORDERED_BY=Rows.addElement("ORDERED_BY");
                ORDERED_BY.addText(replaceNullString(resultSet.getString("ORDERED_BY")));
                //病人 ID
                Element PATIENT_ID=Rows.addElement("PATIENT_ID");
                PATIENT_ID.addText(replaceNullString(resultSet.getString("PATIENT_ID")));
                //本次住院标识
                Element VISIT_ID=Rows.addElement("VISIT_ID");
                VISIT_ID.addText(replaceNullString(resultSet.getString("VISIT_ID")));
                //序号
                Element XH=Rows.addElement("XH");
                XH.addText(replaceNullString(resultSet.getString("XH")));
                //医嘱序号
                Element ORDER_NO=Rows.addElement("ORDER_NO");
                ORDER_NO.addText(replaceNullString(resultSet.getString("ORDER_NO")));
                //医嘱子序号
                Element ORDER_SUB_NO=Rows.addElement("ORDER_SUB_NO");
                ORDER_SUB_NO.addText(replaceNullString(resultSet.getString("ORDER_SUB_NO")));
                //药品代码
                Element DRUG_CODE=Rows.addElement("DRUG_CODE");
                DRUG_CODE.addText(replaceNullString(resultSet.getString("DRUG_CODE")));
                //药品规格
                Element DRUG_SPEC=Rows.addElement("DRUG_SPEC");
                DRUG_SPEC.addText(replaceNullString(resultSet.getString("DRUG_SPEC")));
                //单位
                Element DRUG_UNITS=Rows.addElement("DRUG_UNITS");
                DRUG_UNITS.addText(replaceNullString(resultSet.getString("DRUG_UNITS")));
                //厂家/产地
                Element FIRM_ID=Rows.addElement("FIRM_ID");
                FIRM_ID.addText(replaceNullString(resultSet.getString("FIRM_ID")));
                //退 药 申 请 数 量
                Element TYSQSL=Rows.addElement("TYSQSL");
                TYSQSL.addText(replaceNullString(resultSet.getString("TYSQSL")));
                //退药费用
                Element COSTS=Rows.addElement("COSTS");
                COSTS.addText(replaceNullString(resultSet.getString("COSTS")));
                //长期/临时医嘱标志
                Element REPEAT_INDICATOR=Rows.addElement("REPEAT_INDICATOR");
                REPEAT_INDICATOR.addText(replaceNullString(resultSet.getString("REPEAT_INDICATOR")));
                //给药途径和方法
                Element ROUTENAME=Rows.addElement("ROUTENAME");
                ROUTENAME.addText(replaceNullString(resultSet.getString("ROUTENAME")));
                //用药日期
                Element USEDATE=Rows.addElement("USEDATE");
                USEDATE.addText(replaceNullString(resultSet.getString("USEDATE")));
                //用药时间
                Element USE_TIME=Rows.addElement("USE_TIME");
                USE_TIME.addText(replaceNullString(resultSet.getString("USE_TIME")));
                //退药仓库
                Element CKID=Rows.addElement("CKID");
                CKID.addText(replaceNullString(resultSet.getString("CKID")));
            }
            if (resultSet.getRow()==0){
                fail();
            }
        }catch (Exception e){
            LOG.error(e.getMessage());
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
        MESSAGE.setText("失败");
        Element Rows=Body.addElement("Rows");
        //退药序号
        Element TYSEQ=Rows.addElement("TYSEQ");
        TYSEQ.addText(replaceNullString(""));
        //日期
        Element TYSQ_DATE_TIME=Rows.addElement("TYSQ_DATE_TIME");
        TYSQ_DATE_TIME.addText(replaceNullString(""));
        //摆药/发药日期及时间
        Element DISPENSING_DATE_TIME=Rows.addElement("DISPENSING_DATE_TIME");
        DISPENSING_DATE_TIME.addText(replaceNullString(""));
        //申请科室
        Element ORDERED_BY=Rows.addElement("ORDERED_BY");
        ORDERED_BY.addText(replaceNullString(""));
        //病人 ID
        Element PATIENT_ID=Rows.addElement("PATIENT_ID");
        PATIENT_ID.addText(replaceNullString(""));
        //本次住院标识
        Element VISIT_ID=Rows.addElement("VISIT_ID");
        VISIT_ID.addText(replaceNullString(""));
        //序号
        Element XH=Rows.addElement("XH");
        XH.addText(replaceNullString(""));
        //医嘱序号
        Element ORDER_NO=Rows.addElement("ORDER_NO");
        ORDER_NO.addText(replaceNullString(""));
        //医嘱子序号
        Element ORDER_SUB_NO=Rows.addElement("ORDER_SUB_NO");
        ORDER_SUB_NO.addText(replaceNullString(""));
        //药品代码
        Element DRUG_CODE=Rows.addElement("DRUG_CODE");
        DRUG_CODE.addText(replaceNullString(""));
        //药品规格
        Element DRUG_SPEC=Rows.addElement("DRUG_SPEC");
        DRUG_SPEC.addText(replaceNullString(""));
        //单位
        Element DRUG_UNITS=Rows.addElement("DRUG_UNITS");
        DRUG_UNITS.addText(replaceNullString(""));
        //厂家/产地
        Element FIRM_ID=Rows.addElement("FIRM_ID");
        FIRM_ID.addText(replaceNullString(""));
        //退 药 申 请 数 量
        Element TYSQSL=Rows.addElement("TYSQSL");
        TYSQSL.addText(replaceNullString(""));
        //退药费用
        Element COSTS=Rows.addElement("COSTS");
        COSTS.addText(replaceNullString(""));
        //长期/临时医嘱标志
        Element REPEAT_INDICATOR=Rows.addElement("REPEAT_INDICATOR");
        REPEAT_INDICATOR.addText(replaceNullString(""));
        //给药途径和方法
        Element ROUTENAME=Rows.addElement("ROUTENAME");
        ROUTENAME.addText(replaceNullString(""));
        //用药日期
        Element USEDATE=Rows.addElement("USEDATE");
        USEDATE.addText(replaceNullString(""));
        //用药时间
        Element USE_TIME=Rows.addElement("USE_TIME");
        USE_TIME.addText(replaceNullString(""));
        //退药仓库
        Element CKID=Rows.addElement("CKID");
        CKID.addText(replaceNullString(""));
    }
}
