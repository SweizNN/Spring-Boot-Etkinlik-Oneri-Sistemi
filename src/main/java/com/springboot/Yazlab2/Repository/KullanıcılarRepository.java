package com.springboot.Yazlab2.Repository;

import com.springboot.Yazlab2.tablolar.Etkinlikler;
import com.springboot.Yazlab2.tablolar.Kullanıcılar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface KullanıcılarRepository extends JpaRepository<Kullanıcılar,Long> {
    Kullanıcılar findByKullanıcıAdı(String kullanıcıAdı);
    @Query("SELECT k FROM Kullanıcılar k WHERE k.eposta = :eposta")
    Kullanıcılar findByEposta(String eposta);
    @Modifying
    @Transactional
    @Query("UPDATE Kullanıcılar k SET k.sifre = :sifre WHERE k.eposta = :eposta")
    void updateBySifre(@Param("eposta") String eposta, @Param("sifre") String sifre);





}
