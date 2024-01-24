package com.example.servingwebcontent.securityConfig;
import com.example.servingwebcontent.services.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity  // весь класс будет применен к глобоальной веб безопасности
@EnableMethodSecurity // чтобы работали роли
public class SecurityConfig {
    /*   метод создает пользователй и сохраняет в памяти прилоежения
     UserDetailsService - интерфейс, позволяющий проставлять данные о пользоватле
     PasswordEncoder - интерфейс для одностороннего преобразования пароля
     методы должны находиться в контексте приложения
     если мы не будем испольщовать шифрование пароля, то в БД пароль будет обычным, а те пароли, что к нам приходит зашифрованы
     возникнет ситуация, что щашифрованный пароль сравнивается с незашифрованным
      */
/*   @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails admin = User.builder().username("admin").password(passwordEncoder.encode("admin")).roles("ADMIN", "USER", "MODER").build();
        UserDetails user = User.builder().username("user").password(passwordEncoder.encode("user")).roles("USER").build();
        UserDetails moder = User.builder().username("moder").password(passwordEncoder.encode("moder")).roles("MODER").build();
        return new InMemoryUserDetailsManager(admin, user, moder);
    }*/
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        return new MyUserDetailsService();
    }

    // создаем собственные фильтры
    // HttpSecurity - позволяет конфигрировать авторизацию и аутентификацию http запросов
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.requestMatchers("people/welcome", "people/new-user").permitAll()
                        .requestMatchers("people/**").authenticated())
                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
                .build();
    }

    //  метод возвращает кодировщик
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
