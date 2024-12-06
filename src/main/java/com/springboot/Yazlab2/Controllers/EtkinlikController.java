package com.springboot.Yazlab2.Controllers;

import com.springboot.Yazlab2.Repository.EtkinlikRepository;
import com.springboot.Yazlab2.Repository.KullanıcılarRepository;
import com.springboot.Yazlab2.Repository.PuanlarRepository;
import com.springboot.Yazlab2.tablolar.Etkinlikler;
import com.springboot.Yazlab2.tablolar.Kullanıcılar;
import com.springboot.Yazlab2.tablolar.Puanlar;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;

@Controller
public class EtkinlikController {

    @Autowired
    EtkinlikRepository etkinlikRepository;
    @Autowired
    PuanlarRepository puanlarRepository;
    @Autowired
    KullanıcılarRepository userRepository;


    @GetMapping("/home/add-event")
    public String showAddEventForm() {
        return "add-event";
    }

    @PostMapping("/home/add-event")
    public String addEvent(//yeni bir etkinlik oluştururken webden bilgileri alıp database'e kaydediyoruz
            @RequestParam("etkinlikAdi") String etkinlikAdi,
            @RequestParam("aciklama") String aciklama,
            @RequestParam("tarih") Date tarih,
            @RequestParam("saat") String saatStr,
            @RequestParam("etkinlik_suresi") String etkinlikSuresiStr,
            @RequestParam("konum") String konum,
            @RequestParam("kategori") String kategori,
            Model model, HttpSession httpSession) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Time saat = new Time(sdf.parse(saatStr).getTime());
            Time etkinlikSuresi = new Time(sdf.parse(etkinlikSuresiStr).getTime());
            String kullanıcıAdı = (String) httpSession.getAttribute("kullanıcıAdı");
            Kullanıcılar kullanıcı = userRepository.findByKullanıcıAdı(kullanıcıAdı);
            Etkinlikler etkinlik = new Etkinlikler();
            etkinlik.setEtkinlik_adı(etkinlikAdi);
            etkinlik.setAciklama(aciklama);
            etkinlik.setTarih(tarih);
            etkinlik.setSaat(saat);
            etkinlik.setEtkinlik_suresi(etkinlikSuresi);
            etkinlik.setKonum(konum);
            etkinlik.setKategori(kategori);
            etkinlik.setKullanıcıId(kullanıcı.getId());
            etkinlik.setOnayDurumu(false);
//etkinlik durumuna göre puanlama
            Puanlar puan=puanlarRepository.findByKullanıcıID(kullanıcı.getId());
            if(puan==null){
                puan=new Puanlar();
                puan.setKullanıcıID(kullanıcı.getId());
                puan.setGonderim_zamanı(new Date(System.currentTimeMillis()));
                puan.setPuanlar(15);
            }else{
                puan.setGonderim_zamanı(new Date(System.currentTimeMillis()));
                puan.setPuanlar(puan.getPuanlar()+15);
            }

            etkinlikRepository.save(etkinlik);
            puanlarRepository.save(puan);
            model.addAttribute("successMessage", "Etkinlik başarıyla eklendi.");

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());

            return "add-event";
        }
        return "redirect:/home";
    }
}
