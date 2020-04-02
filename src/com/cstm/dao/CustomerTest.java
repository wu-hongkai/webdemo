package com.cstm.dao;

import cn.itcast.utils.CommonUtils;
import com.cstm.domain.Customer;
import org.junit.Test;

public class CustomerTest {
    @Test
    public void addToMysql(){
        CustomerDao customerDao = new CustomerDao();
        for (int i = 0; i < 300; i++) {
            Customer customer = new Customer();
            customer.setCid(CommonUtils.uuid());
            customer.setCname("cstm"+i);
            customer.setBirthday("2020-01-03");
            customer.setGender(i%2==0?"男":"女");//这样用第一次见
            customer.setCellphone("139"+i);
            customer.setEmail("cstm"+i+"@163.com");
            customer.setDescription("我是第"+i+"个客户");
            customerDao.add(customer);
        }
    }
}
