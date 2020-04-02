package com.cstm.web.servlet;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class GetRequest extends HttpServletRequestWrapper {
    public GetRequest(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if(value == null) return null;
        try {
            return new String(value.getBytes("utf-8"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Map getParameterMap() {
        Map<String,String[]> map = new HashMap<String,String[]>(super.getParameterMap());
        if(map == null) return null;
        for(String name : map.keySet()) {
            String[] values = map.get(name);
            for(int i = 0; i < values.length; i++) {
                try {
                    values[0] = new String(values[0].getBytes("utf-8"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
            map.put(name, values);
        }
        return map;
    }
}
