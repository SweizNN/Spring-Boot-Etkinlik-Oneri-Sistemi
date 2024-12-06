package com.springboot.Yazlab2.Controllers;

import com.springboot.Yazlab2.Repository.*;
import com.springboot.Yazlab2.tablolar.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.sql.Date;
import java.util.*;


@Controller
public class homeController {
    @Autowired
    KullanıcılarRepository userRepository;
    @Autowired
    EtkinlikRepository etkinliklerRepository;
    @Autowired
    KatılımcılarRepository katılımcılarRepository;
    @Autowired
    PuanlarRepository puanlarRepository;


    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        String kullanıcıAdı = (String) session.getAttribute("kullanıcıAdı");
        if (kullanıcıAdı != null) {
            Kullanıcılar kullanıcı = userRepository.findByKullanıcıAdı(kullanıcıAdı);
            model.addAttribute("kullanıcı", kullanıcı);
            Puanlar puan = puanlarRepository.findByKullanıcıID(kullanıcı.getId());
            List<Etkinlikler> etkinliklerListesi = etkinliklerRepository.findAll();//tüm etkinliklerin oldugu liste
            ArrayList<Etkinlikler> ilgiAlanEtkinlikler=new ArrayList<>();
            ArrayList<Etkinlikler> digerEtkinlikler=new ArrayList<>();
            List<Katılımcılar> katılımcılar = katılımcılarRepository.findByKullanıcıID(kullanıcı.getId());
            ArrayList<Long> etkinlikIds = new ArrayList<>();
            for (Katılımcılar katılımcı : katılımcılar) {
                etkinlikIds.add(katılımcı.getEtkinlikID());
            }
            model.addAttribute("toplamPuan",puan.getPuanlar());
/* Bu kısım etkinlik öneri sistemi önce kullanıcının ilgi alanıyla tam uyuşan var mı diye kontrol ediliyor ve en üste alınıyor daha sonra ise katılım geçmişine göre*/

            Map<String, Integer> kategoriSıklığı = new HashMap<>();//Katılım geçmişine göre sorgulama için önceden katıldığı kategori sayılarını tutuyoruz
            for (Katılımcılar katılımcı : katılımcılar) {
                Etkinlikler etkinlik = etkinliklerRepository.findById(katılımcı.getEtkinlikID()).orElse(null);
                if (etkinlik != null) {
                    String kategori = etkinlik.getKategori().toLowerCase();
                    kategoriSıklığı.put(kategori, kategoriSıklığı.getOrDefault(kategori, 0) + 1);
                    System.out.println("Etkinlik Öneri Sistemi Kategorileri:  ");
                    System.out.println(kategoriSıklığı);
                }
            }

            ArrayList<Etkinlikler> katılımGecmisiArr=new ArrayList<>();
            ArrayList<Etkinlikler> konumArr=new ArrayList<>();
            String[] kullanıcıİlgiAlanları=kullanıcı.getIlgi_alanları().split(" ");//kullanıcının ilgi alanını dizide tutuyoruz ve split ile ayırıyoruz
            for(Etkinlikler etkinlikler : etkinliklerListesi) {
                boolean eklendi = false;
                for(String s:kullanıcıİlgiAlanları) {
                    if (s.equalsIgnoreCase(etkinlikler.getKategori())) {//büyük küçük harf farkını kapatıyoruz ve kullanıcının ilgi alanıyla kategori uyuşuyo mu bakıyoruz
                        ilgiAlanEtkinlikler.add(etkinlikler);
                        eklendi = true;
                        break;//arrayliste birden fazla eklememek için break
                    }
                }

                if (!eklendi && kategoriSıklığı.containsKey(etkinlikler.getKategori().toLowerCase())) {
                    katılımGecmisiArr.add(etkinlikler); // Burada diger etkinlikler arraylistine katılım geçmişine göre sıralamayı atıyoruz
                    eklendi = true;
                }

                 if(!eklendi && etkinlikler.getKonum().equalsIgnoreCase(kullanıcı.getKonum())){//Burada en son konuma göre kontrol ediyoruz ve sıralamayı tamamlıyoruz
                    konumArr.add(etkinlikler);
                    eklendi = true;
                }

                // eğer hiçbir sıralamaya uymazsa rastgele ekliyouz
                else if (!eklendi) {
                    digerEtkinlikler.add(etkinlikler);
                }
            }

            ilgiAlanEtkinlikler.addAll(katılımGecmisiArr);
            ilgiAlanEtkinlikler.addAll(konumArr);
            ilgiAlanEtkinlikler.addAll(digerEtkinlikler);
            ArrayList<Etkinlikler> onaylıEtkinlikler=new ArrayList<>();
            for(Etkinlikler etkinlik: ilgiAlanEtkinlikler) {
                if(etkinlik.isOnayDurumu()) {//burada adminin onaylamadığı etkinlikleri atmıyoruz admin onaylaması lazım
                    onaylıEtkinlikler.add(etkinlik);
                }
                model.addAttribute("etkinlikler", onaylıEtkinlikler);
            }
            List<Etkinlikler> katılınanEtkinlikler = etkinliklerRepository.findAllByIdIn(etkinlikIds);
            model.addAttribute("katılınanEtkinlikler", katılınanEtkinlikler);//katıldığım etkinlikleri web'e gönderiyoruz
            return "home";
        } else {
            model.addAttribute("error", "Hata");
            return "login";
        }
    }






    @PostMapping("/home/join/{etkinlikId}")
    @ResponseBody
    public Map<String, String> etkinliğeKatıl(@RequestParam("etkinlik_id") Long etkinlikId, HttpSession session) {
        Map<String, String> response = new HashMap<>();
        List<Etkinlikler> tumEtkinlikler = etkinliklerRepository.findAll();//tüm etkinliklerin listesi
        String kullanıcıAdı = (String) session.getAttribute("kullanıcıAdı");//sistemdeki kullanıcıyı buluyor
        if (kullanıcıAdı != null) {
            Kullanıcılar kullanıcı = userRepository.findByKullanıcıAdı(kullanıcıAdı);
            List<Katılımcılar> katılımcılar = katılımcılarRepository.findByKullanıcıID(kullanıcı.getId());
            ArrayList<Long> etkinlikIds = new ArrayList<>();
            for (Katılımcılar katılımcı : katılımcılar) {
                etkinlikIds.add(katılımcı.getEtkinlikID());
            }
            Etkinlikler etkinlik = etkinliklerRepository.findById(etkinlikId).orElse(null);
            List<Etkinlikler> katılınanEtkinlikler = etkinliklerRepository.findAllByIdIn(etkinlikIds);
            if (!katılımcılarRepository.existsByKullanıcıIDAndEtkinlikID(kullanıcı.getId(), etkinlikId)) {//kullanıcı id ve etkinlik id ile katılımcıyı buluyoruz
                Katılımcılar katılımcı = new Katılımcılar();
                katılımcı.setKullanıcıID(kullanıcı.getId());//katılımcı tablosuna kaydiyoruz
                katılımcı.setEtkinlikID(etkinlikId);
                for(Etkinlikler katıldıgımetkinlik:katılınanEtkinlikler){
                    assert etkinlik != null;
                    if(katıldıgımetkinlik.getSaat().equals(etkinlik.getSaat()) && katıldıgımetkinlik.getTarih().equals(etkinlik.getTarih())){ //burada saat ve tarihe göre etkinliğin çakışma kontrolünü yapıyoruz
                        ArrayList<String> alternatifEtkinlikler = new ArrayList<>();
                        for(Etkinlikler etkinlikler:tumEtkinlikler){
                            if (!katılınanEtkinlikler.contains(etkinlikler) && !(etkinlikler.getSaat().equals(etkinlik.getSaat()) && etkinlikler.getTarih().equals(etkinlik.getTarih()))) { // Eğer zaman uyuşmazlığı varsa uyuşmayan diğer etkinliklerden öneri yap
                                if(etkinlikler.isOnayDurumu()) {
                                    if(!alternatifEtkinlikler.contains(etkinlikler.getEtkinlik_adı())) {
                                        alternatifEtkinlikler.add(etkinlikler.getEtkinlik_adı());
                                    }
                                }
                               // alternatifEtkinlikler.add(etkinlikler.getEtkinlik_adı());
                            }
                        }
                        if (!alternatifEtkinlikler.isEmpty()) {
                            response.put("message", "Zaman uyumsuz. Önerilen Etkinlikler:");//burada alternatif etkinlikleri web'e gönderiyoruz
                            response.put("alternatif", String.valueOf(alternatifEtkinlikler));
                        } else {
                            response.put("message", "Zaman uyumsuz, ancak başka önerilen etkinlik yok.");
                        }

                        return response;
                    }

                }

//Puan ile ilgili kısım
                Puanlar puan=puanlarRepository.findByKullanıcıID(kullanıcı.getId());
                if(puan==null){
                    puan=new Puanlar();
                    puan.setKullanıcıID(kullanıcı.getId());
                    puan.setPuanlar(0);
                    puan.setGonderim_zamanı(new Date(System.currentTimeMillis()));
                    if(katılınanEtkinlikler.isEmpty()){
                        puan.setPuanlar(20);
                        puanlarRepository.save(puan);
                    }
                }else{
                    puan.setGonderim_zamanı(new Date(System.currentTimeMillis()));
                    if(katılınanEtkinlikler.isEmpty()){
                        puan.setPuanlar(puan.getPuanlar()+20);
                        puanlarRepository.save(puan);
                    }else{
                        puan.setPuanlar(puan.getPuanlar()+10);
                        puanlarRepository.save(puan);
                    }
                }
                katılımcılarRepository.save(katılımcı);
                response.put("message", "Etkinliğe katıldınız.");
            } else {
                response.put("message", "Bu etkinliğe zaten katıldınız.");
            }
            return response;
        } else {
            return response;
        }
    }



}
