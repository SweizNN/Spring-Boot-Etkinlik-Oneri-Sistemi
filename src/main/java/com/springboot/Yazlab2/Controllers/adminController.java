package com.springboot.Yazlab2.Controllers;

import com.springboot.Yazlab2.Repository.*;
import com.springboot.Yazlab2.tablolar.Etkinlikler;
import com.springboot.Yazlab2.tablolar.Katılımcılar;
import com.springboot.Yazlab2.tablolar.Kullanıcılar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class adminController {
    @Autowired
    KullanıcılarRepository kullaniciRepository;
    @Autowired
    EtkinlikRepository etkinlikRepository;
    @Autowired
    KatılımcılarRepository katılımcılarRepository;
    @Autowired
    MesajlarRepository mesajlarRepository;
    @Autowired
    PuanlarRepository puanlarRepository;


    @GetMapping("/admin")
    public String adminMenu(Model model) {
        List<Kullanıcılar> tumKullanıcılar=kullaniciRepository.findAll();
        List<Etkinlikler> tumEtkinlikler=etkinlikRepository.findAll();
        model.addAttribute("users",tumKullanıcılar);
        model.addAttribute("events",tumEtkinlikler);
        List<Etkinlikler> onayBekleyenEtkinlikler = etkinlikRepository.findByOnayDurumu(false);//onaylanmayan etkinlikleri buluyoruz
        model.addAttribute("pendingEvents", onayBekleyenEtkinlikler);
        List<Etkinlikler> onaylanmışEtkinlikler = etkinlikRepository.findByOnayDurumu(true);//onaylananları buluyoruz
        model.addAttribute("approvedEvents", onaylanmışEtkinlikler);
        return "admin";
    }

    @PostMapping("/admin/approve-event")
    public String etkinlikOnayla(@RequestParam("id") Long id, Model model) {
        try {
            Etkinlikler etkinlik = etkinlikRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Etkinlik bulunamadı!"));
            etkinlik.setOnayDurumu(true);
            etkinlikRepository.save(etkinlik);//admin etkinliği onaylama butonuna basınca databasede kaydediyor
            model.addAttribute("successMessage", "Etkinlik başarıyla onaylandı.");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Etkinlik onaylanırken bir hata oluştu: " + e.getMessage());
        }
        return "redirect:/admin";
    }

//etkinlik detayına bakmak
    @GetMapping("/admin/event/{id}")
    public String getEventDetails(@PathVariable Long id, Model model) {
        Optional<Etkinlikler> event = etkinlikRepository.findById(id);
        if (event.isEmpty()) {
            return "redirect:/admin";
        }
        // Katılımcı bilgileri
        List<Katılımcılar> katılımcılar = katılımcılarRepository.findByEtkinlikID(id);
        List<Kullanıcılar> kullanıcılar = new ArrayList<>();
        for (Katılımcılar katılımcı : katılımcılar) {
            kullaniciRepository.findById(katılımcı.getKullanıcıID()).ifPresent(kullanıcılar::add);
        }

        model.addAttribute("event", event.get());
        model.addAttribute("kullanıcılar", kullanıcılar);

        return "adminEventDetail"; // Etkinlik detay sayfasına yönlendirme
    }
//kullanıcı detayına bakmak
    @GetMapping("/admin/user/{id}")
    public String getUserDetails(@PathVariable Long id, Model model) {
        Optional<Kullanıcılar> kullanıcı = kullaniciRepository.findById(id);
        if (kullanıcı.isPresent()) {
            model.addAttribute("user", kullanıcı.get());
            return "adminUserDetail";
        } else {
            return "admin";
        }
    }

    @PostMapping("/admin/event/delete/{id}")
    @Transactional
    public String deleteEvent(@PathVariable Long id) {//etkinliği silmek için önce katılımcılardan daha sonra mesajlardan siliyoruz en son etkinliği siliyoruz
        katılımcılarRepository.deleteByEtkinlikID(id);
        mesajlarRepository.deleteByEtkinlikID(id);
        etkinlikRepository.deleteById(id);
        return "redirect:/admin";
    }

    @PostMapping("/admin/event/update/{id}")
    public String updateEvent(@PathVariable Long id,@RequestParam("kategori") String kategori, @RequestParam("konum") String konum,
     @RequestParam("tarih") Date tarih,@RequestParam("saat") String saatStr,@RequestParam("etkinlik_suresi") String etkinlikSuresi, @RequestParam("aciklama") String aciklama,
                              @RequestParam("etkinlik_adı") String etkinlik_adı) throws ParseException {
        Optional<Etkinlikler> etkinlikler = etkinlikRepository.findById(id);
        if (etkinlikler.isPresent()) {
            Etkinlikler etkinlik = etkinlikler.get();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Time saat = new Time(sdf.parse(saatStr).getTime());
            Time sure = new Time(sdf.parse(etkinlikSuresi).getTime());
            etkinlik.setEtkinlik_adı(etkinlik_adı);
            etkinlik.setKategori(kategori);
            etkinlik.setKonum(konum);
            etkinlik.setTarih(tarih);
            etkinlik.setSaat(saat);
            etkinlik.setEtkinlik_suresi(sure);
            etkinlik.setAciklama(aciklama);
            etkinlikRepository.save(etkinlik);
        }
        return "redirect:/admin";
    }

    @PostMapping("/admin/user/update/{id}")
    public String updateUser(@PathVariable Long id,@RequestParam("ad") String ad,@RequestParam("soyad") String soyad, @RequestParam("kullanıcıAdı") String  kullanıcıAdı,
                             @RequestParam("eposta") String eposta, @RequestParam("sifre") String sifre, @RequestParam("konum") String konum,
                             @RequestParam("ilgi_alanları") String ilgi_alanları, @RequestParam("dogum_tarihi") Date dogum_tarihi, @RequestParam("cinsiyet") String cinsiyet, @RequestParam("telefon_numaras") String telefon_numarası,
                            @RequestParam("profil_fotografı") String profil_fotografı ) throws ParseException {
        Kullanıcılar kullanıcılar=kullaniciRepository.findById(id).get();//bilgileri webden çekip güncelliyoruz
        kullanıcılar.setAd(ad);
        kullanıcılar.setSoyad(soyad);
        kullanıcılar.setKullanıcıAdı(kullanıcıAdı);
        kullanıcılar.setEposta(eposta);
        kullanıcılar.setSifre(sifre);
        kullanıcılar.setKonum(konum);
        kullanıcılar.setIlgi_alanları(ilgi_alanları);
        kullanıcılar.setDogum_tarihi(dogum_tarihi);
        kullanıcılar.setCinsiyet(Kullanıcılar.Cinsiyet.valueOf(cinsiyet));
        kullanıcılar.setTelefon_numaras(telefon_numarası);
        kullanıcılar.setProfil_fotografı(profil_fotografı);
        kullaniciRepository.save(kullanıcılar);

        return "redirect:/admin";
    }
    @PostMapping("/admin/user/delete/{id}")
    @Transactional
    public String deleteUser(@PathVariable Long id){
        Kullanıcılar kullanıcılar;
        if(kullaniciRepository.findById(id).isPresent()){
             kullanıcılar=kullaniciRepository.findById(id).get();
            katılımcılarRepository.deleteByKullanıcıID(kullanıcılar.getId());
            mesajlarRepository.deleteByKullanıcıID(kullanıcılar.getId());
            puanlarRepository.deleteByKullanıcıID(kullanıcılar.getId());
            kullaniciRepository.deleteById(kullanıcılar.getId());
        }
        return "redirect:/admin";
    }


}
