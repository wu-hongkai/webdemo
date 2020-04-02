package com.cstm.domain;

import java.util.List;

public class PageBean<T> {
    private int pageCode;//当前页码page code
    private int totalPage;//总页数total page，需要单独计算
    private int totalRecord;//总记录数total record
    private int pageSize;//每页记录数page size
    private List<T> beanList;//当前页的数据

    public PageBean(){
        //init();
    }
    public PageBean(int pageCode, int totalRecord, int pageSize, List<T> beanList) {
        this.pageCode = pageCode;
        this.totalRecord = totalRecord;
        this.pageSize = pageSize;
        this.beanList = beanList;
    }


    public int getPageCode() {
        return pageCode;
    }

    public void setPageCode(int pageCode) {
        this.pageCode = pageCode;
    }

    public int getTotalPage() {
        return totalPage;
    }

    //单独计算totalpage，注意它的取值是向上取整
    public void setTotalPage(int totalRecord, int pageSize) {
        this.totalPage = totalRecord / pageSize;
        if(totalRecord % pageSize != 0){
            this.totalPage++;
        }
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getBeanList() {
        return beanList;
    }

    public void setBeanList(List<T> beanList) {
        this.beanList = beanList;
    }
}
