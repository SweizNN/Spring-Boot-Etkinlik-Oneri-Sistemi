package com.springboot.Yazlab2.Repository;

import com.springboot.Yazlab2.tablolar.Katılımcılar;
import com.springboot.Yazlab2.tablolar.Puanlar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PuanlarRepository extends JpaRepository<Puanlar, Long> {
    Puanlar findByKullanıcıID(Long kullanıcıID);
    @Modifying
    @Query("DELETE FROM Puanlar p WHERE p.kullanıcıID = :kullanıcıID")
    void deleteByKullanıcıID(@Param("kullanıcıID") Long kullanıcıID);

}
