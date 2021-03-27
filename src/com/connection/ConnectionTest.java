package com.connection;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionTest {
    //方式一
    @Test
    public void test1() throws SQLException {
        //获取Driver实现类对象
        Driver driver = new com.mysql.jdbc.Driver();
        String url = "jdbc:mysql://localhost:3306/test";

        //将用户名密码封装到Properties中
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "1234");
        Connection connect = driver.connect(url, info);
        System.out.println(connect);
    }

    //方式二：对方式一进行升级，不出现第三方API，使程序有更好的移植性
    @Test
    public void test2() throws Exception {
        //用反射获取Driver实现类对象
        Class<?> clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();

        //提供要连接的数据库
        String url = "jdbc:mysql://localhost:3306/test";
        //连接需要的用户名和密码
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "1234");
        Connection connect = driver.connect(url, info);

        System.out.println(connect);
    }

    //方式三：升级方式二，使用DriverManager替换Driver
    @Test
    public void test3() throws Exception {
        //获取Driver实现类对象
        Class<?> clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();
        // 注册驱动
        DriverManager.registerDriver(driver);
        //获取连接
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "1234";
        Connection connect = DriverManager.getConnection(url, user, password);

        System.out.println(connect);
    }

    //方式四：优化方式三
    @Test
    public void test4() throws Exception {
        //加载Driver，相较于方式三，可以省略注册
        //Class.forName("com.mysql.jdbc.Driver");
        //获取连接
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "1234";
        Connection connect = DriverManager.getConnection(url, user, password);

        System.out.println(connect);
    }

    //方式五：升级方式四，在配置文件中读取url,user,password
    @Test
    public void test5() throws Exception {
        //1.读取配置文件中的基本信息
        InputStream is = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
        Properties properties = new Properties();
        properties.load(is);
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        String url = properties.getProperty("url");
        String driverClass = properties.getProperty("driverClass");

        //2.加载驱动
        Class.forName(driverClass);

        //3.获取连接
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);
    }
}
