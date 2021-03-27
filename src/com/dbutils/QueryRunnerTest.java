package com.dbutils;


import com.bean.Customer;
import com.connection.JDBCUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;

import java.beans.Transient;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class QueryRunnerTest {
    //插入
    @Test
    public void testInert() throws SQLException {
        QueryRunner runner = new QueryRunner();
        Connection connection = JDBCUtil.getConnection3();
        String sql = "insert into customers (name,email,birth) values(?,?,?)";
        int inertCount = runner.update(connection, sql, "lilili", "lili@li.com", "1997-02-06");
        System.out.println(inertCount);
    }

    //查询,BeanHandler是ResultSetHandler接口的实现类，用于封装表中的一条记录
    @Test
    public void testQuery1() throws SQLException {
        QueryRunner runner = new QueryRunner();
        Connection connection = JDBCUtil.getConnection3();
        String sql = "select id,name,email,birth from customers where id =? ";
        BeanHandler<Customer> handler = new BeanHandler<Customer>(Customer.class);
        Customer customer = runner.query(connection, sql, handler, 13);
        System.out.println(customer);
    }

    @Test
    public void testQuery2() throws SQLException {
        QueryRunner runner = new QueryRunner();
        Connection connection = JDBCUtil.getConnection3();
        String sql = "select id,name,email,birth from customers where id <? ";
        BeanListHandler<Customer> handler = new BeanListHandler<Customer>(Customer.class);
        List<Customer> customers = runner.query(connection, sql, handler, 13);
        customers.forEach(System.out::println);
    }

    @Test
    public void testQuery3() throws SQLException {
        QueryRunner runner = new QueryRunner();
        Connection connection = JDBCUtil.getConnection3();
        String sql = "select id,name,email,birth from customers where id =? ";
        MapHandler handler = new MapHandler();
        Map<String, Object> map = runner.query(connection, sql, handler, 13);
        System.out.println(map);
    }

    @Test
    public void test4() throws SQLException {
        QueryRunner runner = new QueryRunner();
        Connection connection = JDBCUtil.getConnection3();
        String sql = "select count(*) from customers";
        ScalarHandler h = new ScalarHandler();
        Long count = (Long) runner.query(connection, sql, h);
        System.out.println(count);
    }

    //自定义ResultSetHandler的类
    @Test
    public void test5() throws SQLException {
        QueryRunner runner = new QueryRunner();
        Connection connection = JDBCUtil.getConnection3();
        String sql = "select id,name,email,birth from customers where id =? ";
        ResultSetHandler<Customer> handler = new ResultSetHandler<>() {
            @Override
            public Customer handle(ResultSet resultSet) throws SQLException {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String email = resultSet.getString("email");
                    Date birth = resultSet.getDate("birth");
                    Customer customer = new Customer(id, name, email, birth);
                    return customer;
                }
                return null;
            }
        };
        Customer customer = runner.query(connection, sql, handler, 10);
        System.out.println(customer);

    }
}
