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

    @Column(name = "HinhThucXL")
    private String HinhThucXL;

    @Column(name = "SoTien")
    private Integer SoTien;

    @Column(name = "NgayXL")
    private Date NgayXL;

    @Column(name = "TrangThaiXL")
    private int TrangThaiXL;

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
