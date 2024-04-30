package com.ngt.cuoiky.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/**", "/webjars/**", "/images/**",
                                "/signup/**", "/verify/**", "/auth/**",
                                "/login/**", "/logout/**", "/assets/**", "/css/**", "/product/**", "/brand/**", "/",
                                "/js/**", "/search/**", "/forgot-password/**")
                        .permitAll()
                        .anyRequest().authenticated())
                .formLogin((form) -> form
                        .loginPage("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/")
                        .permitAll())
                .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login")
                        .deleteCookies("JSESSIONID")
                        .permitAll());
                // .rememberMe((remember) -> remember
                //         .rememberMeParameter("remember-me")
                //         .key("AbcDefgKLDSLmvop_0123456789")
                //         .tokenValiditySeconds(7 * 24 * 60 * 60)); // 7 days

        return http.build();
    }

//     public void configure(WebSecurity web) {
//         web.ignoring().requestMatchers("/static/**");
//     }
}
