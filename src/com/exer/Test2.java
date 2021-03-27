package com.exer;

import com.util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Test2 {
//    //向examstudent表中添加一条数据
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("四级/六级");
//        int type = scanner.nextInt();
//        System.out.println("身份证号：");
//        String IDCard = scanner.next();
//        System.out.println("准考证号：");
//        String examCard = scanner.next();
//        System.out.println("学生姓名：");
//        String studentName = scanner.next();
//        System.out.println("所在城市：");
//        String location = scanner.next();
//        System.out.println("成绩：");
//        int grade = scanner.nextInt();
//        String sql = "insert into examstudent (type,IDCard,examCard,studentName,location,grade) values (?,?,?,?,?,?)";
//        int insertCount = update(sql, type, IDCard, examCard, studentName, location, grade);
//        if (insertCount>0) {
//            System.out.println("添加成功");
//        }else {
//            System.out.println("添加失败");
//        }
//    }

//    public static void main(String[] args) {
//        System.out.println("请选择输入的类型");
//        System.out.println("a.准考证号");
//        System.out.println("b身份号");
//        Scanner scanner = new Scanner(System.in);
//        String select = scanner.next();
//        if ("a".equalsIgnoreCase(select)) {
//            System.out.println("请输入准考证号");
//            String examCard = scanner.next();
//            String sql = "select FlowID flowID,Type type,IDCard,ExamCard examCard,StudentName name,location,Grade grade from examstudent where examCard=?";
//            List<Student> students = getInstance(Student.class, sql, examCard);
//            students.forEach(System.out::println);
//        } else if ("b".equalsIgnoreCase(select)) {
//
//        } else {
//            System.out.println("输入有误");
//        }
//    }

    public static void main(String[] args) {
        System.out.println("请输入考号");
        Scanner scanner = new Scanner(System.in);
        String examCard = scanner.next();
        //查询
        //String sql = "select FlowID flowID,Type type,IDCard,ExamCard examCard,StudentName name,location,Grade grade from examstudent where examCard=?";
        //List<Student> students = getInstance(Student.class, sql, examCard);


        String sql1 = "delete from examstudent where examCard=?";
        int deleteCount = update(sql1, examCard);
        if (deleteCount > 0) {
            System.out.println("删除成功");
        }else {
            System.out.println("无此人");
        }

    }

    public static <T> List<T> getInstance(Class<T> clazz, String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
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
                    String columnName = rsmd.getColumnLabel(i + 1);
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
            JDBCUtils.closetResource(conn, ps, rs);
        }
        return null;
    }

    public static int update(String sql, Object... args) {//sql中占位符的个数与可变形参的长度相同
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
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5.关闭资源
            JDBCUtils.closetResource(conn, ps);
        }
        return 0;
    }
}
