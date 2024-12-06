package com.springboot.Yazlab2.tablolar;

import jakarta.persistence.*;

@Entity
@Table(name = "katılımcılar")
public class Katılımcılar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
    @Column(name = "kullanıcı_id",unique = true,nullable = false)
    private Long kullanıcıID;
    @Column(name = "etkinlik_id",unique = true,nullable = false)
    private Long etkinlikID;


























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

    public Long getEtkinlikID() {
        return etkinlikID;
    }

    public void setEtkinlikID(Long etkinlik_id) {
        this.etkinlikID = etkinlik_id;
    }
}
