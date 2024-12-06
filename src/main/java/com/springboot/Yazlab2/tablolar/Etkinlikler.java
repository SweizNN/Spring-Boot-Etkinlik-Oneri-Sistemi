package com.springboot.Yazlab2.tablolar;

import jakarta.persistence.*;

import java.sql.Date;
import java.sql.Time;

@Entity
@Table(name = "etkinlikler")
public class Etkinlikler {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
    @Column(name = "etkinlik_adı",unique = true,nullable = false)
    private String etkinlikAdi;
    @Column(name = "aciklama",unique = true,nullable = false)
    private String aciklama;
    @Column(name = "tarih",unique = true,nullable = false)
    @Temporal(TemporalType.DATE)
    private Date tarih;
    @Column(name = "saat",unique = true,nullable = false)
    @Temporal(TemporalType.TIME)
    private Time saat;
    @Column(name = "etkinlik_suresi",unique = true,nullable = false)
    @Temporal(TemporalType.TIME)
    private Time etkinlik_suresi;
    @Column(name = "konum",unique = true,nullable = false)
    private String konum;
    @Column(name = "kategori",unique = true,nullable = false)
    private String kategori;
    @Column(name = "kullanıcı_id",unique = true,nullable = false)
    private Long kullanıcıId;
    @Column(name = "onay_durumu",unique = true,nullable = false)
    private boolean onayDurumu;



    public Etkinlikler(Long id, String etkinlikAdi, String aciklama, Date tarih, Time saat, Time etkinlik_suresi, String konum, String kategori,Long kullanıcıId) {
        this.id = id;
        this.etkinlikAdi = etkinlikAdi;
        this.aciklama = aciklama;
        this.tarih = tarih;
        this.saat = saat;
        this.etkinlik_suresi = etkinlik_suresi;
        this.konum = konum;
        this.kategori = kategori;
        this.kullanıcıId = kullanıcıId;
    }

    public Etkinlikler() {

    }




    public boolean isOnayDurumu() {
        return onayDurumu;
    }

    public void setOnayDurumu(boolean onayDurumu) {
        this.onayDurumu = onayDurumu;
    }

    public Long getKullanıcıId() {
        return kullanıcıId;
    }

    public void setKullanıcıId(Long kullanici_id) {
        this.kullanıcıId = kullanici_id;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEtkinlik_adı() {
        return etkinlikAdi;
    }

    public void setEtkinlik_adı(String etkinlik_adı) {
        this.etkinlikAdi = etkinlik_adı;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public Date getTarih() {
        return tarih;
    }

    public void setTarih(Date tarih) {
        this.tarih = tarih;
    }

    public Time getSaat() {
        return saat;
    }

    public void setSaat(Time saat) {
        this.saat = saat;
    }

    public Time getEtkinlik_suresi() {
        return etkinlik_suresi;
    }

    public void setEtkinlik_suresi(Time etkinlik_suresi) {
        this.etkinlik_suresi = etkinlik_suresi;
    }

    public String getKonum() {
        return konum;
    }

    public void setKonum(String konum) {
        this.konum = konum;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }
}
