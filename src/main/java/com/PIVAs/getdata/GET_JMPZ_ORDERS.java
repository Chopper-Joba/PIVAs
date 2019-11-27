package com.PIVAs.getdata;

import com.PIVAs.dbconnection.DatabaseConnection;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GET_JMPZ_ORDERS {
    Connection conn=null;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    Document document=null;
    private  String  seqId,sourceSystem,messageId;
    public String GET_JMPZ_ORDERS(Document requestxml){
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
        //获取入参PATIENT_ID的值
        Element patientid=root.element("Body").element("PATIENT_ID");
        String patientId=patientid.getText();
        //获取入参VISIT_ID的值
        Element visitid=root.element("Body").element("VISIT_ID");
        String visitId=visitid.getText();
        //获取入参DEPT_CODE的值
        Element deptcode=root.element("Body").element("DEPT_CODE");
        String deptCode=deptcode.getText();
        //获取入参START_QUERY_TIME的值
        Element starttime=root.element("Body").element("START_QUERY_TIME");
        String startTime=starttime.getText();
        //获取入参END_QUERY_TIME
        Element endtime=root.element("Body").element("START_QUERY_TIME");
        String endTime=endtime.getText();
        String sql= "\"select a.收费细目id as ITEM_CODE,\\n\" + \n" +
                "\"       a.单次用量 as AMOUNT,\\n\" + \n" +
                "\"       a.病人id as PATIENT_ID,\\n\" + \n" +
                "\"       a.主页id as VISIT_ID,\\n\" + \n" +
                "\"       null as ORDER_NO,\\n\" + \n" +
                "\"       a.序号 as ORDER_SUB_NO,\\n\" + \n" +
                "\"       decode(a.医嘱期效, 1, 0, 0, 1, null) as REPEAT_INDICATOR,\\n\" + \n" +
                "\"       b.名称 as ORDER_CLASS,\\n\" + \n" +
                "\"       a.收费细目id as ORDER_CODE,\\n\" + \n" +
                "\"       a.单次用量 as DOSAGE,\\n\" + \n" +
                "\"       c.住院单位 as DOSAGE_UNITS,\\n\" + \n" +
                "\"       null as ROUTENAME,\\n\" + \n" +
                "\"       a.开始执行时间 as START_DATE_TIME,\\n\" + \n" +
                "\"       a.校对时间 as AUDITING_DATE,\\n\" + \n" +
                "\"       a.停嘱时间 as STOP_DATE_TIME,\\n\" + \n" +
                "\"       a.上次执行时间 as LAST_PERFORM_DATE_TIME,\\n\" + \n" +
                "\"       null as DURATION,\\n\" + \n" +
                "\"       null as DURATION_UNITS,\\n\" + \n" +
                "\"       a.执行频次 as FREQUENCY,/*修改为编码*/\\n\" + \n" +
                "\"       a.频率次数 as  FREQ_COUNTER,\\n\" + \n" +
                "\"       a.频率间隔 as FREQ_INTERVAL,\\n\" + \n" +
                "\"       a.间隔单位 as FREQ_INTERVAL_UNIT,\\n\" + \n" +
                "\"       a.医生嘱托 as FREQ_DETAIL,\\n\" + \n" +
                "\"       a.开始执行时间 as  PERFORM_SCHEDULE,\\n\" + \n" +
                "\"       d.名称 as ORDERING_DEPT,\\n\" + \n" +
                "\"       a.开嘱医生 as DOCTOR,\\n\" + \n" +
                "\"       a.停嘱医生 as STOP_DOCTOR,\\n\" + \n" +
                "\"       a.医嘱状态 as ORDER_STATUS,\\n\" + \n" +
                "\"       decode(a.计价特性,0,0,1,3,2,2,null),\\n\" + \n" +
                "\"       null as LYFS,/*后面补充*/\\n\" + \n" +
                "\"       null as END_DATE,/*后面补充*/\\n\" + \n" +
                "\"       null as START_DATE,/*后面补充*/\\n\" + \n" +
                "\"       a.校对护士 as  CHECKER,\\n\" + \n" +
                "\"       a.校对时间 as CHECKER_DATE,\\n\" + \n" +
                "\"       a.执行科室id as DEPT_CODE,\\n\" + \n" +
                "\"       null as CKID,\\n\" + \n" +
                "\"       null as PZFS_HIS,/*后面补充*/\\n\" + \n" +
                "\"       null as PZPC_HIS,\\n\" + \n" +
                "\"      a.医嘱内容 as  ORDER_NAME\\n\" + \n" +
                "\"\\n\" + \n" +
                "\"  from 病人医嘱记录 a, 诊疗项目类别 b, 药品目录 c,部门表 d\\n\" + \n" +
                "\" where a.诊疗类别=b.编码\\n\" + \n" +
                "\" and a.收费细目id=c.药品id\\n\" + \n" +
                "\" and a.执行科室id =d.id\";\n";
    if(deptCode==null||deptCode.equals(""))
    {
        sql+=" and a.开嘱时间> to_date( '"+starttime+"','yyyy-mm-dd hh24:mi:ss')";
        sql+=" and ";
    }
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
                //项目代码
                Element ITEM_CODE=Rows.addElement("ITEM_CODE");
                ITEM_CODE.addText(replaceNullString(""));
                //项目数量
                Element AMOUNT=Rows.addElement("AMOUNT");
                AMOUNT.addText(replaceNullString(""));
                //病人 ID
                Element PATIENT_ID=Rows.addElement("PATIENT_ID");
                PATIENT_ID.addText(replaceNullString(""));
                //本次住院标识
                Element VISIT_ID=Rows.addElement("VISIT_ID");
                VISIT_ID.addText(replaceNullString(""));
                //医嘱组号
                Element ORDER_NO=Rows.addElement("ORDER_NO");
                ORDER_NO.addText(replaceNullString(""));
                //医嘱子序号
                Element ORDER_SUB_NO=Rows.addElement("ORDER_SUB_NO");
                ORDER_SUB_NO.addText(replaceNullString(""));
                //长期/临时医嘱标志
                Element REPEAT_INDICATOR=Rows.addElement("REPEAT_INDICATOR");
                REPEAT_INDICATOR.addText(replaceNullString(""));
                //医嘱类别（1）
                Element ORDER_CLASS=Rows.addElement("ORDER_CLASS");
                ORDER_CLASS.addText(replaceNullString(""));
                //药品代码
                Element ORDER_CODE=Rows.addElement("ORDER_CODE");
                ORDER_CODE.addText(replaceNullString(""));
                //药品一次使用剂量（计量）
                Element DOSAGE=Rows.addElement("DOSAGE");
                DOSAGE.addText(replaceNullString(""));
                //剂量单位（计量）
                Element DOSAGE_UNITS=Rows.addElement("DOSAGE_UNITS");
                DOSAGE_UNITS.addText(replaceNullString(""));
                //给药途径和方法
                Element ROUTENAME=Rows.addElement("ROUTENAME");
                ROUTENAME.addText(replaceNullString(""));
                //医嘱开始日期及时间（确认时间）
                Element START_DATE_TIME=Rows.addElement("START_DATE_TIME");
                START_DATE_TIME.addText(replaceNullString(""));
                //医嘱发送到静配中心时间
                Element AUDITING_DATE=Rows.addElement("AUDITING_DATE");
                AUDITING_DATE.addText(replaceNullString(""));
                //停止日期及时间（停止时间）
                Element STOP_DATE_TIME=Rows.addElement("STOP_DATE_TIME");
                STOP_DATE_TIME.addText(replaceNullString(""));
                //摆药截止日期时间
                Element LAST_PERFORM_DATE_TIME=Rows.addElement("LAST_PERFORM_DATE_TIME");
                LAST_PERFORM_DATE_TIME.addText(replaceNullString(""));
                //持续时间
                Element DURATION=Rows.addElement("DURATION");
                DURATION.addText(replaceNullString(""));
                //持续时间单位
                Element DURATION_UNITS=Rows.addElement("DURATION_UNITS");
                DURATION_UNITS.addText(replaceNullString(""));
                //执行频率描述(频次)
                Element FREQUENCY=Rows.addElement("FREQUENCY");
                FREQUENCY.addText(replaceNullString(""));
                //频率次数
                Element FREQ_COUNTER=Rows.addElement("FREQ_COUNTER");
                FREQ_COUNTER.addText(replaceNullString(""));
                //频率间隔
                Element FREQ_INTERVAL=Rows.addElement("FREQ_INTERVAL");
                FREQ_INTERVAL.addText(replaceNullString(""));
                //频率间隔单位（单位固定为“日”）
                Element FREQ_INTERVAL_UNIT=Rows.addElement("FREQ_INTERVAL_UNIT");
                FREQ_INTERVAL_UNIT.addText(replaceNullString(""));
                //执行时间详细描述(医嘱详细描述)（null）
                Element FREQ_DETAIL=Rows.addElement("FREQ_DETAIL");
                FREQ_DETAIL.addText(replaceNullString(""));
                //医嘱执行时间
                Element PERFORM_SCHEDULE=Rows.addElement("PERFORM_SCHEDULE");
                PERFORM_SCHEDULE.addText(replaceNullString(""));
                //开医嘱科室名称汉字
                Element ORDERING_DEPT=Rows.addElement("ORDERING_DEPT");
                ORDERING_DEPT.addText(replaceNullString(""));
                //医生姓名
                Element DOCTOR=Rows.addElement("DOCTOR");
                DOCTOR.addText(replaceNullString(""));
                //停医嘱医生
                Element STOP_DOCTOR=Rows.addElement("STOP_DOCTOR");
                STOP_DOCTOR.addText(replaceNullString(""));
                //医嘱状态
                Element ORDER_STATUS=Rows.addElement("ORDER_STATUS");
                ORDER_STATUS.addText(replaceNullString(""));
                //计价属性
                Element BILLING_ATTR=Rows.addElement("BILLING_ATTR");
                BILLING_ATTR.addText(replaceNullString(""));
                //医院要求只能次日长期医嘱为“C”临时医嘱为“D”
                Element LYFS=Rows.addElement("LYFS");
                LYFS.addText(replaceNullString(""));
                //停止日期（不带时间的结束日期）
                Element END_DATE=Rows.addElement("END_DATE");
                END_DATE.addText(replaceNullString(""));
                //开始日期（不带时间的开始日期）
                Element START_DATE=Rows.addElement("START_DATE");
                START_DATE.addText(replaceNullString(""));
                // 审查人
                Element CHECKER=Rows.addElement("CHECKER");
                CHECKER.addText(replaceNullString(""));
                // 审查日期
                Element CHECKER_DATE=Rows.addElement("CHECKER_DATE");
                CHECKER_DATE.addText(replaceNullString(""));
                // 科室代码
                Element DEPT_CODE=Rows.addElement("DEPT_CODE");
                DEPT_CODE.addText(replaceNullString(""));
                // 申请发药仓库ID
                Element CKID=Rows.addElement("CKID");
                CKID.addText(replaceNullString(""));
                //药品配置方式(1 是冲配、2 领 配)
                Element PZFS_HIS=Rows.addElement("PZFS_HIS");
                PZFS_HIS.addText(replaceNullString(""));
                //药品配置批次(1,2,3,4,5)
                Element PZPC_HIS=Rows.addElement("PZPC_HIS");
                PZPC_HIS.addText(replaceNullString(""));
                //医嘱名称
                Element ORDER_NAME=Rows.addElement("ORDER_NAME");
                ORDER_NAME.addText(replaceNullString(""));
            }
        }catch (Exception e){

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
        Element ITEM_CODE=Rows.addElement("ITEM_CODE");
        ITEM_CODE.addText(replaceNullString(""));
        //项目数量
        Element AMOUNT=Rows.addElement("AMOUNT");
        AMOUNT.addText(replaceNullString(""));
        //病人 ID
        Element PATIENT_ID=Rows.addElement("PATIENT_ID");
        PATIENT_ID.addText(replaceNullString(""));
        //本次住院标识
        Element VISIT_ID=Rows.addElement("VISIT_ID");
        VISIT_ID.addText(replaceNullString(""));
        //医嘱组号
        Element ORDER_NO=Rows.addElement("ORDER_NO");
        ORDER_NO.addText(replaceNullString(""));
        //医嘱子序号
        Element ORDER_SUB_NO=Rows.addElement("ORDER_SUB_NO");
        ORDER_SUB_NO.addText(replaceNullString(""));
        //长期/临时医嘱标志
        Element REPEAT_INDICATOR=Rows.addElement("REPEAT_INDICATOR");
        REPEAT_INDICATOR.addText(replaceNullString(""));
        //医嘱类别（1）
        Element ORDER_CLASS=Rows.addElement("ORDER_CLASS");
        ORDER_CLASS.addText(replaceNullString(""));
        //药品代码
        Element ORDER_CODE=Rows.addElement("ORDER_CODE");
        ORDER_CODE.addText(replaceNullString(""));
        //药品一次使用剂量（计量）
        Element DOSAGE=Rows.addElement("DOSAGE");
        DOSAGE.addText(replaceNullString(""));
        //剂量单位（计量）
        Element DOSAGE_UNITS=Rows.addElement("DOSAGE_UNITS");
        DOSAGE_UNITS.addText(replaceNullString(""));
        //给药途径和方法
        Element ROUTENAME=Rows.addElement("ROUTENAME");
        ROUTENAME.addText(replaceNullString(""));
        //医嘱开始日期及时间（确认时间）
        Element START_DATE_TIME=Rows.addElement("START_DATE_TIME");
        START_DATE_TIME.addText(replaceNullString(""));
        //医嘱发送到静配中心时间
        Element AUDITING_DATE=Rows.addElement("AUDITING_DATE");
        AUDITING_DATE.addText(replaceNullString(""));
        //停止日期及时间（停止时间）
        Element STOP_DATE_TIME=Rows.addElement("STOP_DATE_TIME");
        STOP_DATE_TIME.addText(replaceNullString(""));
        //摆药截止日期时间
        Element LAST_PERFORM_DATE_TIME=Rows.addElement("LAST_PERFORM_DATE_TIME");
        LAST_PERFORM_DATE_TIME.addText(replaceNullString(""));
        //持续时间
        Element DURATION=Rows.addElement("DURATION");
        DURATION.addText(replaceNullString(""));
        //持续时间单位
        Element DURATION_UNITS=Rows.addElement("DURATION_UNITS");
        DURATION_UNITS.addText(replaceNullString(""));
        //执行频率描述(频次)
        Element FREQUENCY=Rows.addElement("FREQUENCY");
        FREQUENCY.addText(replaceNullString(""));
        //频率次数
        Element FREQ_COUNTER=Rows.addElement("FREQ_COUNTER");
        FREQ_COUNTER.addText(replaceNullString(""));
        //频率间隔
        Element FREQ_INTERVAL=Rows.addElement("FREQ_INTERVAL");
        FREQ_INTERVAL.addText(replaceNullString(""));
        //频率间隔单位（单位固定为“日”）
        Element FREQ_INTERVAL_UNIT=Rows.addElement("FREQ_INTERVAL_UNIT");
        FREQ_INTERVAL_UNIT.addText(replaceNullString(""));
        //执行时间详细描述(医嘱详细描述)（null）
        Element FREQ_DETAIL=Rows.addElement("FREQ_DETAIL");
        FREQ_DETAIL.addText(replaceNullString(""));
        //医嘱执行时间
        Element PERFORM_SCHEDULE=Rows.addElement("PERFORM_SCHEDULE");
        PERFORM_SCHEDULE.addText(replaceNullString(""));
        //开医嘱科室名称汉字
        Element ORDERING_DEPT=Rows.addElement("ORDERING_DEPT");
        ORDERING_DEPT.addText(replaceNullString(""));
        //医生姓名
        Element DOCTOR=Rows.addElement("DOCTOR");
        DOCTOR.addText(replaceNullString(""));
        //停医嘱医生
        Element STOP_DOCTOR=Rows.addElement("STOP_DOCTOR");
        STOP_DOCTOR.addText(replaceNullString(""));
        //医嘱状态
        Element ORDER_STATUS=Rows.addElement("ORDER_STATUS");
        ORDER_STATUS.addText(replaceNullString(""));
        //计价属性
        Element BILLING_ATTR=Rows.addElement("BILLING_ATTR");
        BILLING_ATTR.addText(replaceNullString(""));
        //医院要求只能次日长期医嘱为“C”临时医嘱为“D”
        Element LYFS=Rows.addElement("LYFS");
        LYFS.addText(replaceNullString(""));
        //停止日期（不带时间的结束日期）
        Element END_DATE=Rows.addElement("END_DATE");
        END_DATE.addText(replaceNullString(""));
        //开始日期（不带时间的开始日期）
        Element START_DATE=Rows.addElement("START_DATE");
        START_DATE.addText(replaceNullString(""));
        // 审查人
        Element CHECKER=Rows.addElement("CHECKER");
        CHECKER.addText(replaceNullString(""));
        // 审查日期
        Element CHECKER_DATE=Rows.addElement("CHECKER_DATE");
        CHECKER_DATE.addText(replaceNullString(""));
        // 科室代码
        Element DEPT_CODE=Rows.addElement("DEPT_CODE");
        DEPT_CODE.addText(replaceNullString(""));
        // 申请发药仓库ID
        Element CKID=Rows.addElement("CKID");
        CKID.addText(replaceNullString(""));
        //药品配置方式(1 是冲配、2 领 配)
        Element PZFS_HIS=Rows.addElement("PZFS_HIS");
        PZFS_HIS.addText(replaceNullString(""));
        //药品配置批次(1,2,3,4,5)
        Element PZPC_HIS=Rows.addElement("PZPC_HIS");
        PZPC_HIS.addText(replaceNullString(""));
        //医嘱名称
        Element ORDER_NAME=Rows.addElement("ORDER_NAME");
        ORDER_NAME.addText(replaceNullString(""));
    }
}
