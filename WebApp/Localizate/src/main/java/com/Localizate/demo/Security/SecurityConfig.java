package com.Localizate.demo.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Rutas permitidas para todos los usuarios
                .requestMatchers("/login", "/registro", "/css/**", "/js/**", "/images/**", "/listarEstablecimientos").permitAll()
                // Cualquier otra solicitud requiere autenticación
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login") // Página de inicio de sesión personalizada
                .defaultSuccessUrl("/listEstablecimientos", true) // Redirección después de inicio de sesión exitoso
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout") // Redirección después del cierre de sesión
                .permitAll()
            )
            .csrf().disable(); // Deshabilita CSRF para pruebas; habilitarlo en producción
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
