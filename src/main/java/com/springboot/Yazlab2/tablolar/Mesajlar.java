package com.springboot.Yazlab2.tablolar;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "mesajlar")
public class Mesajlar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
    @Column(name = "gonderici_id",unique = true,nullable = false)
    private Long gondericiId;
    @Column(name = "alıcı_id",unique = true,nullable = false)
    private Long alıcıId;
    @Column(name = "mesaj_metni",unique = true,nullable = false)
    private String mesaj_metni;
    @Column(name = "gonderim_zamanı",unique = true,nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp gonderim_zamanı;
    @Column(name = "etkinlik_id",unique = true,nullable = false)
    private Long etkinlikId;





    public Long getEtkinlikId() {
        return etkinlikId;
    }

    public void setEtkinlikId(Long etkinlik_id) {
        this.etkinlikId = etkinlik_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGondericiId() {
        return gondericiId;
    }

    public void setGondericiId(Long gonderici_id) {
        this.gondericiId = gonderici_id;
    }

    public Long getAlıcıId() {
        return alıcıId;
    }

    public void setAlıcıId(Long alıcı_id) {
        this.alıcıId = alıcı_id;
    }

    public String getMesaj_metni() {
        return mesaj_metni;
    }

    public void setMesaj_metni(String mesaj_metni) {
        this.mesaj_metni = mesaj_metni;
    }

    public Timestamp getGonderim_zamanı() {
        return gonderim_zamanı;
    }

    public void setGonderim_zamanı(Timestamp gonderim_zamanı) {
        this.gonderim_zamanı = gonderim_zamanı;
    }
}
