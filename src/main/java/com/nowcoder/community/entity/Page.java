package com.nowcoder.community.entity;

public class Page {

    private int current = 1;//当前页码
    private int limit = 10;//显示上限
    private int rows;//数据总数(用于计算总页数)
    private String path;//查询路径(用于复用分页链接)

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if (current >= 1) {
            this.current = current;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit >= 1 && limit <= 100) {
            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if (rows >= 0) {
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取当前页的起始行
     */
    public int getOffset() {
        return (current - 1) * limit;
    }

    /**
     * 获取总页数
     */
    public int getTotal() {
        if (rows % limit == 0) {
            return rows / limit;
        } else {
            return rows / limit + 1;
        }
    }

    /**
     * 获取起始页码：页面上显示的几个页码的起始位置
     */
    public int getFrom() {
        int from = current - 2;
        return from < 1 ? 1 : from;
    }

    /**
     * 获取结束页码：页面上显示的几个页码的结束位置
     */
    public int getTo() {
        int to = current + 2;
        int total = getTotal();//总页数
        return to > total ? total : to;
    }
}
