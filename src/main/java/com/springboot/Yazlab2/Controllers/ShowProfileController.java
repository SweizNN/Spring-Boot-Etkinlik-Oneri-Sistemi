package com.springboot.Yazlab2.Controllers;

import com.springboot.Yazlab2.Repository.EtkinlikRepository;
import com.springboot.Yazlab2.Repository.KatılımcılarRepository;
import com.springboot.Yazlab2.Repository.KullanıcılarRepository;
import com.springboot.Yazlab2.tablolar.Etkinlikler;
import com.springboot.Yazlab2.tablolar.Katılımcılar;
import com.springboot.Yazlab2.tablolar.Kullanıcılar;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ShowProfileController {
    @Autowired
    KullanıcılarRepository userRepository;
    @Autowired
    KatılımcılarRepository katılımcılarRepository;
    @Autowired
    EtkinlikRepository etkinliklerRepository;

    @Autowired
    EtkinlikRepository etkinlikRepository;

    @GetMapping("/update-profile")
    public String showProfileForm(Model model, HttpSession session) {
        String kullanıcıAdı = (String) session.getAttribute("kullanıcıAdı");
        Kullanıcılar kullanıcı = userRepository.findByKullanıcıAdı(kullanıcıAdı);

        if (kullanıcı != null) {
            List<Etkinlikler> olusturulanEtkinlikler=etkinlikRepository.findByKullanıcıId(kullanıcı.getId());//etkinliğin kullanıcı idsine göre arıyoruz ve olusturdugu etkinlikleri buluyoruz
            model.addAttribute("olusturulanEtkinlikler", olusturulanEtkinlikler);
            model.addAttribute("kullanıcı", kullanıcı);
            return "update-profile";
        } else {
            model.addAttribute("error", "Kullanıcı bulunamadı.");
            return "home";
        }
    }


    @GetMapping("/update-profile/event/{id}")
    public String etkinlikDetay(@PathVariable("id") Long id, Model model) {
        Etkinlikler etkinlik = etkinliklerRepository.findById(id).orElse(null);
        if (etkinlik != null) {//etkinliğin bilgilerini gönderiyoruz
            model.addAttribute("etkinlik", etkinlik);
            return "event-detailsProfile";
        } else {
            model.addAttribute("error", "Etkinlik bulunamadı.");
            return "home";
        }
    }

    @PostMapping("/update-profile/event/delete/{id}")
    public String deleteEvent(@PathVariable("id") Long id, Model model, HttpSession session) {
        String kullanıcıAdı = (String) session.getAttribute("kullanıcıAdı");
        Kullanıcılar kullanıcı = userRepository.findByKullanıcıAdı(kullanıcıAdı);
        if (kullanıcı != null) {
            Etkinlikler etkinlik = etkinlikRepository.findById(id).orElse(null);
            if (etkinlik != null && etkinlik.getKullanıcıId().equals(kullanıcı.getId())) {
                List<Katılımcılar> katılımcılar=katılımcılarRepository.findByEtkinlikID(etkinlik.getId());
                katılımcılarRepository.deleteAll(katılımcılar);
                etkinlikRepository.delete(etkinlik);
                model.addAttribute("successMessage", "Etkinlik başarıyla silindi.");
            } else {
                model.addAttribute("error", "Etkinlik bulunamadı.");
            }
        } else {
            model.addAttribute("error", "Kullanıcı bulunamadı.");
        }
        return "redirect:/update-profile";
    }

    @PostMapping("/update-profile/event/update/{id}")
    public String updateEvent(
            @PathVariable("id") Long id,
            @RequestParam("etkinlik_adı") String etkinlikAdı,
            @RequestParam("kategori") String kategori,
            @RequestParam("konum") String konum,
            @RequestParam("tarih") Date tarih,
            @RequestParam("etkinlik_suresi") String etkinlik_suresi,
            @RequestParam("saat") String saatStr,
            @RequestParam("aciklama") String aciklama,
            HttpSession session) throws ParseException {
        String kullanıcıAdı = (String) session.getAttribute("kullanıcıAdı");
        Kullanıcılar kullanıcı = userRepository.findByKullanıcıAdı(kullanıcıAdı);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Time saat = new Time(sdf.parse(saatStr).getTime());
        Time etkinlikSuresi = new Time(sdf.parse(etkinlik_suresi).getTime());

        if (kullanıcı != null) {
            Etkinlikler etkinlik = etkinlikRepository.findById(id).orElse(null);
            if (etkinlik != null && etkinlik.getKullanıcıId().equals(kullanıcı.getId())) {
                etkinlik.setEtkinlik_adı(etkinlikAdı);
                etkinlik.setKategori(kategori);
                etkinlik.setKonum(konum);
                etkinlik.setTarih(tarih);
                etkinlik.setEtkinlik_suresi(etkinlikSuresi);
                etkinlik.setSaat(saat);
                etkinlik.setAciklama(aciklama);
                etkinlikRepository.save(etkinlik);
            }
        }
        return "redirect:/update-profile";
    }



    @PostMapping("/home/update-profile")
    public String updateProfile(
            @RequestParam("kullanıcı_adı") String kullanıcı_adı, @RequestParam("sifre") String sifre, @RequestParam("ad") String ad, @RequestParam("soyad") String soyad,
            @RequestParam("eposta") String eposta, @RequestParam("telefon_numarası") String telefon_numarası, @RequestParam("dogum_tarihi") Date dogum_tarihi, @RequestParam("ilgi_alanları") String ilgi_alan,
            @RequestParam("konum") String konum, @RequestParam("cinsiyet") Kullanıcılar.Cinsiyet cinsiyet, @RequestParam("profil_fotografı") String profil_fotografı , Model model){

        Kullanıcılar kullanıcı = userRepository.findByKullanıcıAdı(kullanıcı_adı);
        if (kullanıcı != null) {
            kullanıcı.setKullanıcıAdı(kullanıcı_adı);
            kullanıcı.setSifre(sifre);
            kullanıcı.setAd(ad);
            kullanıcı.setSoyad(soyad);
            kullanıcı.setEposta(eposta);
            kullanıcı.setTelefon_numaras(telefon_numarası);
            kullanıcı.setDogum_tarihi(dogum_tarihi);
            kullanıcı.setIlgi_alanları(ilgi_alan);
            kullanıcı.setKonum(konum);
            kullanıcı.setCinsiyet(cinsiyet);
            kullanıcı.setProfil_fotografı(profil_fotografı);
            userRepository.save(kullanıcı);
            model.addAttribute("successMessage", "Profil başarıyla güncellendi.");
        } else {
            model.addAttribute("error", "Güncelleme sırasında hata oluştu.");
        }

return ("redirect:/update-profile");
    }


}
