package io.github.seujorgenochurras.p2pApi.api.security.config;

import io.github.seujorgenochurras.p2pApi.api.security.auth.JwtFilter;
import io.github.seujorgenochurras.p2pApi.api.security.detail.UserDetailsImplService;
import io.github.seujorgenochurras.p2pApi.domain.exception.handler.AuthenticationExceptionHandler;
import io.github.seujorgenochurras.p2pApi.domain.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private UserDetailsImplService userDetailsImplService;

    @Autowired
    private AuthenticationExceptionHandler exceptionHandler;

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withUsername("user")
            .password(encoder().encode("simManoSequiserPodePa")) //this is super dangerous remove this later pls add secret in a .env or smth
            .roles("USER")
            .build();

        UserDetails admin = User.withUsername("admin")
            .password(encoder().encode("senhadoadminmano")) //this is super dangerous remove this later pls add secret in a .env or smth
            .roles("ADMIN")
            .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider daoAuthenticationProvider =
            new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsImplService);
        daoAuthenticationProvider.setPasswordEncoder(encoder());
        return daoAuthenticationProvider;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain clientFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/home", "/", "/signup", "/login")
            .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
            .httpBasic(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .exceptionHandling(configurer -> configurer.authenticationEntryPoint(exceptionHandler));

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain blockAllRequest(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }


    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(10);
    }
}
