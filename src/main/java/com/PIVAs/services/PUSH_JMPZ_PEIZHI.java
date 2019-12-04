package com.PIVAs.services;

import com.PIVAs.entity.PEIZHI;
import com.PIVAs.util.DBUtil;
import com.PIVAs.util.ReplaceNullStringUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PUSH_JMPZ_PEIZHI {
    private static final Logger LOG = LoggerFactory.getLogger(PUSH_JMPZ_PEIZHI.class);
    Connection conn=null;
    Document document=null;
    CallableStatement proc=null;
    private  String  seqId,sourceSystem,messageId;
    PEIZHI peizhi=new PEIZHI();
    public String PUSH_JMPZ_PEIZHI(Document requestxml){
        try {
            conn = DBUtil.getConnection();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            return "数据库连接失败！";
        }
        Element root=requestxml.getRootElement();
        //获取入参SourceSystem的节点的值
        Element sourcesystem=root.element("Header").element("SourceSystem");
        sourceSystem= ReplaceNullStringUtil.replaceNullString(sourcesystem.getText());
        //获取入参MessageID的值
        Element messageid=root.element("Header").element("MessageID");
        messageId=ReplaceNullStringUtil.replaceNullString(messageid.getText());
        //获取参数数组
        List<Element> rows=root.element("Body").element("rows").elements("row");
        for (Element row : rows) {
            //费用序号
            peizhi.setXH(Integer.valueOf(row.element("XH").getText()));
            //获取入参的PATIENT_ID,病人ID
            peizhi.setPATIENT_ID(row.element("PATIENT_ID").getText());
            //住院次数,VISIT_ID
            peizhi.setVISIT_ID(row.element("VISIT_ID").getText());
            //医嘱号,ORDER_NO
            peizhi.setORDER_NO(row.element("ORDER_NO").getText());
            //使用日期,USE_DATE
            peizhi.setUSE_DATE(row.element("USE_DATE").getText());
            //使用时间,USE_TIME
            peizhi.setUSE_TIME(row.element("USE_TIME").getText());
            //药品id,SPID
            peizhi.setSPID(row.element("SPID").getText());
            //配置药品数量,SHL_PEIZHI
            peizhi.setSHL_PEIZHI(new BigDecimal(row.element("SHL_PEIZHI").getText()));
            //SEQID
            seqId=row.element("SEQID").getText();
            peizhi.setSEQID(seqId);
            try {
                proc = conn.prepareCall("{call PUSH_JMPZ_PEIZHI_INS(?,?,?,?,?,?,?,?,?)}");
                proc.setInt(1, (Integer) peizhi.getXH());
                proc.setString(2, peizhi.getPATIENT_ID());
                proc.setString(3, peizhi.getVISIT_ID());
                proc.setString(4, peizhi.getORDER_NO());
                proc.setString(5, peizhi.getUSE_DATE());
                proc.setString(6, peizhi.getUSE_TIME());
                proc.setString(7, peizhi.getSPID());
                proc.setBigDecimal(8, peizhi.getSHL_PEIZHI());
                proc.setString(9, peizhi.getSEQID());
                proc.execute();
                //初始化响应头
                if(document==null){
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
                    SEQID.setText(seqId);
                }
            }catch (Exception e){
                LOG.error(e.getMessage());
                fail();
            }
            finally {
                DBUtil.close(conn);
                try {
                    proc.close();
                } catch (SQLException e) {
                    LOG.error(e.getMessage());
                }
            }
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
        CODE.setText(ReplaceNullStringUtil.replaceNullString("0"));
        Element MESSAGE = Body.addElement("MESSAGE");
        MESSAGE.setText("失败");
        Element SEQID=Body.addElement("SEQID");
        SEQID.setText("");
    }
}
