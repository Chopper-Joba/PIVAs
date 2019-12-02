package com.PIVAs.getdata;

import com.PIVAs.dao.PZFY;
import com.PIVAs.dbconnection.DatabaseConnection;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class PUSH_JMPZ_PZFY {
    private static final Logger LOG = LoggerFactory.getLogger(GET_JMPZ_DISPENSE_REC_COUNT.class);
    Connection conn=null;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    Document document=null;
    PZFY pzfy=new PZFY();
    private  String  seqId,sourceSystem,messageId;
    CallableStatement proc=null;
    public String PUSH_JMPZ_PZFY(Document requestxml){
        try {
            conn = DatabaseConnection.getConnection();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            return "数据库连接失败！";
        }
        Element root=requestxml.getRootElement();
        Element sourcesystem=root.element("Header").element("SourceSystem");
        //获取入参SourceSystem的节点的值
        sourceSystem=replaceNullString(sourcesystem.getText());
        Element messageid=root.element("Header").element("MessageID");
        //获取入参MessageID的值
        messageId=replaceNullString(messageid.getText());

        //获取入参ID的值
        Element id=root.element("Body").element("ID");
        pzfy.setID(Integer.parseInt(id.getText()));
        //获取入参PATIENT_ID的值
        Element patientid=root.element("Body").element("PATIENT_ID");
        pzfy.setPATIENT_ID(patientid.getText());
        //获取入参VISIT_ID
        Element vicitid=root.element("Body").element("VISIT_ID");
        pzfy.setVISIT_ID(vicitid.getText());
        //获取入参ORDER_ID的值
        Element orderid=root.element("Body").element("ORDER_ID");
        pzfy.setORDER_ID(orderid.getText());

        //获取入参Department_no的值
        Element departmentno=root.element("Body").element("Department_no");
        pzfy.setDepartment_no(departmentno.getText());
        //获取入参Item_no的值
        Element itemno=root.element("Body").element("Item_no");
        pzfy.setItem_no(itemno.getText());
        //获取入参DevNo的值
        Element devno=root.element("Body").element("DevNo");
        pzfy.setDevNo(devno.getText());
        //获取入参Num的值
        Element num=root.element("Body").element("Num");
        pzfy.setNum(Integer.parseInt(num.getText()));
        //获取入参Costs的值
        Element costs=root.element("Body").element("Costs");
        pzfy.setCosts(new BigDecimal(costs.getText()));
        //获取入参DEMO的值
        Element demo=root.element("Body").element("DEMO");
        pzfy.setDEMO(demo.getText());
        //获取入参Createtime的值
        Element createtime=root.element("Body").element("Createtime");
        try {
            pzfy.setCreatetime(new SimpleDateFormat("yyyy-mm-dd hh:mi:ss").parse(createtime.getText()));
        }catch (Exception e){
            e.printStackTrace();
        }
        //获取入参SEQID
        Element seqid=root.element("Body").element("SEQID");
        pzfy.setSEQID(seqid.getText());
        /*
        将回传的数据插入OUSH_JMPZ_PZFY表
         */
        try {
            document = DocumentHelper.createDocument();
            document.setXMLEncoding("utf-8");
            proc = conn.prepareCall("{call PUSH_JMPZ_PZFY_INS(?,?,?,?,?,?,?,?,?,?,?,?)}");
            proc.setInt(1,pzfy.getID());
            proc.setString(2,pzfy.getPATIENT_ID());
            proc.setString(3,pzfy.getVISIT_ID());
            proc.setString(4,pzfy.getORDER_ID());
            proc.setString(5,pzfy.getDepartment_no());
            proc.setString(6,pzfy.getItem_no());
            proc.setString(7,pzfy.getDevNo());
            proc.setInt(8,pzfy.getNum());
            proc.setBigDecimal(9,pzfy.getCosts());
            proc.setString(10,pzfy.getDEMO());
            proc.setDate(11, (java.sql.Date) pzfy.getCreatetime());
            proc.setString(12,pzfy.getSEQID());

            LOG.warn(pzfy.toString());

            Boolean successINS =proc.execute();
            if (successINS){
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
                //HIS收费时间
                Element UpdateDate=Body.addElement("UpdateDate");
                UpdateDate.setText(replaceNullString(pzfy.getCreatetime().toString()));
                //HIS 系统收费执行标志
                Element Flag=Body.addElement("Flag");
                Flag.setText(replaceNullString("0"));
                //ID
                Element ID=Body.addElement("ID");
                ID.setText(replaceNullString(String.valueOf(pzfy.getID())));
            }else {
                LOG.error("存储过程:PUSH_JMPZ_PZFY_INS 数据存储失败");
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
        Element UpdateDate=Body.addElement("UpdateDate");
        UpdateDate.setText(replaceNullString(""));
        //HIS 系统收费执行标志
        Element Flag=Body.addElement("Flag");
        Flag.setText(replaceNullString(""));
        //ID
        Element ID=Body.addElement("ID");
        ID.setText(replaceNullString(String.valueOf(pzfy.getID())));
    }
}
