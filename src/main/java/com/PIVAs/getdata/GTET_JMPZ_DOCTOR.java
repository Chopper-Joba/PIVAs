package com.PIVAs.getdata;

import com.PIVAs.dbconnection.DatabaseConnection;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GTET_JMPZ_DOCTOR {
    Connection conn=null;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    Document document=null;
    private  String  seqId,sourceSystem,messageId;
    public String  getDoctor( Document requestxml){
            try {
                conn = DatabaseConnection.getConnection();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                return "数据库连接失败！";
            }
            Element root=requestxml.getRootElement();
            Element seqid=root.element("Body").element("SEQID");
//           获取入参的SEQID节点的值
             seqId=replaceNullString(seqid.getText());
            Element sourcesystem=root.element("Header").element("SourceSystem");
//            获取入参SourceSystem的节点的值
            sourceSystem=replaceNullString(sourcesystem.getText());
            Element messageid=root.element("Header").element("MessageID");
//            获取入参MessageID的值
             messageId=replaceNullString(messageid.getText());
            String sql="select a.id as aid,a.姓名,b.id as bid,b.名称,c.部门id,c.人员id,d.人员性质 from 人员表 a,部门表 b,部门人员 c ,人员性质说明 d " +
                    "where a.id=c.人员id and b.id=c.部门id and a.id=d.人员id and d.人员性质='医生' and c.人员id=d.人员id";
            try{
                document= DocumentHelper.createDocument();
                document.setXMLEncoding("utf-8");
                preparedStatement=conn.prepareStatement(sql);
                resultSet=preparedStatement.executeQuery();
                Element Request=document.addElement("Request");
                Element Header=Request.addElement("Header");
                //获取入参sourcesystem
                Element SourceSystem=Header.addElement("SourceSystem");
                SourceSystem.setText(sourceSystem);
                //获取入参MessageID
                Element MessageID=Header.addElement("MessageID");
                MessageID.setText(messageId);
                Element Body=Request.addElement("Body");
                Element CODE=Body.addElement("CODE");
                CODE.setText(replaceNullString("0"));
                Element MESSAGE=Body.addElement("MESSAGE");
                MESSAGE.setText("成功");
                Element SEQID=Body.addElement("SEQID");
                SEQID.setText(seqId);
                int rows=0;
                while (resultSet.next()){
                    rows++;
                    Element Rows=Body.addElement("Rows");
                    //医生编码
                    Element DOCTOR_NO=Rows.addElement("DOCTOR_NO");
                    DOCTOR_NO.setText(replaceNullString(resultSet.getString("aid")));
                    //医生名称
                    Element DOCTOR_NAME=Rows.addElement("DOCTOR_NAME");
                    DOCTOR_NAME.setText(replaceNullString(resultSet.getString("姓名")));
                    //部门编号
                    Element DEPARTMENT_NO=Rows.addElement("DEPARTMENT_NO");
                    DEPARTMENT_NO.setText(replaceNullString(resultSet.getString("bid")));
                    //部门名称
                    Element DEPARTMENTNAME=Rows.addElement("DEPARTMENTNAME");
                    DEPARTMENTNAME.setText(replaceNullString(resultSet.getString("名称")));
                    //备注
                    Element BEIZHU=Rows.addElement("BEIZHU");
                    BEIZHU.setText(replaceNullString(""));
                }
                if (rows==0){
                    fail();
                }
            }catch (Exception e){
                fail();
            }finally {
                try {
                    conn.close();
                    preparedStatement.close();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            return  document.asXML();
        }

        public  String replaceNullString(String str){
        if (str==null){
            return "";
        }
        else
            return str;
        }
      public void fail(){
        document=DocumentHelper.createDocument();
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
        Element DOCTOR_NO=Rows.addElement("DOCTOR_NO");
        DOCTOR_NO.setText(replaceNullString(""));
        Element DOCTOR_NAME=Rows.addElement("DOCTOR_NAME");
        DOCTOR_NAME.setText(replaceNullString(""));
        Element DEPARTMENT_NO=Rows.addElement("DEPARTMENT_NO");
        DEPARTMENT_NO.setText(replaceNullString(""));
        Element DEPARTMENTNAME=Rows.addElement("DEPARTMENTNAME");
        DEPARTMENTNAME.setText(replaceNullString(""));
        Element BEIZHU=Rows.addElement("BEIZHU");
        BEIZHU.setText(replaceNullString(""));
    }

}
