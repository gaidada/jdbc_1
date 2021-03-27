package com.exer;

import com.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

/*使用PreparedStatement实现批量数据的操作*
* CREATE TABLE goods(
id INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(25)
);
* 向goods表中插入1w条数据
 */
public class InsertTest {
    //批量添加方式1
    @Test
    public void test1() throws Exception {
        Connection connection = JDBCUtils.getConnection();
        String sql = "insert into goods(name) values(?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        for (int i = 1; i <= 10000; i++) {
            ps.setObject(1, "name_" + i);
            //“攒”sql
            ps.addBatch();
            if (i % 500 == 0) {
                //执行batch
                ps.executeBatch();
                //清空batch
                ps.clearBatch();
            }

        }
        JDBCUtils.closetResource(connection, ps);
    }

    //批量插入方式2，优化1
    @Test
    public void test2() throws Exception {
        Connection connection = JDBCUtils.getConnection();
        connection.setAutoCommit(false);
        String sql = "insert into goods(name) values(?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        for (int i = 1; i <= 10000; i++) {
            ps.setObject(1, "name_" + i);
            //“攒”sql
            ps.addBatch();
            if (i % 500 == 0) {
                //执行batch
                ps.executeBatch();
                //清空batch
                ps.clearBatch();
            }
        }
        connection.commit();
        JDBCUtils.closetResource(connection, ps);
    }
}
