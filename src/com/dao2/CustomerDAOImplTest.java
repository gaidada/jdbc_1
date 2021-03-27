package com.dao2;

import com.bean.Customer;
import com.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.util.List;

public class CustomerDAOImplTest {
    CustomerDAOImpl dao = new CustomerDAOImpl();


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

}
