package by.belstu.it.lyskov.jwt;

import by.belstu.it.lyskov.controller.jwt.JwtManager;
import by.belstu.it.lyskov.controller.jwt.UserDetailsInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtManagerTest {

    @Autowired
    private JwtManager jwtManager;

    @Test
    void shouldValidateTokenIfUserDetailsIsValid() {
        UserDetails userDetails = new UserDetailsInfo(1L, "login", "secret", "test", "email@mail.com", new ArrayList<>());
        String token = jwtManager.generateJwt(userDetails);
        assertTrue(jwtManager.validateJwt(token, userDetails));
    }
}