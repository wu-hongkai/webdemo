package com.cstm.service;

import com.cstm.dao.CustomerDao;
import com.cstm.domain.Customer;

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

    public List<Customer> findAll(){
        return customerDao.findAll();
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

    public List<Customer> query(Customer criteria) {
        return customerDao.query(criteria);
    }
}
