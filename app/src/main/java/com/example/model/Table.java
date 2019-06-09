package com.example.model;

public class Table {
    private Integer id;
    private Integer status;
    private String name;
    public static final int FREE = 0;
    public static final int BUSY = 1;

    public Table(Integer id, Integer status, String name) {
        this.id = id;
        this.status = status;
        this.name = name;
    }

    public Table() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
