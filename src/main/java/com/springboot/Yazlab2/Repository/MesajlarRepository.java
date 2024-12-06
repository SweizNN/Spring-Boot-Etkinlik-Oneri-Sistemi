package com.springboot.Yazlab2.Repository;

import com.springboot.Yazlab2.tablolar.Mesajlar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MesajlarRepository extends JpaRepository<Mesajlar, Long> {
    List<Mesajlar> findByEtkinlikId(Long etkinlikId);
    @Modifying
    @Query("DELETE FROM Mesajlar m WHERE m.etkinlikId = :etkinlikId")
    void deleteByEtkinlikID(@Param("etkinlikId") Long etkinlikId);
    @Modifying
    @Query("DELETE FROM Mesajlar m WHERE m.gondericiId = :gondericiId")
    void deleteByKullanıcıID(@Param("gondericiId") Long gondericiId);
}
