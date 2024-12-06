package com.springboot.Yazlab2.tablolar;

import jakarta.persistence.*;
import org.springframework.data.annotation.Id;
import java.sql.Date;

@Entity
@Table(name="kullanıcılar")
public class Kullanıcılar {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
    @Column(name="kullanıcı_adı", unique = true,nullable = false)
    private String kullanıcıAdı;
    @Column(name = "sifre",unique = true,nullable = false)
    private String sifre;
    @Column(name = "eposta",unique = true,nullable = false)
    private String eposta;
    @Column(name = "konum",unique = true,nullable = false)
    private String konum;
    @Column(name = "ilgi_alanları",unique = true,nullable = false)
    private String ilgi_alanları;
    @Column(name = "ad",unique = true,nullable = false)
    private String ad;
    @Column(name = "soyad",unique = true,nullable = false)
    private String soyad;
    @Column(name = "dogum_tarihi",unique = true,nullable = false)
    @Temporal(TemporalType.DATE)
    public static Date dogum_tarihi;
    @Column(name = "cinsiyet",unique = true,nullable = false)
    @Enumerated(EnumType.STRING)
    private Cinsiyet cinsiyet;
    @Column(name = "telefon_numarası",unique = true,nullable = false)
    private String telefon_numarası;
    @Column(name = "profil_fotografı",unique = true,nullable = false)
    private String profil_fotografı;


    public Kullanıcılar(String kullanıcıAd, String encodedPassword, String ad, String soyad, String ePosta, String telefonNo, Date dogumTarih, String ilgiAlan, String konum, Enum cinsiyet, String profilurl) {
        this.kullanıcıAdı=kullanıcıAd;
        this.sifre=encodedPassword;
        this.eposta=ePosta;
        this.konum=konum;
        this.ad=ad;
        this.soyad=soyad;
        this.telefon_numarası=telefonNo;
        this.dogum_tarihi=dogumTarih;
        this.ilgi_alanları=ilgiAlan;
        this.cinsiyet= (Cinsiyet) cinsiyet;
        this.profil_fotografı=profilurl;


    }

    public Kullanıcılar() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKullanıcıAdı() {
        return kullanıcıAdı;
    }

    public void setKullanıcıAdı(String kullanıcıAdı) {
        this.kullanıcıAdı = kullanıcıAdı;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

    public String getEposta() {
        return eposta;
    }

    public void setEposta(String eposta) {
        this.eposta = eposta;
    }

    public String getKonum() {
        return konum;
    }

    public void setKonum(String konum) {
        this.konum = konum;
    }

    public String getIlgi_alanları() {
        return ilgi_alanları;
    }

    public void setIlgi_alanları(String ilgi_alanları) {
        this.ilgi_alanları = ilgi_alanları;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }

    public Date getDogum_tarihi() {
        return dogum_tarihi;
    }

    public void setDogum_tarihi(Date dogum_tarihi) {
        this.dogum_tarihi = dogum_tarihi;
    }

    public Cinsiyet getCinsiyet() {
        return cinsiyet;
    }

    public void setCinsiyet(Cinsiyet cinsiyet) {
        this.cinsiyet = cinsiyet;
    }

    public String getTelefon_numaras() {
        return telefon_numarası;
    }

    public void setTelefon_numaras(String telefon_numaras) {
        this.telefon_numarası = telefon_numaras;
    }

    public String getProfil_fotografı() {
        return profil_fotografı;
    }

    public void setProfil_fotografı(String profil_fotografı) {
        this.profil_fotografı = profil_fotografı;
    }

    public enum Cinsiyet {
        Erkek, Kadın, Diğer
    }


    @Override
    public String toString() {
        return "Kullanıcılar{" +
                "id=" + id +
                ", kullanıcıAdı='" + kullanıcıAdı + '\'' +
                ", sifre='" + sifre + '\'' +
                ", eposta='" + eposta + '\'' +
                ", konum='" + konum + '\'' +
                ", ilgi_alanları='" + ilgi_alanları + '\'' +
                ", ad='" + ad + '\'' +
                ", soyad='" + soyad + '\'' +
                ", dogum_tarihi=" + dogum_tarihi +
                ", cinsiyet=" + cinsiyet +
                ", telefon_numarası='" + telefon_numarası + '\'' +
                ", profil_fotografı='" + profil_fotografı + '\'' +
                '}';
    }
}