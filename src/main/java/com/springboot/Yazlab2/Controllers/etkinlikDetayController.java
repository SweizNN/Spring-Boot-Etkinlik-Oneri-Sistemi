package com.springboot.Yazlab2.Controllers;

import com.springboot.Yazlab2.Repository.EtkinlikRepository;
import com.springboot.Yazlab2.Repository.KatılımcılarRepository;
import com.springboot.Yazlab2.Repository.KullanıcılarRepository;
import com.springboot.Yazlab2.Repository.MesajlarRepository;
import com.springboot.Yazlab2.tablolar.Etkinlikler;
import com.springboot.Yazlab2.tablolar.Katılımcılar;
import com.springboot.Yazlab2.tablolar.Kullanıcılar;
import com.springboot.Yazlab2.tablolar.Mesajlar;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Controller
public class etkinlikDetayController {
    @Autowired
    EtkinlikRepository etkinliklerRepository;
    @Autowired
    MesajlarRepository mesajlarRepository;
    @Autowired
    KullanıcılarRepository userRepository;
    @Autowired
    private KullanıcılarRepository kullanıcılarRepository;
    @Autowired
    private KatılımcılarRepository katılımcılarRepository;


    @GetMapping("/home/event/{id}")
    public String etkinlikDetay(@PathVariable("id") Long id, Model model,HttpSession session) {//etkinliğe tıklayınca detaylarını gösterdiğimiz ve webe gerekli bilgileri gönderdiğimiz yer
        Etkinlikler etkinlik = etkinliklerRepository.findById(id).orElse(null);
        String kullanıcıAdı = (String) session.getAttribute("kullanıcıAdı");
        Kullanıcılar kullanıcı = userRepository.findByKullanıcıAdı(kullanıcıAdı);
        if (etkinlik != null) {
            model.addAttribute("kullanıcıKonum",kullanıcı.getKonum());
            List<Mesajlar> mesajlarList=mesajlarRepository.findByEtkinlikId(id);
            model.addAttribute("etkinlik", etkinlik);
            model.addAttribute("eventLocation", etkinlik.getKonum());
            List<Kullanıcılar> kullanıcılar=kullanıcılarRepository.findAll();
            model.addAttribute("mesajlar", mesajlarList);
            model.addAttribute("kullanıcı", kullanıcılar);
            return "event-details";
        } else {
            model.addAttribute("error", "Etkinlik bulunamadı.");
            return "home";
        }
    }


//Burada mesajlaşmayı yapıyoruz
    @PostMapping("/home/event/{id}")
    public String mesajGonder(@PathVariable("id") Long id, HttpSession session, @RequestParam("mesaj_metni") String mesajMetni, Model model) {
        String kullanıcıAdı = (String) session.getAttribute("kullanıcıAdı");
        Kullanıcılar kullanıcı = userRepository.findByKullanıcıAdı(kullanıcıAdı);
        model.addAttribute("kullanıcı",kullanıcı);
        Etkinlikler etkinlik = etkinliklerRepository.findById(id).orElse(null);
        boolean katıldıMı=false;

        if (etkinlik != null) {
            List<Katılımcılar> katılımcılar= katılımcılarRepository.findByEtkinlikID(etkinlik.getId());
            for(Katılımcılar k: katılımcılar){
                if(k.getKullanıcıID()==kullanıcı.getId()){
                    katıldıMı=true;
                }
            }
            if(katıldıMı){//eğer kullanıcı etkinliğe katıldıysa mesajı database'e kaydedilicek
                Mesajlar mesaj = new Mesajlar();
                mesaj.setEtkinlikId(etkinlik.getId());
                mesaj.setGondericiId(kullanıcı.getId());
                mesaj.setAlıcıId(etkinlik.getId());
                mesaj.setMesaj_metni(mesajMetni);
                mesaj.setGonderim_zamanı(new Timestamp(System.currentTimeMillis()));
                mesajlarRepository.save(mesaj);
            }
        }

        return "redirect:/home/event/" + id;
    }
    @GetMapping("/home/event/messages/{id}")
    @ResponseBody
    public List<Mesajlar> mesajlariGetir(@PathVariable("id") Long id) {
        return mesajlarRepository.findByEtkinlikId(id);
    }

    @GetMapping("/home/event/messages/user/{id}")
    @ResponseBody
    public Optional<Kullanıcılar> kullanıcıyıGetir(@PathVariable("id") Long id) {
        return kullanıcılarRepository.findById(id);
    }

}
