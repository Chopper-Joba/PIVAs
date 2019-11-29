package com.PIVAs.dbconnection;

import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DatabaseConnection {
    // 创建一个数据库连接
    public static Connection getConnection() throws IOException
    {
        Connection connection = null;
        try {
            Properties properties = PropertiesLoaderUtils.loadAllProperties("application.properties");
            String driver = properties.getProperty("datasource.drive");
            String url = properties.getProperty("datasource.url");
            String user = properties.getProperty("datasource.username");
            String password = properties.getProperty("datasource.password");
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
            //System.out.println("成功连接数据库");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return connection;
    }
    //关闭资源
    public static void close(Connection conn ) {
        try {
            if (conn != null) {
                conn.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void close(PreparedStatement preparedStatement) {
        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void close(ResultSet resultSet ) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void close(Connection conn,PreparedStatement preparedStatement,ResultSet resultSet){
        close(conn);
        close(preparedStatement);
        close(resultSet);
    }
}
