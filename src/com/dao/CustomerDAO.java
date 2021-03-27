package com.dao;

import com.bean.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

//此接口用于规范针对于customers表的常用操作
public interface CustomerDAO {
    //将cust对象添加到数据库中
    void insert(Connection connection, Customer cust);

    //根据指定的id，删除表中的一条记录
    void deleteById(Connection connection, int id);

    //针对内存中的cust对象修改数据表中的记录
    void updateById(Connection connection, Customer customer);

    //指定id查询得到对应的Customer对象
    Customer getCustomerById(Connection connection, int id);

    //查询表中所有记录构成的集合
    List<Customer> getAll(Connection connection);

    //返回数据表中的数据的条目数
    Long getCount(Connection connection);

    //返回数据表中最大的生日
    Date getMaxBirth(Connection connection);
}
