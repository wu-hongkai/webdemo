package com.cstm.dao;

import cn.itcast.jdbc.TxQueryRunner;
import com.cstm.domain.Customer;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
* DAO层
* */
public class CustomerDao {
    //使用的是自己封装的TxQueryRunner类
    private QueryRunner qr = new TxQueryRunner();
    /*
    * 添加客户
    * */
    public void add(Customer c){
        String sql = "INSERT INTO customer VALUES(?,?,?,?,?,?,?)";
        Object[] params = {c.getCid(),c.getCname(),c.getGender(),c.getBirthday(),c.getCellphone(),c.getEmail(),c.getDescription()};
        try {
            qr.update(sql, params);
        } catch (SQLException e) {
           throw new RuntimeException(e);
        }
    }
    /*
    * 查询客户
    * */
    public List<Customer> findAll(){
        String sql = "select * from customer";
        try {
            //多行多列，使用BeanList
            return qr.query(sql, new BeanListHandler<Customer>(Customer.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    /*
    * 加载客户
    * */
    public Customer load(String cid) {
        String sql = "select * from customer where cid=?";
        try {
            return qr.query(sql, new BeanListHandler<Customer>(Customer.class), cid).get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    /*
    * 编辑客户
    * */
    public void edit(Customer customer) {
        String sql = "update customer set cname=?,gender=?,birthday=?,"+
                "cellphone=?,email=?,description=? where cid=?";
        Object[] params = {customer.getCname(), customer.getGender(), customer.getBirthday(), customer.getCellphone(),
                customer.getEmail(), customer.getDescription(),customer.getCid()};
        try {
            qr.update(sql,params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /*
    * 删除客户
    * */
    public void delete(String cid) {
        String sql = "delete from customer where cid=?";
        try {
            qr.update(sql, cid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /*
    * 多条件组合查询
    * */
    public List<Customer> query(Customer criteria) {
        /*
        * 1. 给出sql模板
        *   分析可能出现的条件情况：
        *       四个条件都没给；只给一个；。。。
        *       所以难点在于如何给出适配的sql语句模板：使用StringBuilder
        * 2. 给出参数
        * 3. 调用query方法，使用结果集处理器：BeanListHandler
        * */

        //1. 给出sql模板
        //把查询语句作为前缀
        //技巧：在sql前缀里增加 where 1=1,这样使后续的条件sql句式统一，也不必再加if了。
        StringBuilder sql = new StringBuilder("select * from customer where 1=1");

        //params用于动态添加参数
        List<Object> params = new ArrayList<>();
        String cname =criteria.getCname();
        if(cname != null && !cname.trim().isEmpty()){
            sql.append(" and cname =?");
            params.add(cname);
            //改进：模糊查询like params.add("%"+cname+"%")
        }
        String gender =criteria.getGender();
        if(gender != null && !gender.trim().isEmpty()){
            sql.append(" and gender =?");
            params.add(gender);
        }
        String cellphone =criteria.getCellphone();
        if(cellphone != null && !cellphone.trim().isEmpty()){
            sql.append(" and cellphone =?");
            params.add(cellphone);
        }
        String email =criteria.getEmail();
        if(email != null && !email.trim().isEmpty()){
            sql.append(" and email =?");
            params.add(email);
        }

        //3. 执行query
        try {
            //调用query时需要把参数类型设置正确
            return qr.query(sql.toString(),new BeanListHandler<Customer>(Customer.class),params.toArray());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
