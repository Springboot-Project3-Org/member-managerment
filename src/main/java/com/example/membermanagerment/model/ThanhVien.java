package com.example.membermanagerment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;

@Entity
@Table(name = "thanhvien")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

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
    @JsonIgnore
    private List<ThongTinSD> thongtinsd;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "thanhvien")
    @JsonIgnore
    private List<XuLy> xuly;

    public BigInteger getMaTV() {
        return maTV;
    }

    public void setMaTV(BigInteger maTV) {
        this.maTV = maTV;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getKhoa() {
        return khoa;
    }

    public void setKhoa(String khoa) {
        this.khoa = khoa;
    }

    public String getNganh() {
        return nganh;
    }

    public void setNganh(String nganh) {
        this.nganh = nganh;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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