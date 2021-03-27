package com.preparedstatement.crud;

import com.util.JDBCUtils;
import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/*
 * 使用PreparedStatement替代Statement，实现对数据表的增删改查操作
 *
 * 增删改；查
 * */
public class PreparedStatementUpdteTest {
    //向customers表中添加一条记录
    @Test
    public void test1() {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
            Properties properties = new Properties();
            properties.load(is);
            String user = properties.getProperty("user");
            String password = properties.getProperty("password");
            String url = properties.getProperty("url");
            String driverClass = properties.getProperty("driverClass");

            Class.forName(driverClass);

            connection = DriverManager.getConnection(url, user, password);
            System.out.println(connection);

            //预编译sql语句，返回PreparedStatement实例
            String sql = "insert into customers(name,email,birth) values(?,?,?)";
            ps = connection.prepareStatement(sql);
            //填充占位符
            ps.setString(1, "Nike");
            ps.setString(2, "nike@126.com");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse("2000-01-01");
            ps.setDate(3, new java.sql.Date(date.getTime()));

            //执行操作
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //资源关闭
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }

    //修改customers表中的一条记录
    @Test
    public void test2() throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //获取数据库的连接
            conn = JDBCUtils.getConnection();
            //预编译sql语句，返回PreparedStatement的实例
            String sql = "update customers set email=? where id = ?";
            ps = conn.prepareStatement(sql);
            //填充占位符
            ps.setObject(1, "123@163.com");
            ps.setObject(2, 18);
            //执行
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //资源的关闭
            JDBCUtils.closetResource(conn, ps);
        }
    }

    @Test
    public void testUpdate(){
//        String sql="delete from customers where id=?";
//        update(sql,3);
        String sql = "update `order` set order_name=? where order_id =?";
        update(sql,"DD",2);
    }

    //通用的增删改操作
    public void update(String sql, Object... args)  {//sql中占位符的个数与可变形参的长度相同
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //1.获取数据库的连接
            conn = JDBCUtils.getConnection();
            //2.预编译sql语句，返回PreparedStatement的实例
            ps = conn.prepareStatement(sql);
            //3.填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);//!!!!
            }
            //4.执行
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5.关闭资源
            JDBCUtils.closetResource(conn, ps);
        }
    }


}
