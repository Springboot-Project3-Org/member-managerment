package com.example.membermanagerment.model;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;

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
    @JsonIgnore
    private BigInteger MaTV;

    @Column(name = "hinh_thucxl")
    private String HinhThucXL;

    @Column(name = "so_tien")
    private Integer SoTien;

    @Column(name = "NgayXL")
    private Timestamp NgayXL;

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

    public Timestamp getNgayXL() {
        return NgayXL;
    }

    public void setNgayXL(Timestamp ngayXL) {
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

    public XuLy(BigInteger thanhvien, String HinhThucXL, Integer SoTien, Timestamp NgayXL, int TrangThaiXL) {
        this.MaTV = thanhvien;
        this.HinhThucXL = HinhThucXL;
        this.SoTien = SoTien;
        this.NgayXL = NgayXL;
        this.TrangThaiXL = TrangThaiXL;
    }

    public XuLy(BigInteger maTV, String hinhThucXL, Timestamp ngayXL, int trangThaiXL) {
        this.MaTV = maTV;
        this.HinhThucXL = hinhThucXL;
        this.NgayXL = ngayXL;
        this.TrangThaiXL = trangThaiXL;
    }

    public XuLy(int maXL, BigInteger maTV, String hinhThucXL, Integer soTien, Timestamp ngayXL, int trangThaiXL) {
        MaXL = maXL;
        MaTV = maTV;
        HinhThucXL = hinhThucXL;
        SoTien = soTien;
        NgayXL = ngayXL;
        TrangThaiXL = trangThaiXL;
    }

    @Override
    public String toString() {
        return "XuLy{" +
                "MaXL=" + MaXL +
                ", MaTV=" + MaTV +
                ", HinhThucXL='" + HinhThucXL + '\'' +
                ", SoTien=" + SoTien +
                ", NgayXL=" + NgayXL +
                ", TrangThaiXL=" + TrangThaiXL +
                '}';
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaTV", referencedColumnName = "MaTV", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_thanhvien_handle"))
    @JsonIgnore
    private ThanhVien thanhvien;
}
