package com.exer;

import com.bean.Customer;
import com.util.JDBCUtils;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.*;

//使用PrepredStatement测试Blob类型的数据
public class BlobTest {
    //向数据表customers插入Blob类型的字段
    @Test
    public void test1() throws Exception {
        Connection connection = JDBCUtils.getConnection();
        String sql = "insert into customers(name,email,birth,photo) values(?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setObject(1, "zhang");
        ps.setObject(2, "erew@qq.com");
        ps.setObject(3, "2000-01-01");
        FileInputStream is = new FileInputStream("tp.png");
        ps.setBlob(4, is);
        ps.execute();
        JDBCUtils.closetResource(connection, ps);
    }

    @Test
    public void test2() throws Exception {
        Connection connection = JDBCUtils.getConnection();
        String sql = "select id,name,email,birth,photo from customers where id =?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, 22);
        ResultSet rs = ps.executeQuery();
        InputStream is = null;
        FileOutputStream fos = null;
        if (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String email = rs.getString("email");
            Date birth = rs.getDate("birth");
            Customer customer = new Customer(id, name, email, birth);
            System.out.println(customer);
            //将blob类型的字段下载下来，保存在本地
            Blob photo = rs.getBlob("photo");
            is = photo.getBinaryStream();
            fos = new FileOutputStream("zhang.png");
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
        }
        is.close();
        fos.close();
        JDBCUtils.closetResource(connection, ps, rs);
    }
}
