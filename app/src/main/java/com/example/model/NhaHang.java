package com.example.model;

public class NhaHang {
    private Integer id;
    private String tenmonan;
    private Integer giamonan;
    private String diadiem;

    public NhaHang(Integer id, String tenmonan, Integer giamonan, String diadiem) {
        this.id = id;
        this.tenmonan = tenmonan;
        this.giamonan = giamonan;
        this.diadiem = diadiem;
    }

    public Integer getId() {
        return id;
    }

    public String getTenmonan() {
        return tenmonan;
    }

    public Integer getGiamonan() {
        return giamonan;
    }

    public String getDiadiem() {
        return diadiem;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTenmonan(String tenmonan) {
        this.tenmonan = tenmonan;
    }

    public void setGiamonan(Integer giamonan) {
        this.giamonan = giamonan;
    }

    public void setDiadiem(String diadiem) {
        this.diadiem = diadiem;
    }
}
