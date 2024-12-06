package com.springboot.Yazlab2.Repository;

import com.springboot.Yazlab2.tablolar.Katılımcılar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KatılımcılarRepository extends JpaRepository<Katılımcılar, Long> {
    boolean existsByKullanıcıIDAndEtkinlikID(Long kullanıcıID, Long etkinlikID);
    List<Katılımcılar> findByKullanıcıID(Long kullanıcıID);
    List<Katılımcılar> findByEtkinlikID(Long etkinlikID);
    @Modifying
    @Query("DELETE FROM Katılımcılar k WHERE k.etkinlikID = :etkinlikID")
    void deleteByEtkinlikID(@Param("etkinlikID") Long etkinlikID);
    @Modifying
    @Query("DELETE FROM Katılımcılar k WHERE k.kullanıcıID = :kullanıcıID")
    void deleteByKullanıcıID(@Param("kullanıcıID") Long kullanıcıID);

}
