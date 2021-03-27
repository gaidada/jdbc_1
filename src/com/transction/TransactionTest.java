package com.transction;

import com.bean.User;
import com.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionTest {
    /*
     * AA用户给BB转账100
     * */
    @Test
    public void test1() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            //取消数据的自动取消
            conn.setAutoCommit(false);
//            System.out.println(conn.getAutoCommit());
            String sql1 = "update user_table set balance = balance -100 where user=?";
            update2(conn, sql1, "AA");
            //模拟异常
            System.out.println(10 / 0);

            String sql2 = "update user_table set balance = balance +100 where user=?";
            update2(conn, sql2, "BB");
            System.out.println("转账成功");
            //提交数据
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            //回滚数据
            try {
                conn.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } finally {
            JDBCUtils.closetResource(conn, null);
        }
    }

    //通用的增删改操作--version1.0
    public int update(String sql, Object... args) {//sql中占位符的个数与可变形参的长度相同
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
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5.关闭资源
            JDBCUtils.closetResource(conn, ps);
        }
        return 0;
    }

    //考虑事务后的操作--version2.0
    public int update2(Connection conn, String sql, Object... args) {
        PreparedStatement ps = null;
        try {
            //2.预编译sql语句，返回PreparedStatement的实例
            ps = conn.prepareStatement(sql);
            //3.填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);//!!!!
            }
            //4.执行
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //主要针对于数据库连接池时修改其为自动提交数据
            try {
                conn.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            //5.关闭资源
            JDBCUtils.closetResource(null, ps);
        }
        return 0;
    }


    //事务
    @Test
    public void test2() throws Exception {
        Connection conn = JDBCUtils.getConnection();
        //获取当前连接的隔离级别
        System.out.println(conn.getTransactionIsolation());
        //取消自动提交
        conn.setAutoCommit(false);
        String sql = "select user,password,balance from user_table where user=?";
        List<User> users = getInstance(conn, User.class, sql, "CC");
        users.forEach(System.out::println);
    }

    @Test
    public void test3() throws Exception {
        Connection conn = JDBCUtils.getConnection();
        //取消自动提交
        conn.setAutoCommit(false);
        String sql = "update user_table set balance =? where user=?";
        update2(conn, sql, 8888, "CC");

        Thread.sleep(15000);
        System.out.println("修改结束");
    }

    //通用的查询操作，用于返回数据表中的记录--考虑事务version2.0
    public <T> List<T> getInstance(Connection conn, Class<T> clazz, String sql, Object... args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            rs = ps.executeQuery();
            //获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();

            int columnCount = rsmd.getColumnCount();
            //创建集合对象
            ArrayList<T> list = new ArrayList<>();
            while (rs.next()) {
                T t = clazz.newInstance();
                //处理一行结果集中的每个列
                for (int i = 0; i < columnCount; i++) {
                    //获取列值
                    Object columnValue = rs.getObject(i + 1);
                    //获取每个列的列名
                    String columnName = rsmd.getColumnName(i + 1);
                    //给cust对象指定的columnName属性，赋值为columnValue，通过反射
                    Field field = clazz.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closetResource(null, ps, rs);
        }
        return null;
    }
}
