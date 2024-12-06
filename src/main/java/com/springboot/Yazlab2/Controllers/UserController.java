package com.springboot.Yazlab2.Controllers;

import com.springboot.Yazlab2.Repository.PuanlarRepository;
import com.springboot.Yazlab2.tablolar.Kullanıcılar;
import com.springboot.Yazlab2.Repository.KullanıcılarRepository;
import com.springboot.Yazlab2.tablolar.Puanlar;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;


@Controller
public class UserController {
    @Autowired
    KullanıcılarRepository userRepository;
    @Autowired
    PuanlarRepository puanlarRepository;

    public static ArrayList<Object> kullanıcıÖzellik = new ArrayList<>();
    @Autowired
    public UserController(KullanıcılarRepository userRepository) {
        this.userRepository = userRepository;
    }

//giriş sayfasında kullanıcı adı ve şifreyi çekiyoruz
    @PostMapping("/login")
    public String login(
            @RequestParam("kullanıcı_adı") String kullanıcıAdı,
            @RequestParam("sifre") String sifre,
            Model model,
            HttpSession session) {

        if (kullanıcıAdı.equals("admin") && sifre.equals("admin123")) {
            return "redirect:/admin";//kullanıcı adı ve sifre adminle ilgiliyse admin sayfasına at
        }

        Kullanıcılar kullanıcı = userRepository.findByKullanıcıAdı(kullanıcıAdı);
        if (kullanıcı != null && kullanıcı.getSifre().equals(sifre)) {
            // Kullanıcı adını oturuma ekliyoruz
            session.setAttribute("kullanıcıAdı", kullanıcıAdı);
            return "redirect:/home";
        } else {
            model.addAttribute("error", "Kullanıcı adı veya şifre hatalı");
            return "login";
        }
    }

    @Transactional
    @PostMapping("/forgot-password")
    public String SifreUnuttum(@RequestParam("eposta") String eposta, @RequestParam(value = "yeniSifre", required = false) String yeniSifre, Model model) {
        Kullanıcılar kullanıcı = userRepository.findByEposta(eposta);//girilen e posta ile kullanıcıyı buluyor
        if (kullanıcı == null) {
            model.addAttribute("error", "Bu eposta ile kayıtlı kullanıcı bulunamadı.");
            return "forgot-password";
        } else if (yeniSifre == null || yeniSifre.isEmpty()) {
            model.addAttribute("eposta", eposta);
            model.addAttribute("emailConfirmed", true);

            return "forgot-password";
        } else {
            userRepository.updateBySifre(eposta, yeniSifre);
            userRepository.save(kullanıcı);
            model.addAttribute("successMessage", "Şifreniz başarıyla güncellendi.");
            model.addAttribute("emailConfirmed", true);
            return "redirect:/login";
        }


    }



    @PostMapping("/register")
    public void bilgiCek(@RequestParam("kullanıcı_adı") String kullanıcı_adı, @RequestParam("sifre") String sifre, @RequestParam("ad") String ad, @RequestParam("soyad") String soyad,
                         @RequestParam("eposta") String eposta, @RequestParam("telefon_numarası") String telefon_numarası, @RequestParam("dogum_tarihi") Date dogum_tarihi, @RequestParam("ilgi_alanları") String ilgi_alan,
                         @RequestParam("konum") String konum, @RequestParam("cinsiyet") Kullanıcılar.Cinsiyet cinsiyet, @RequestParam("profil_fotografı") String profil_fotografı , Model model){
        kullanıcıÖzellik.add(kullanıcı_adı);
        kullanıcıÖzellik.add(sifre);
        kullanıcıÖzellik.add(ad);
        kullanıcıÖzellik.add(soyad);
        kullanıcıÖzellik.add(eposta);
        kullanıcıÖzellik.add(telefon_numarası);
        kullanıcıÖzellik.add(dogum_tarihi);
        kullanıcıÖzellik.add(ilgi_alan);
        kullanıcıÖzellik.add(konum);
        kullanıcıÖzellik.add(cinsiyet);
        kullanıcıÖzellik.add(profil_fotografı);
        save(kullanıcıÖzellik,model);
    }




    public void save(ArrayList<Object> kullanıcıÖzellik, Model model) {
        String kullanıcıAd = (String) kullanıcıÖzellik.get(0);
        String sifre = (String) kullanıcıÖzellik.get(1);
        String ad = (String) kullanıcıÖzellik.get(2);
        String soyad = (String) kullanıcıÖzellik.get(3);
        String ePosta = (String) kullanıcıÖzellik.get(4);
        String telefonNo = (String) kullanıcıÖzellik.get(5);
        Date dogumTarih = (Date) kullanıcıÖzellik.get(6);
        String ilgiAlan = (String) kullanıcıÖzellik.get(7);
        String konum = (String) kullanıcıÖzellik.get(8);
        Kullanıcılar.Cinsiyet cinsiyet = (Kullanıcılar.Cinsiyet) kullanıcıÖzellik.get(9);
        String profilUrl = (String) kullanıcıÖzellik.get(10);

        Kullanıcılar yeniKullanıcı = new Kullanıcılar(kullanıcıAd, sifre, ad, soyad, ePosta, telefonNo, dogumTarih, ilgiAlan, konum, cinsiyet, profilUrl);
        userRepository.save(yeniKullanıcı);
        Long kullanıcıId = yeniKullanıcı.getId();
        // Puanlar tablosunda yeni bir satır oluşturuyoruz
        Puanlar puan = new Puanlar();
        puan.setKullanıcıID(kullanıcıId);  // Kullanıcının ID'sini burada ilişkilendiriyoruz
        puan.setPuanlar(0); // Başlangıçta puan 0 olacak
        puan.setGonderim_zamanı(new Date(System.currentTimeMillis()));  // Puanın kaydedildiği zamanı atıyoruz

        // Puanları veritabanına kaydediyoruz
        puanlarRepository.save(puan);
        kullanıcıÖzellik.clear();
        model.addAttribute("successMessage", "Kullanıcı başarıyla kaydedildi!");
    }



}
