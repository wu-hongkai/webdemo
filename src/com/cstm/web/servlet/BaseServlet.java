package com.cstm.web.servlet;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * BaseServlet用来作为其它Servlet的父类
 *
 * @author qdmmy6
 *
 *         一个类多个请求处理方法，每个请求处理方法的原型与service相同！ 原型 = 返回值类型 + 方法名称 + 参数列表
 */
public class BaseServlet extends HttpServlet {
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        /*
         * 对编码的处理
         */
        if(request.getMethod().equalsIgnoreCase("post")) {
            request.setCharacterEncoding("UTF-8");
        } else if(request.getMethod().equalsIgnoreCase("get")){
            request = new GetRequest(request);
        }
        response.setContentType("text/html;charset=utf-8");

        /**
         * 1. 获取method参数，它是用户想调用的方法 2. 把方法名称变成Method类的实例对象 3. 通过invoke()来调用这个方法
         */
        String methodName = request.getParameter("method");
        Method method = null;
        /**
         * 2. 通过方法名称获取Method对象
         */
        try {
            method = this.getClass().getMethod(methodName,
                    HttpServletRequest.class, HttpServletResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("您要调用的方法：" + methodName + "不存在！", e);
        }

        /**
         * 3. 通过method对象来调用它
         */
        try {
            String result = (String)method.invoke(this, request, response);
            if(result != null && !result.trim().isEmpty()) {
                String[] strs = result.split(":");
                if(strs[0].equals("f")) {
                    request.getRequestDispatcher(strs[1]).forward(request, response);
                } else if(strs[0].equals("r")) {
                    response.sendRedirect(request.getContextPath() + strs[1]);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

