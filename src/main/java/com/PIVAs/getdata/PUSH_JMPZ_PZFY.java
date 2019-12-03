package com.PIVAs.getdata;

import com.PIVAs.dao.PZFY;
import com.PIVAs.dbconnection.DatabaseConnection;
import com.PIVAs.util.ReplaceNullStringUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Date;
import java.util.List;

public class PUSH_JMPZ_PZFY {
    private static final Logger LOG = LoggerFactory.getLogger(PUSH_JMPZ_PZFY.class);
    Connection conn=null;
    Document document=null;
    PZFY pzfy=new PZFY();
    private  String  sourceSystem,messageId;
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
        sourceSystem=ReplaceNullStringUtil.replaceNullString(sourcesystem.getText());
        Element messageid=root.element("Header").element("MessageID");
        //获取入参MessageID的值
        messageId=ReplaceNullStringUtil.replaceNullString(messageid.getText());
        //封装返回数据头
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
        CODE.setText(ReplaceNullStringUtil.replaceNullString("0"));
        Element MESSAGE = Body.addElement("MESSAGE");
        MESSAGE.setText("成功");
        Element SEQID=Body.addElement("SEQID");
        //获取参数数组
        List<Element> rows=root.element("Body").element("rows").elements("row");
        //循环获取数据调用存储过程存入数据库
        for (Element row : rows) {
            pzfy.setID(Integer.valueOf(ReplaceNullStringUtil.replaceNullString(row.element("ID").getText())));
            pzfy.setPATIENT_ID(ReplaceNullStringUtil.replaceNullString(row.element("PATIENT_ID").getText()));
            pzfy.setVISIT_ID(ReplaceNullStringUtil.replaceNullString(row.element("VISIT_ID").getText()));
            pzfy.setORDER_ID(ReplaceNullStringUtil.replaceNullString(row.element("ORDER_ID").getText()));
            pzfy.setDepartment_no(ReplaceNullStringUtil.replaceNullString(row.element("Department_no").getText()));
            pzfy.setItem_no(ReplaceNullStringUtil.replaceNullString(row.element("Item_no").getText()));
            pzfy.setDevNo(ReplaceNullStringUtil.replaceNullString(row.element("DevNo").getText()));
            pzfy.setNum(Integer.valueOf(ReplaceNullStringUtil.replaceNullString(row.element("Num").getText())));
            pzfy.setCosts(new BigDecimal(row.element("Costs").getText()));
            pzfy.setDEMO(ReplaceNullStringUtil.replaceNullString(row.element("DEMO").getText()));
            try {
                LOG.error(row.element("Createtime").getText());
                pzfy.setCreatetime(DateUtils.parseDate(row.element("Createtime").getText(),"yyy-MM-dd HH24:MM:SS"));

            } catch (Exception e) {
                LOG.error(e.getMessage());
                return e.getMessage();
            }
            pzfy.setSEQID(ReplaceNullStringUtil.replaceNullString(row.element("SEQID").getText()));
            SEQID.setText(pzfy.getSEQID());
            try {
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
                proc.setString(11,pzfy.getCreatetime().toString());
                proc.setString(12,pzfy.getSEQID());
                Boolean successINS =proc.execute();
                if (successINS){
                    //HIS收费时间
                    Element ROWS=Body.addElement("ROWS");
                    Element UpdateDate=ROWS.addElement("UpdateDate");
                    UpdateDate.setText(new Date().toString());
                    //HIS 系统收费执行标志
                    Element Flag=ROWS.addElement("Flag");
                    Flag.setText(ReplaceNullStringUtil.replaceNullString("0"));
                    //ID
                    Element ID=ROWS.addElement("ID");
                    ID.setText(ReplaceNullStringUtil.replaceNullString(String.valueOf(pzfy.getID())));
                }else {
                    LOG.error("存储过程:PUSH_JMPZ_PZFY_INS 数据存储失败");
                    fail();
                }
            }catch (Exception e){
                LOG.error(e.getMessage());
                fail();
                return e.getMessage();
            }
        }
        return document.asXML();
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
        UpdateDate.setText(new Date().toString());
        //HIS 系统收费执行标志
        Element Flag=Body.addElement("Flag");
        Flag.setText(ReplaceNullStringUtil.replaceNullString("1"));
        //ID
        Element ID=Body.addElement("ID");
        ID.setText(ReplaceNullStringUtil.replaceNullString(String.valueOf(pzfy.getID())));
    }
}
