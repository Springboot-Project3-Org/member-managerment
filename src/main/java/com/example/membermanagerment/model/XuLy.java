package com.example.membermanagerment.model;

import java.math.BigInteger;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Entity
@Table(name = "xuly")
@Getter
@Setter
public class XuLy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaXL")
    private int MaXL;

    @Column(name = "MaTV")
    private BigInteger MaTV;

    @Column(name = "hinh_thucxl")
    private String HinhThucXL;

    @Column(name = "so_tien")
    private Integer SoTien;

    @Column(name = "NgayXL")
    private Date NgayXL;

    @Column(name = "trang_thaixl")
    private int TrangThaiXL;

    public int getMaXL() {
        return MaXL;
    }

    public void setMaXL(int maXL) {
        MaXL = maXL;
    }

    public BigInteger getMaTV() {
        return MaTV;
    }

    public void setMaTV(BigInteger maTV) {
        MaTV = maTV;
    }

    public String getHinhThucXL() {
        return HinhThucXL;
    }

    public void setHinhThucXL(String hinhThucXL) {
        HinhThucXL = hinhThucXL;
    }

    public Integer getSoTien() {
        return SoTien;
    }

    public void setSoTien(Integer soTien) {
        SoTien = soTien;
    }

    public Date getNgayXL() {
        return NgayXL;
    }

    public void setNgayXL(Date ngayXL) {
        NgayXL = ngayXL;
    }

    public int getTrangThaiXL() {
        return TrangThaiXL;
    }

    public void setTrangThaiXL(int trangThaiXL) {
        TrangThaiXL = trangThaiXL;
    }

    public XuLy() {
    }

    public XuLy(BigInteger thanhvien, String HinhThucXL, Integer SoTien, Date NgayXL, int TrangThaiXL) {
        this.MaTV = thanhvien;
        this.HinhThucXL = HinhThucXL;
        this.SoTien = SoTien;
        this.NgayXL = NgayXL;
        this.TrangThaiXL = TrangThaiXL;
    }

    public XuLy(BigInteger maTV, String hinhThucXL, Date ngayXL, int trangThaiXL) {
        this.MaTV = maTV;
        this.HinhThucXL = hinhThucXL;
        this.NgayXL = ngayXL;
        this.TrangThaiXL = trangThaiXL;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaTV", referencedColumnName = "MaTV", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_thanhvien_handle"))
    private ThanhVien thanhvien;
}
