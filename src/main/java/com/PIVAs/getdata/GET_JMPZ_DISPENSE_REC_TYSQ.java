package com.PIVAs.getdata;

import com.PIVAs.dbconnection.DatabaseConnection;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GET_JMPZ_DISPENSE_REC_TYSQ {
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
