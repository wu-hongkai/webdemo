package com.cstm.dao;

import cn.itcast.jdbc.TxQueryRunner;
import com.cstm.domain.Customer;
import com.cstm.domain.PageBean;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
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
//    /*
//    * 查询客户 version1 : 未分页
//    * */
//    public List<Customer> findAll(){
//        String sql = "select * from customer";
//        try {
//            //多行多列，使用BeanList
//            return qr.query(sql, new BeanListHandler<Customer>(Customer.class));
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    /*
    * 查询客户 version2 : 分页
    * @return 分页对象PageBean
    * */
    public PageBean<Customer> findAll(int pageCode, int pageSize){
        //1. 创建pageBean对象
        PageBean<Customer> pageBean = new PageBean<>();
        //2. 设置pagebean的pageCode和pageSize
        pageBean.setPageCode(pageCode);
        pageBean.setPageSize(pageSize);

        try {
            //3. 得到totalRecord
            //qr使用ScalarHandler接口，返回类型为Object，先转为Number
            String sql = "select count(*) from customer";
            Number number = (Number)qr.query(sql, new ScalarHandler());
            int totalRecord = number.intValue();
            pageBean.setTotalRecord(totalRecord);
            //根据totalRecord和pageSize计算totalPage
            pageBean.setTotalPage(totalRecord, pageSize);
            //4. 分页查询，得到beanList
            sql = "select * from customer order by cname limit ?,?";
            List<Customer> beanList = qr.query(sql,
                    new BeanListHandler<>(Customer.class),
                    (pageCode-1)*pageSize,
                    pageSize);
           pageBean.setBeanList(beanList);
           return pageBean;
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
            return qr.query(sql, new BeanListHandler<>(Customer.class), cid).get(0);
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
//    /*
//    * 多条件组合查询 version1 未分页
//    * */
//    public List<Customer> query(Customer criteria) {
//        /*
//        * 1. 给出sql模板
//        *   分析可能出现的条件情况：
//        *       四个条件都没给；只给一个；。。。
//        *       所以难点在于如何给出适配的sql语句模板：使用StringBuilder
//        * 2. 给出参数
//        * 3. 调用query方法，使用结果集处理器：BeanListHandler
//        * */
//
//        //1. 给出sql模板
//        //把查询语句作为前缀
//        //技巧：在sql前缀里增加 where 1=1,这样使后续的条件sql句式统一，也不必再加if了。
//        StringBuilder sql = new StringBuilder("select * from customer where 1=1");
//
//        //params用于动态添加参数
//        List<Object> params = new ArrayList<>();
//        String cname =criteria.getCname();
//        if(cname != null && !cname.trim().isEmpty()){
//            sql.append(" and cname =?");
//            params.add(cname);
//            //改进：模糊查询like params.add("%"+cname+"%")
//        }
//        String gender =criteria.getGender();
//        if(gender != null && !gender.trim().isEmpty()){
//            sql.append(" and gender =?");
//            params.add(gender);
//        }
//        String cellphone =criteria.getCellphone();
//        if(cellphone != null && !cellphone.trim().isEmpty()){
//            sql.append(" and cellphone =?");
//            params.add(cellphone);
//        }
//        String email =criteria.getEmail();
//        if(email != null && !email.trim().isEmpty()){
//            sql.append(" and email =?");
//            params.add(email);
//        }
//
//        //3. 执行query
//        try {
//            //调用query时需要把参数类型设置正确
//            return qr.query(sql.toString(),new BeanListHandler<Customer>(Customer.class),params.toArray());
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
    /*
     * 多条件组合查询 version2 分页
     * */
    public PageBean<Customer> query(Customer criteria, int pageCode, int pageSize) {

        //1. 创建pageBean对象
        PageBean<Customer> pageBean = new PageBean<>();
        //2. 设置pagebean的pageCode和pageSize
        pageBean.setPageCode(pageCode);
        pageBean.setPageSize(pageSize);

        try {
            //3. 得到totalRecord

            //技巧：将sql前缀分成两部分，以便whereSql的重用
            StringBuilder cntSql = new StringBuilder("select count(*) from customer");
            StringBuilder whereSql = new StringBuilder(" where 1=1");
            /*构造sql模板和 参数 */
            List<Object> params = new ArrayList<>();//params用于动态添加参数
            String cname =criteria.getCname();
            if(cname != null && !cname.trim().isEmpty()){
                whereSql.append(" and cname =?");
                params.add(cname);
                //改进：模糊查询like params.add("%"+cname+"%")
            }
            String gender =criteria.getGender();
            if(gender != null && !gender.trim().isEmpty()){
                whereSql.append(" and gender =?");
                params.add(gender);
            }
            String cellphone =criteria.getCellphone();
            if(cellphone != null && !cellphone.trim().isEmpty()){
                whereSql.append(" and cellphone =?");
                params.add(cellphone);
            }
            String email =criteria.getEmail();
            if(email != null && !email.trim().isEmpty()){
                whereSql.append(" and email =?");
                params.add(email);
            }
            //qr使用ScalarHandler接口，返回类型为Object，先转为Number再转int
            //记得把sql接上whereSql，凑成完整的sql语句
            Number number = (Number)qr.query(cntSql.append(whereSql).toString(), new ScalarHandler(),params.toArray());
            int totalRecord = number.intValue();
            pageBean.setTotalRecord(totalRecord);
            pageBean.setTotalPage(totalRecord, pageSize);//根据totalRecord和pageSize计算totalPage
            //4. 分页查询，得到beanList
            //编程技巧：把sql模板的前缀再拆成两部分，以实现where部分的重用，而不必再重做一遍sql模板的构造
            StringBuilder sql = new StringBuilder("select * from customer");
            //给出limit子句
            StringBuilder limitSql = new StringBuilder(" order by cname limit ?,?");
            //params中要添加limit后面两个问号对应的值
            params.add((pageCode-1)*pageSize);
            params.add(pageSize);

            //提交sql语句
            List<Customer> beanList = qr.query(sql.append(whereSql).append(limitSql).toString(),
                    new BeanListHandler<>(Customer.class),
                    params.toArray());
            //将组合成的url封装到pageBean中

            System.out.println(beanList.size()+ whereSql.toString()+ " "+ criteria.getGender());
            pageBean.setBeanList(beanList);
            return pageBean;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
