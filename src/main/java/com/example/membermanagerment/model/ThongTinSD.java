package com.example.membermanagerment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

import java.math.BigInteger;
import java.sql.Timestamp;

@Entity
@Table(name = "thongtinsd")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class ThongTinSD {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaTT")
    private int MaTT;

    @Column(name = "MaTV")
    private BigInteger thanhvien;

    @Column(name = "MaTB")
    @JsonIgnore
    private Integer thietbi;

    @Column(name = "TGVao")
    private Timestamp TGVao;

    @Column(name = "TGMuon")
    private Timestamp TGMuon;

    @Column(name = "TGTra")
    private Timestamp TGTra;

    @Column(name = "tgdat_cho")
    private Timestamp TGDatCho;

    public int getMaTT() {
        return MaTT;
    }

    public void setMaTT(int maTT) {
        MaTT = maTT;
    }

    public BigInteger getThanhvien() {
        return thanhvien;
    }

    public void setThanhvien(BigInteger thanhvien) {
        this.thanhvien = thanhvien;
    }

    public Integer getThietbi() {
        return thietbi;
    }

    public void setThietbi(Integer thietbi) {
        this.thietbi = thietbi;
    }

    public Timestamp getTGVao() {
        return TGVao;
    }

    public void setTGVao(Timestamp TGVao) {
        this.TGVao = TGVao;
    }

    public Timestamp getTGMuon() {
        return TGMuon;
    }

    public void setTGMuon(Timestamp TGMuon) {
        this.TGMuon = TGMuon;
    }

    public Timestamp getTGTra() {
        return TGTra;
    }

    public void setTGTra(Timestamp TGTra) {
        this.TGTra = TGTra;
    }

    public Timestamp getTGDatCho() {
        return TGDatCho;
    }

    public void setTGDatCho(Timestamp TGDatCho) {
        this.TGDatCho = TGDatCho;
    }

    public ThanhVien getMember() {
        return member;
    }

    public void setMember(ThanhVien member) {
        this.member = member;
    }

    public ThongTinSD() {
    }

    public ThongTinSD(BigInteger thanhvien, Integer thietbi, Timestamp TGVao, Timestamp TGMuon, Timestamp TGTra, Timestamp TGDatCho) {
        this.thanhvien = thanhvien;
        this.thietbi = thietbi;
        this.TGVao = TGVao;
        this.TGMuon = TGMuon;
        this.TGTra = TGTra;
        this.TGDatCho = TGDatCho;
    }

    @Override
    public String toString() {
        return "ThongTinSD{" +
                "MaTT=" + MaTT +
                ", thanhvien=" + thanhvien +
                ", thietbi=" + thietbi +
                ", TGVao=" + TGVao +
                ", TGMuon=" + TGMuon +
                ", TGTra=" + TGTra +
                ", TGDatCho=" + TGDatCho +
                ", member=" + member +
                '}';
    }

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "MaTB", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_thongtinsd_device"))
    @JsonIgnore
    private ThietBi device;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaTV", referencedColumnName = "MaTV", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_thanhvien_thongtinsd"))
    private ThanhVien member;
}