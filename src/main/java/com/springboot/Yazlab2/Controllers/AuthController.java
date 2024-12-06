package com.springboot.Yazlab2.Controllers;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class AuthController {

    @Bean//spring boot securitysini kaldırıyoruz
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                // /register ve /login gibi sayfalara erişime izin veriyoruz
                .requestMatchers("/**").permitAll()
                // Diğer tüm isteklere kimlik doğrulama yapılması gerektiğini belirtiyoruz
                .anyRequest().authenticated()
                .and().csrf(AbstractHttpConfigurer::disable);  // Eğer CSRF koruması istemiyorsanız devre dışı bırakabilirsiniz

        return http.build();
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationPage() {
        return "register";
    }






    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "forgot-password"; // forgot-password.html sayfasına yönlendirin
    }

}
