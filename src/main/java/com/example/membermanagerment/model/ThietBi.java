package com.example.membermanagerment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "thietbi")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ThietBi {
    @Id
    @Column(name = "MaTB")
    @JsonIgnore
    private int MaTB;

    @Column(name = "TenTB")
    private String tenTB;

    @Column(name = "mo_tatb")
    private String moTaTB;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "thietbi")
    @JsonIgnore
    private List<ThongTinSD> thongtinsd;


    public ThietBi() {
    }

    public ThietBi(int maTB, String tenTB, String moTaTB) {
        this.MaTB = maTB;
        this.tenTB = tenTB;
        this.moTaTB = moTaTB;
    }

    public ThietBi(String tenTB, String moTaTB) {
        this.tenTB = tenTB;
        this.moTaTB = moTaTB;
    }

    @Override
    public String toString() {
        return "ThietBi{" +
                "maTB=" + MaTB +
                ", tenTB='" + tenTB + '\'' +
                ", moTaTB='" + moTaTB + '\'' +
                '}';
    }

    // Getter và setter
    public int getMaTB() {
        return MaTB;
    }

    public void setMaTB(int maTB) {
        this.MaTB = maTB;
    }

    public String getTenTB() {
        return tenTB;
    }

    public void setTenTB(String tenTB) {
        this.tenTB = tenTB;
    }

    public String getMoTaTB() {
        return moTaTB;
    }

    public void setMoTaTB(String moTaTB) {
        this.moTaTB = moTaTB;
    }

    public List<ThongTinSD> getThongtinsd() {
        return thongtinsd;
    }

    public void setThongtinsd(List<ThongTinSD> thongtinsd) {
        this.thongtinsd = thongtinsd;
    }
}