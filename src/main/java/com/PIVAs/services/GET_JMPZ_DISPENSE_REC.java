package com.PIVAs.services;

import com.PIVAs.util.DBUtil;
import com.PIVAs.util.ReplaceNullStringUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GET_JMPZ_DISPENSE_REC {
    Connection conn=null;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    Document document=null;
   StringBuilder errMessage=new StringBuilder("");
    private static final Logger LOG = LoggerFactory.getLogger(GET_JMPZ_DISPENSE_REC.class);
    private  String  seqId,sourceSystem,messageId,despensingXH,dispensingDateTime;
    public String GET_JMPZ_DISPENSE_REC(Document requestxml){
        try {
            conn = DBUtil.getConnection();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            return "数据库连接失败！";
        }
        Element root=requestxml.getRootElement();
        //获取入参的SEQID节点的值
        Element seqid=root.element("Body").element("SEQID");
        seqId=ReplaceNullStringUtil.replaceNullString(seqid.getText());
        //获取入参SourceSystem的节点的值
        Element sourcesystem=root.element("Header").element("SourceSystem");
        sourceSystem=ReplaceNullStringUtil.replaceNullString(sourcesystem.getText());
        //获取入参MessageID的值
        Element messageid=root.element("Header").element("MessageID");
        messageId=ReplaceNullStringUtil.replaceNullString(messageid.getText());
        //DISPENSING_XH
        Element DISPENSING_xh=root.element("Body").element("DISPENSING_XH");
        despensingXH=ReplaceNullStringUtil.replaceNullString(DISPENSING_xh.getText());
        //DISPENSING_DATE_TIME
        Element dispensing_date_time=root.element("Body").element("DISPENSING_DATE_TIME");
        dispensingDateTime=ReplaceNullStringUtil.replaceNullString(dispensing_date_time.getText());
        String sql="select\n" +
                "a.no as DISPENSING_XH,\n" +
                "a.库房id as DISPENSARY,\n" +
                "a.库房id as CKID,\n" +
                "to_char(a.审核日期,'yyyy-mm-dd') as RQ,\n" +
                "a.审核日期 as DISPENSING_DATE_TIME,\n" +
                "a.对方部门id as ORDERED_BY,\n" +
                "b.病人id as PATIENT_ID,\n" +
                "b.主页id as VISIT_ID,\n" +
                "a.序号 as XH,\n" +
                "null as ORDER_NO,\n" +
                "a.序号 as ORDER_SUB_NO,\n" +
                "a.药品id as DRUG_CODE,\n" +
                "c.规格 as DRUG_SPEC,\n" +
                "c.住院单位 as DRUG_UNITS,\n" +
                "a.产地 as FIRM_ID,\n" +
                "a.实际数量 as DISPENSE_AMOUNT,\n" +
                "a.零售金额 as COSTS,\n" +
                "decode(d.医嘱期效,0,1,1,0,null)as REPEAT_INDICATOR,\n" +
                "1 as FYLX,\n" +
                "e.医嘱内容 as ROUTENAME,\n" +
                "a.配药人 as DISPENSING_PROVIDER,\n" +
                "null as SRRY,\n" +
                "null as YEBZ,\n" +
                "null as YPGLBZ,\n" +
                "d.开始执行时间 as USEDATE,\n" +
                "null as USE_TIME,\n" +
                "a.填制日期 as SHENQ_DATE_TIME,\n" +
                "d.执行频次 as FREQUENCY,\n" +
                "d.医嘱内容 as ORDER_NAME,\n" +
                " null as TWO_CODE\n" +
                "from 药品收发记录 a, 住院费用记录 b, 药品目录 c, 病人医嘱记录 d, 病人医嘱记录 e\n" +
                "where a.费用id=b.id\n" +
                "and a.药品id=c.药品id\n" +
                "and b.病人id=d.病人id\n" +
                "and b.主页id=d.主页id\n" +
                "and a.药品id = d.收费细目id\n" +
                "and d.相关id= e.id\n" +
                "and a.入出系数=-1"+
                "and a.no=?"+
                "and  a.审核日期=to_date(?,'yyyy-mm-dd hh24:mi:ss')";
        try {
            document = DocumentHelper.createDocument();
            document.setXMLEncoding("utf-8");
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,despensingXH);
            preparedStatement.setString(2,dispensingDateTime);
            resultSet = preparedStatement.executeQuery();
            Element Request = document.addElement("Request");
            Element Header = Request.addElement("Header");
            Element SourceSystem = Header.addElement("SourceSystem");
            SourceSystem.setText(sourceSystem);
            Element MessageID = Header.addElement("MessageID");
            MessageID.setText(messageId);
            Element Body = Request.addElement("Body");
            Element CODE = Body.addElement("CODE");
            CODE.setText(ReplaceNullStringUtil.replaceNullString("0"));
            Element MESSAGE = Body.addElement("MESSAGE");
            MESSAGE.setText("成功");
            Element SEQID = Body.addElement("SEQID");
            SEQID.setText(seqId);
            while (resultSet.next()) {
                Element Rows = Body.addElement("Rows");
                //摆药序号
                Element DISPENSING_XH=Rows.addElement("DISPENSING_XH");
                DISPENSING_XH.addText(ReplaceNullStringUtil.replaceNullString(resultSet.getString("DISPENSING_XH")));
                //发药药房 ID
                Element DISPENSARY=Rows.addElement("DISPENSARY");
                DISPENSARY.addText(ReplaceNullStringUtil.replaceNullString(resultSet.getString("DISPENSARY")));
                //日期
                Element RQ=Rows.addElement("RQ");
                RQ.addText(ReplaceNullStringUtil.replaceNullString(resultSet.getString("RQ")));
                //摆药/发药日期及时间
                Element DISPENSING_DATE_TIME=Rows.addElement("DISPENSING_DATE_TIME");
                DISPENSING_DATE_TIME.addText(ReplaceNullStringUtil.replaceNullString(resultSet.getString("DISPENSING_DATE_TIME")));
                //申请科室
                Element ORDERED_BY=Rows.addElement("ORDERED_BY");
                ORDERED_BY.addText(ReplaceNullStringUtil.replaceNullString(resultSet.getString("ORDERED_BY")));
                //病人 ID
                Element PATIENT_ID=Rows.addElement("PATIENT_ID");
                PATIENT_ID.addText(ReplaceNullStringUtil.replaceNullString(resultSet.getString("PATIENT_ID")));
                //本次住院标识
                Element VISIT_ID=Rows.addElement("VISIT_ID");
                VISIT_ID.addText(ReplaceNullStringUtil.replaceNullString(resultSet.getString("VISIT_ID")));
                //费用序号
                Element XH=Rows.addElement("XH");
                XH.addText(ReplaceNullStringUtil.replaceNullString(resultSet.getString("XH")));
                //医嘱序号
                Element ORDER_NO=Rows.addElement("ORDER_NO");
                ORDER_NO.addText(ReplaceNullStringUtil.replaceNullString(resultSet.getString("ORDER_NO")));
                //医嘱子序号
                Element ORDER_SUB_NO=Rows.addElement("ORDER_SUB_NO");
                ORDER_SUB_NO.addText(ReplaceNullStringUtil.replaceNullString(resultSet.getString("ORDER_SUB_NO")));
                //药品代码
                Element DRUG_CODE=Rows.addElement("DRUG_CODE");
                DRUG_CODE.addText(ReplaceNullStringUtil.replaceNullString(resultSet.getString("DRUG_CODE")));
                //药品规格
                Element DRUG_SPEC=Rows.addElement("DRUG_SPEC");
                DRUG_SPEC.addText(ReplaceNullStringUtil.replaceNullString(resultSet.getString("DRUG_SPEC")));
                //单位
                Element DRUG_UNITS=Rows.addElement("DRUG_UNITS");
                DRUG_UNITS.addText(ReplaceNullStringUtil.replaceNullString(resultSet.getString("DRUG_UNITS")));
                //厂家/产地
                Element FIRM_ID=Rows.addElement("FIRM_ID");
                FIRM_ID.addText(ReplaceNullStringUtil.replaceNullString(resultSet.getString("FIRM_ID")));
                //发药数量
                Element DISPENSE_AMOUNT=Rows.addElement("DISPENSE_AMOUNT");
                DISPENSE_AMOUNT.addText(ReplaceNullStringUtil.replaceNullString(resultSet.getString("DISPENSE_AMOUNT")));
                //费用
                Element COSTS=Rows.addElement("COSTS");
                COSTS.addText(ReplaceNullStringUtil.replaceNullString(resultSet.getString("COSTS")));
                //长期/临时医嘱标志
                Element REPEAT_INDICATOR=Rows.addElement("REPEAT_INDICATOR");
                REPEAT_INDICATOR.addText(ReplaceNullStringUtil.replaceNullString(resultSet.getString("REPEAT_INDICATOR")));
                //发药类型
                Element FYLX=Rows.addElement("FYLX");
                FYLX.addText(ReplaceNullStringUtil.replaceNullString(resultSet.getString("FYLX")));
                //给药途径和方法
                Element ROUTENAME=Rows.addElement("ROUTENAME");
                ROUTENAME.addText(ReplaceNullStringUtil.replaceNullString(resultSet.getString("ROUTENAME")));
                //发药人员
                Element DISPENSING_PROVIDER=Rows.addElement("DISPENSING_PROVIDER");
                DISPENSING_PROVIDER.addText(ReplaceNullStringUtil.replaceNullString(resultSet.getString("DISPENSING_PROVIDER")));
                //输入人员
                Element SRRY=Rows.addElement("SRRY");
                SRRY.addText(ReplaceNullStringUtil.replaceNullString(resultSet.getString("SRRY")));
                //婴儿标记
                Element YEBZ=Rows.addElement("YEBZ");
                YEBZ.addText(ReplaceNullStringUtil.replaceNullString(resultSet.getString("YEBZ")));
                //药品管理标志
                Element YPGLBZ=Rows.addElement("YPGLBZ");
                YPGLBZ.addText(ReplaceNullStringUtil.replaceNullString(resultSet.getString("YPGLBZ")));
                //用药日期
                Element USEDATE=Rows.addElement("USEDATE");
                USEDATE.addText(ReplaceNullStringUtil.replaceNullString(resultSet.getString("USEDATE")));
                //用药时间
                Element USE_TIME=Rows.addElement("USE_TIME");
                USE_TIME.addText(ReplaceNullStringUtil.replaceNullString(resultSet.getString("USE_TIME")));
                //申请日期
                Element SHENQ_DATE_TIME=Rows.addElement("SHENQ_DATE_TIME");
                SHENQ_DATE_TIME.addText(ReplaceNullStringUtil.replaceNullString(resultSet.getString("SHENQ_DATE_TIME")));
                //执行频率描述(频次)
                Element FREQUENCY=Rows.addElement("FREQUENCY");
                FREQUENCY.addText(ReplaceNullStringUtil.replaceNullString(resultSet.getString("FREQUENCY")));
                //医嘱名称或者药品名称(频次)
                Element ORDER_NAME=Rows.addElement("ORDER_NAME");
                ORDER_NAME.addText(ReplaceNullStringUtil.replaceNullString(resultSet.getString("ORDER_NAME")));
                //二维码
                Element TWO_CODE=Rows.addElement("TWO_CODE");
                TWO_CODE.addText(ReplaceNullStringUtil.replaceNullString(resultSet.getString("TWO_CODE")));
            }
            if (resultSet.getRow()==0){
                errMessage.append("没有查询到数据!");
                fail();
            }
        }catch (Exception e){
            fail();
        }finally {
         DBUtil.close(conn,preparedStatement,resultSet);
        }
        LOG.error(errMessage.toString());
        return document.asXML();
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
        //摆药序号
        Element DISPENSING_XH=Rows.addElement("DISPENSING_XH");
        DISPENSING_XH.addText(ReplaceNullStringUtil.replaceNullString(""));
        //发药药房 ID
        Element DISPENSARY=Rows.addElement("DISPENSARY");
        DISPENSARY.addText(ReplaceNullStringUtil.replaceNullString(""));
        //日期
        Element RQ=Rows.addElement("RQ");
        RQ.addText(ReplaceNullStringUtil.replaceNullString(""));
        //摆药/发药日期及时间
        Element DISPENSING_DATE_TIME=Rows.addElement("DISPENSING_DATE_TIME");
        DISPENSING_DATE_TIME.addText(ReplaceNullStringUtil.replaceNullString(""));
        //申请科室
        Element ORDERED_BY=Rows.addElement("ORDERED_BY");
        ORDERED_BY.addText(ReplaceNullStringUtil.replaceNullString(""));
        //病人 ID
        Element PATIENT_ID=Rows.addElement("PATIENT_ID");
        PATIENT_ID.addText(ReplaceNullStringUtil.replaceNullString(""));
        //本次住院标识
        Element VISIT_ID=Rows.addElement("VISIT_ID");
        VISIT_ID.addText(ReplaceNullStringUtil.replaceNullString(""));
        //费用序号
        Element XH=Rows.addElement("XH");
        XH.addText(ReplaceNullStringUtil.replaceNullString(""));
        //医嘱序号
        Element ORDER_NO=Rows.addElement("ORDER_NO");
        ORDER_NO.addText(ReplaceNullStringUtil.replaceNullString(""));
        //医嘱子序号
        Element ORDER_SUB_NO=Rows.addElement("ORDER_SUB_NO");
        ORDER_SUB_NO.addText(ReplaceNullStringUtil.replaceNullString(""));
        //药品代码
        Element DRUG_CODE=Rows.addElement("DRUG_CODE");
        DRUG_CODE.addText(ReplaceNullStringUtil.replaceNullString(""));
        //药品规格
        Element DRUG_SPEC=Rows.addElement("DRUG_SPEC");
        DRUG_SPEC.addText(ReplaceNullStringUtil.replaceNullString(""));
        //单位
        Element DRUG_UNITS=Rows.addElement("DRUG_UNITS");
        DRUG_UNITS.addText(ReplaceNullStringUtil.replaceNullString(""));
        //厂家/产地
        Element FIRM_ID=Rows.addElement("FIRM_ID");
        FIRM_ID.addText(ReplaceNullStringUtil.replaceNullString(""));
        //发药数量
        Element DISPENSE_AMOUNT=Rows.addElement("DISPENSE_AMOUNT");
        DISPENSE_AMOUNT.addText(ReplaceNullStringUtil.replaceNullString(""));
        //费用
        Element COSTS=Rows.addElement("COSTS");
        COSTS.addText(ReplaceNullStringUtil.replaceNullString(""));
        //长期/临时医嘱标志
        Element REPEAT_INDICATOR=Rows.addElement("REPEAT_INDICATOR");
        REPEAT_INDICATOR.addText(ReplaceNullStringUtil.replaceNullString(""));
        //发药类型
        Element FYLX=Rows.addElement("FYLX");
        FYLX.addText(ReplaceNullStringUtil.replaceNullString(""));
        //给药途径和方法
        Element ROUTENAME=Rows.addElement("ROUTENAME");
        ROUTENAME.addText(ReplaceNullStringUtil.replaceNullString(""));
        //发药人员
        Element DISPENSING_PROVIDER=Rows.addElement("DISPENSING_PROVIDER");
        DISPENSING_PROVIDER.addText(ReplaceNullStringUtil.replaceNullString(""));
        //输入人员
        Element SRRY=Rows.addElement("SRRY");
        SRRY.addText(ReplaceNullStringUtil.replaceNullString(""));
        //婴儿标记
        Element YEBZ=Rows.addElement("YEBZ");
        YEBZ.addText(ReplaceNullStringUtil.replaceNullString(""));
        //药品管理标志
        Element YPGLBZ=Rows.addElement("YPGLBZ");
        YPGLBZ.addText(ReplaceNullStringUtil.replaceNullString(""));
        //用药日期
        Element USEDATE=Rows.addElement("USEDATE");
        USEDATE.addText(ReplaceNullStringUtil.replaceNullString(""));
        //用药时间
        Element USE_TIME=Rows.addElement("USE_TIME");
        USE_TIME.addText(ReplaceNullStringUtil.replaceNullString(""));
        //申请日期
        Element SHENQ_DATE_TIME=Rows.addElement("SHENQ_DATE_TIME");
        SHENQ_DATE_TIME.addText(ReplaceNullStringUtil.replaceNullString(""));
        //执行频率描述(频次)
        Element FREQUENCY=Rows.addElement("FREQUENCY");
        FREQUENCY.addText(ReplaceNullStringUtil.replaceNullString(""));
        //医嘱名称或者药品名称(频次)
        Element ORDER_NAME=Rows.addElement("ORDER_NAME");
        ORDER_NAME.addText(ReplaceNullStringUtil.replaceNullString(""));
        //二维码
        Element TWO_CODE=Rows.addElement("TWO_CODE");
        TWO_CODE.addText(ReplaceNullStringUtil.replaceNullString(""));
    }
}
