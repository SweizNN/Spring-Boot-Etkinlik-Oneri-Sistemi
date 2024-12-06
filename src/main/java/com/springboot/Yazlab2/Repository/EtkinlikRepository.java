package com.springboot.Yazlab2.Repository;

import com.springboot.Yazlab2.tablolar.Etkinlikler;
import com.springboot.Yazlab2.tablolar.Katılımcılar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface EtkinlikRepository extends JpaRepository<Etkinlikler, Long> {
    List<Etkinlikler> findAllByIdIn(List<Long> id);
    List<Etkinlikler> findByKullanıcıId(Long kullanıcıId);
    List<Etkinlikler> findByOnayDurumu(boolean onayDurumu);



}
