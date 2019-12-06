package com.PIVAs.services;

import com.PIVAs.entity.PZFY;
import com.PIVAs.util.DBUtil;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PUSH_JMPZ_PZFY {
    private static final Logger LOG = LoggerFactory.getLogger(PUSH_JMPZ_PZFY.class);
    Connection conn=null;
    Document document=null;
    PZFY pzfy=new PZFY();
    private  String  sourceSystem,messageId;
    CallableStatement proc=null;
    StringBuilder errMessage=new StringBuilder("");
    public String PUSH_JMPZ_PZFY(Document requestxml){
        try {
            conn = DBUtil.getConnection();
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
                    pzfy.setCreatetime(DateUtils.parseDate(row.element("Createtime").getText(),"yyyy-MM-dd HH:mm:ss"));
                } catch (Exception e) {
                    errMessage.append(e.getMessage());
                }
                pzfy.setSEQID(ReplaceNullStringUtil.replaceNullString(row.element("SEQID").getText()));
                SEQID.setText(pzfy.getSEQID());
                try {
                    proc = conn.prepareCall("{call PUSH_JMPZ_PZFY_INS(?,?,?,?,?,?,?,?,?,?,to_date(?,'yyyy-MM-dd HH24:mi:ss'),?)}");
                    proc.setInt(1,pzfy.getID());
                    proc.setString(2,pzfy.getPATIENT_ID());
                    proc.setString(3,pzfy.getVISIT_ID());
                    proc.setString(4,pzfy.getORDER_ID());
                    proc.setString(5,pzfy.getDepartment_no());
                    proc.setString(6,pzfy.getItem_no());
                    proc.setString(7,pzfy.getDevNo());
                    proc.setInt(8,pzfy.getNum());
                    proc.setBigDecimal(9,pzfy.getCosts());
                    proc.setString(10, pzfy.getDEMO());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    proc.setString(11, sdf.format(pzfy.getCreatetime()));
                    proc.setString(12, pzfy.getSEQID());
                    proc.execute();
                    //HIS收费时间
                    Element ROWS = Body.addElement("ROWS");
                    Element UpdateDate = ROWS.addElement("UpdateDate");
                    UpdateDate.setText(sdf.format(new Date()));
                    //HIS 系统收费执行标志
                    Element Flag = ROWS.addElement("Flag");
                    Flag.setText(ReplaceNullStringUtil.replaceNullString("0"));
                    //ID
                    Element ID = ROWS.addElement("ID");
                    ID.setText(ReplaceNullStringUtil.replaceNullString(String.valueOf(pzfy.getID())));
                }catch (Exception e){
                    errMessage.append(e.getMessage());
                    fail();
                    return e.getMessage();
                }
            }
        } catch (IOException e1) {
            return "数据库连接失败！";
        }finally {
            DBUtil.close(conn);
            DBUtil.close(proc);
        }
        LOG.error(errMessage.toString());
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
        UpdateDate.setText("");
        //HIS 系统收费执行标志
        Element Flag=Body.addElement("Flag");
        Flag.setText(ReplaceNullStringUtil.replaceNullString("1"));
        //ID
        Element ID=Body.addElement("ID");
        ID.setText("");
    }
}
