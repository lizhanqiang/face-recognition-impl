package com.owesome.facerecognition.utils;

public class PagingResult {
    private Integer pageSize;
    private Integer pageIndex;
    private Integer totalRecord;
    private Object pageRecords;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(Integer totalRecord) {
        this.totalRecord = totalRecord;
    }

    public Object getPageRecords() {
        return pageRecords;
    }

    public void setPageRecords(Object pageRecords) {
        this.pageRecords = pageRecords;
    }
}
