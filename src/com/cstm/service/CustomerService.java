package com.cstm.service;

import com.cstm.dao.CustomerDao;
import com.cstm.domain.Customer;
import com.cstm.domain.PageBean;

import java.util.List;

/*
* 业务层
* 依赖DAO
* */
public class CustomerService {
    private CustomerDao customerDao = new CustomerDao();


    public void add(Customer c){
        customerDao.add(c);
    }

//    public List<Customer> findAll(){
//        return customerDao.findAll();
//    }
    public PageBean<Customer> findAll(int pageCode, int pageSize){
        return customerDao.findAll(pageCode, pageSize);
    }

    public Customer load(String cid) {
        return customerDao.load(cid);
    }

    public void edit(Customer customer) {
        customerDao.edit(customer);
    }

    public void delete(String cid) {
        customerDao.delete(cid);
    }

//    /*
//     * 高级查询：多条件组合查询+分页
//     * */
//    public List<Customer> query(Customer criteria) {
//        return customerDao.query(criteria);
//    }

    /*
    * 高级查询：多条件组合查询+分页
    * */
    public PageBean<Customer> query(Customer criteria, int pageCode, int pageSize) {
        return customerDao.query(criteria, pageCode, pageSize);
    }
}
