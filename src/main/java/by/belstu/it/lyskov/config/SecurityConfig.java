package by.belstu.it.lyskov.config;

import by.belstu.it.lyskov.controller.filter.JwtFilter;
import by.belstu.it.lyskov.controller.jwt.AuthHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthHandler authHandler;
    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter, AuthHandler authHandler) {
        this.jwtFilter = jwtFilter;
        this.authHandler = authHandler;
    }

    @Bean
    public SpringSecurityDialect securityDialect() {
        return new SpringSecurityDialect();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(auth -> auth
                        .antMatchers("/admin/**",
                                "/api/*/update/*", "/api/*/delete/*", "/api/trips/add").hasRole("ADMIN")
                        .antMatchers("/api/users/add").anonymous()
                        .antMatchers("/api/**").hasRole("USER")
                        .antMatchers("/login/user").hasRole("USER")
                        .antMatchers("/*", "/css/**", "/js/**", "/api-docs/**", "/swagger-ui/**").permitAll()
                        .anyRequest().authenticated())
                .logout(logout -> logout.permitAll()
                        .logoutSuccessUrl("/")
                        .deleteCookies("JSESSIONID"))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint(authHandler)
                        .accessDeniedHandler(authHandler))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf().disable();
        return http.build();
    }
}
