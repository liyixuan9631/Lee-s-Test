package com.lemon.util;

import com.lemon.data.Constants;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @Project: PortAutomation
 * @Site: http://www.lemonban.com
 * @Forum: http://testingpai.com
 * @Copyright: ©2020 版权所有 湖南省零檬信息技术有限公司
 * @Author: Administrator
 * @Create: 2021-04-25 23:28
 * @Desc：
 **/

public class JDBCUtils {
    /**
    * 实现功能描述：建立数据库连接
    * @param
    * @return 返回数据库连接
    */
    public static Connection getConnection() {
        //定义数据库连接
        //Oracle：jdbc:oracle:thin:@localhost:1521:DBName
        //SqlServer：jdbc:microsoft:sqlserver://localhost:1433; DatabaseName=DBName
        // MySql：jdbc:mysql://localhost:3306/DBName
        String url = "jdbc:mysql://" + Constants.DB_BASE_URI + Constants.DB_NAME + "?useUnicode=true&characterEncoding=utf-8";
        String user = Constants.DB_USERNAME;
        String password = Constants.DB_PWD;
        //定义数据库连接对象
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url,user,password);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
    * 实现功能描述：关闭数据库连接
    * @param connection 数据库连接对象
    * @return void
    */
    public static void closeConnection(Connection connection){
        //判空
        if(connection != null){
            //关闭数据库连接
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /**
    * 实现功能描述：sql的更新操作（增删改）
    * @param sql
    * @return void
    */
    public static void update(String sql) {
        Connection connection = getConnection();
        QueryRunner queryRunner =new QueryRunner();
        try {
            queryRunner.update(connection,sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            //关闭数据库连接
            closeConnection(connection);
        }
    }

    /**
    * 实现功能描述：查询所有的结果集
    * @param sql
    * @return 返回的结果集
    */
    public static List<Map<String,Object>> queryAll( String sql){
        Connection connection = getConnection();
        QueryRunner queryRunner = new QueryRunner();
        List<Map<String,Object>> result = null;
        try {
            result = queryRunner.query(connection,sql,new MapListHandler());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            closeConnection(connection);
        }
        return result;
    }

    /**
     * 实现功能描述：查询结果集中的第一条
     * @param sql
     * @return 返回的结果
     */
    public static Map<String,Object> queryOne( String sql){
        Connection connection = getConnection();
        QueryRunner queryRunner = new QueryRunner();
        Map<String,Object> result = null;
        try {
            result = queryRunner.query(connection,sql,new MapHandler());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            closeConnection(connection);
        }
        return result;
    }

    /**
     * 实现功能描述：查询单挑的数据
     * @param sql
     * @return 返回的结果
     */
    public static Object querySingleData( String sql){
        Connection connection = getConnection();
        QueryRunner queryRunner = new QueryRunner();
        Object result = null;
        try {
            result = queryRunner.query(connection,sql,new ScalarHandler<Object>());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            closeConnection(connection);
        }
        return result;
    }
}
