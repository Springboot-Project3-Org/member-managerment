package com.example.membermanagerment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

import java.math.BigInteger;
import java.sql.Timestamp;

@Entity
@Table(name = "thongtinsd")
@Getter
@Setter
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
        return "thongtinsd{" +
                "MaTT=" + MaTT +
                ", thanhvien=" + thanhvien +
                ", thietbi=" + thietbi +
                ", TGVao=" + TGVao +
                ", TGMuon=" + TGMuon +
                ", TGTra=" + TGTra +
                ", TGDatCho=" + TGDatCho +
                '}';
    }

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "MaTB", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_thongtinsd_device"))
    private ThietBi device;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaTV", referencedColumnName = "MaTV", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_thanhvien_thongtinsd"))
    private ThanhVien member;
}