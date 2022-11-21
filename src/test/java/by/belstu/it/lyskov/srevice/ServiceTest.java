package by.belstu.it.lyskov.srevice;

import by.belstu.it.lyskov.exception.UserNotFoundException;
import by.belstu.it.lyskov.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void shouldThrowExceptionIfUserNotExists() {
        assertThrows(UserNotFoundException.class, () -> userService.findUser(0L));
    }
}
