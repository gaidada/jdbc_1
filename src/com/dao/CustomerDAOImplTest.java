package com.dao;

import com.bean.Customer;
import com.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

public class CustomerDAOImplTest {
    CustomerDAOImpl dao = new CustomerDAOImpl();

    @Test
    public void testInsert() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            Customer customer = new Customer(1, "zhangsan", "zhangs@zs.com", new Date(46474874465L));
            dao.insert(connection, customer);
            System.out.println("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closetResource(connection, null);
        }
    }

    @Test
    public void testDeleteById() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            dao.deleteById(connection, 23);
            System.out.println("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closetResource(connection, null);
        }
    }

    @Test
    public void testUpdateCustomer() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            Customer customer = new Customer(22, "zhangsan", "zhangs@zs.com", new Date(46474874465L));
            dao.updateById(connection, customer);
            System.out.println("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closetResource(connection, null);
        }
    }

    @Test
    public void testGetAll() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            List<Customer> list = dao.getAll(connection);
            list.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closetResource(connection, null);
        }
    }

    @Test
    public void testGetCount() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            System.out.println(dao.getCount(connection));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closetResource(connection, null);
        }
    }

    @Test
    public void testMaxBirth() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            System.out.println(dao.getMaxBirth(connection));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closetResource(connection, null);
        }
    }
}
