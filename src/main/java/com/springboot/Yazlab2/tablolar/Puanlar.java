package com.springboot.Yazlab2.tablolar;

import jakarta.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "puanlar")
public class Puanlar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
    @Column(name = "kullanıcı_id",unique = true,nullable = false)
    private Long kullanıcıID;
    @Column(name = "puanlar",unique = true,nullable = false)
    private int puanlar;
    @Column(name = "kazanılan_tarih",unique = true,nullable = false)
    @Temporal(TemporalType.DATE)
    private Date gonderim_zamanı;


















    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getKullanıcıID() {
        return kullanıcıID;
    }

    public void setKullanıcıID(Long kullanıcı_id) {
        this.kullanıcıID = kullanıcı_id;
    }

    public int getPuanlar() {
        return puanlar;
    }

    public void setPuanlar(int puanlar) {
        this.puanlar = puanlar;
    }

    public Date getGonderim_zamanı() {
        return gonderim_zamanı;
    }

    public void setGonderim_zamanı(Date gonderim_zamanı) {
        this.gonderim_zamanı = gonderim_zamanı;
    }
}
