package com.example.membermanagerment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;

@Entity
@Table(name = "thanhvien")
@Getter
@Setter
public class ThanhVien {
    @Id
    @Column(name = "MaTV")
    private BigInteger maTV;

    @Column(name = "ho_ten")
    private String hoTen;

    @Column(name = "Khoa")
    private String khoa;

    @Column(name = "Nganh")
    private String nganh;

    @Column(name = "SDT")
    private String sdt;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "thanhvien")
    private List<ThongTinSD> thongtinsd;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "thanhvien")
    private List<XuLy> xuly;

    public ThanhVien() {
    }

    public ThanhVien(BigInteger maSV, String hoTen, String khoa, String nganh, String sdt, String password, String email) {
        this.maTV = maSV;
        this.hoTen = hoTen;
        this.khoa = khoa;
        this.nganh = nganh;
        this.sdt = sdt;
        this.password = password;
        this.email = email;
    }

    @Override
    public String toString() {
        return "ThanhVien{" +
                "maTV=" + maTV +
                ", hoTen='" + hoTen + '\'' +
                ", khoa='" + khoa + '\'' +
                ", nganh='" + nganh + '\'' +
                ", sdt='" + sdt + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}