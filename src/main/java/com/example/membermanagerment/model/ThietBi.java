package com.example.membermanagerment.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "thietbi")
@Getter
@Setter
public class ThietBi {
    @Id
    @Column(name = "MaTB")
    private int MaTB;

    @Column(name = "TenTB")
    private String TenTB;

    @Column(name = "MoTaTB")
    private String MoTaTB;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "thietbi")
    private List<ThongTinSD> thongtinsd;

    public ThietBi() {
    }

    public ThietBi(int maTB, String TenTB, String MoTaTB) {
        this.MaTB = maTB;
        this.TenTB = TenTB;
        this.MoTaTB = MoTaTB;
    }

    public ThietBi(String TenTB, String MoTaTB) {
        this.TenTB = TenTB;
        this.MoTaTB = MoTaTB;
    }

    @Override
    public String toString() {
        return "thietbi{" +
                "MaTB=" + MaTB +
                ", TenTB='" + TenTB + '\'' +
                ", MoTaTB='" + MoTaTB + '\'' +
                '}';
    }
}