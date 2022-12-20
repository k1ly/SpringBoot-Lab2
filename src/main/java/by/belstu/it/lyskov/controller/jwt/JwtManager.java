package by.belstu.it.lyskov.controller.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtManager {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private Integer expirationPeriod;

    public String generateJwt(UserDetails userDetails) {
        Date issueDate = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        Date expirationDate = Date.from(LocalDateTime.now().plus(expirationPeriod, ChronoUnit.MINUTES)
                .atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issueDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public boolean validateJwt(String token, UserDetails userDetails) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject().equals(userDetails.getUsername()) && claims.getExpiration().after(new Date());
    }

    public String getLoginFromJwt(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }
}
