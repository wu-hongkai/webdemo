package com.cstm.web.servlet;

import cn.itcast.servlet.BaseServlet;
import cn.itcast.utils.CommonUtils;
import com.cstm.domain.Customer;
import com.cstm.domain.PageBean;
import com.cstm.service.CustomerService;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/*
* web层
* */
@WebServlet(name = "CustomerServlet")
public class CustomerServlet extends BaseServlet {
    //依赖service
    private CustomerService customerService = new CustomerService();

    //新增的方法必须与servlet中的生命周期方法service参数一致
    //返回值为字符串，BaseServlet可以根据字符串做转发或重定向操作
    /*
    * 添加客户
    * */
    public String add(HttpServletRequest request, HttpServletResponse response) {
        /*
        * 1.封装表单到Customer对象
        * 2.补全cid，使用UUID
        * 3.使用service方法完成添加工作
        * 4.向request域中保存成功信息
        * 5.转发到msg.jsp
        * */
        Customer c = CommonUtils.toBean(request.getParameterMap(), Customer.class);
        c.setCid(CommonUtils.uuid());
        customerService.add(c);
        request.setAttribute("msg","添加成功");
        //BaseServlet根据返回的字符串做转发或重定向
        return "f:/msg.jsp";
    }
    /*
    *查询所有客户 version1 ：未分页
    */
//    public String findAll(HttpServletRequest request, HttpServletResponse response){
//        /*
//        * 1.调用service方法，得到查询结果
//        * 2.查询结果保存到request域中
//        * 3.转发到list.jsp
//        * */
//        request.setAttribute("cstmList",customerService.findAll());
//        return "f:/list.jsp";
//    }
    /*
    * 查询所有客户 version2 ：分页
    * */
    public String findAll(HttpServletRequest request, HttpServletResponse response){
        /*
         * 1.获取页面传递的pagecode
         * 2.给定pagesize的值
         * 3.使用pagecode和pagesize调用service方法，得到pagebean，保存到request域
         * 4.转发到list.jsp
         * */
        int pagecode = getPageCode(request);//得到pagecode
        int pageSize = 10;//给定pagesize值
        PageBean<Customer> pageBean = customerService.findAll(pagecode, pageSize);//传递pc,ps，获得pagebean
        request.setAttribute("pageBean",pageBean);//把pagebean保存到request域
        return "f:/list.jsp";//转发到list.jsp
    }
    /*
    * 获取pagecode
    *   如果pagecode不存在，说明pagecode=1
    *   如果pagecode存在，需要转成int类型
    * */
    private int getPageCode(HttpServletRequest request){
        String value = request.getParameter("pageCode");
        if(value == null || value.trim().isEmpty()){
            return 1;
        }
        return Integer.parseInt(value);
    }



    /*
    * 编辑之前的准备工作
    * */
    public String preEdit(HttpServletRequest request, HttpServletResponse response){
        /*
        * 1. 获取cid
        * 2. 使用cid 来调用service方法，得到Customer对象
        * 3. 将Customer对象存入request域中
        * 4. 转发给edit.jsp
        * */
        String cid = request.getParameter("cid");
        Customer cstm = customerService.load(cid);
        request.setAttribute("cstm", cstm);
        return "f:/edit.jsp";
    }
    /*
    * 编辑客户
    * */
    public String edit(HttpServletRequest request, HttpServletResponse response){
        /*
        * 1. 封装表单到Customer 对象中
        * 2. 调用service方法完成修改
        * 3. 保持成功信息到request域
        * 4. 转发到msg.jsp显示成功信息
        * */
        Customer customer = CommonUtils.toBean(request.getParameterMap(), Customer.class);
        customerService.edit(customer);
        request.setAttribute("msg", "编辑成功");
        return "f:/msg.jsp";
    }
    /*
    * 删除客户
    * */
    public String delete(HttpServletRequest request, HttpServletResponse response){
        /*
        * 1. 获取CID
        * 2. 传递CID到service中的删除方法delete
        * 3. 保存成功信息到request域中
        * 4. 转发到msg.jsp
        * */
        String cid = request.getParameter("cid");
        customerService.delete(cid);
        request.setAttribute("msg","删除成功");
        return "f:/msg.jsp";
    }
//    /*
//    * 高级查询 version1 未分页
//    * */
//    public String query(HttpServletRequest request, HttpServletResponse response){
//        /*
//        * 1. 封装表单到customer对象，只需要四个属性，这些属性组合起来就是查询条件
//        * 2. 调用service方法，得到List<Customer>
//        * 3. 保存查询结果到request域
//        * 4. 转发到list.jsp
//        * */
//        Customer criteria = CommonUtils.toBean(request.getParameterMap(),Customer.class);
//        List<Customer> cstmList = customerService.query(criteria);
//        request.setAttribute("cstmList",cstmList);
//        return "f:/list.jsp";
//    }
/*
 * 高级查询 version2 分页
 * 类比findAll的version2进行修改
 * */
    public String query(HttpServletRequest request, HttpServletResponse response){
        /*
         * 0. 把条件封装到Customer对象中
         * 1. 得到pageCode
         * 2. 给定pageSize
         * 3. 使用pc,ps调用service方法得到pageBean
         * 4. 把pageBean保存到request域
         * 5。 转发到list.jsp
         * */
        //获取条件
        Customer criteria = CommonUtils.toBean(request.getParameterMap(),Customer.class);
        int pagecode = getPageCode(request);//得到pagecode
        int pageSize = 10;//给定pagesize值
        PageBean<Customer> pageBean = customerService.query(criteria, pagecode, pageSize);
        request.setAttribute("pageBean",pageBean);//把pagebean保存到request域
        return "f:/list.jsp";//转发到list.jsp
    }
}
