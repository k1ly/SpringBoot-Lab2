package by.belstu.it.lyskov.controller.filter;

import by.belstu.it.lyskov.controller.jwt.JwtManager;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@Component
@WebFilter(filterName = "JwtFilter")
public class JwtFilter extends OncePerRequestFilter {

    private final JwtManager jwtManager;
    private final UserDetailsService userDetailsService;

    public JwtFilter(JwtManager jwtManager, UserDetailsService userDetailsService) {
        this.jwtManager = jwtManager;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenHeader = request.getHeader("Authorization");
        String token = null;
        String login = null;
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            token = tokenHeader.substring(7);
            try {
                login = jwtManager.getLoginFromJwt(token);
            } catch (IllegalArgumentException e) {
                log.error("Unable to get JWT token");
            } catch (ExpiredJwtException e) {
                log.error("JWT token has expired");
            } catch (MalformedJwtException e) {
              log.error("Invalid JWT structure");
            }
        } else {
            log.warn("Bearer string not found in token (" + request.getRequestURL().toString() + ")");
        }
        if (login != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(login);
            if (jwtManager.validateJwt(token, userDetails)) {
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
            } else {
                log.error("Invalid JWT token!");
            }
        }
        filterChain.doFilter(request, response);
    }
}
