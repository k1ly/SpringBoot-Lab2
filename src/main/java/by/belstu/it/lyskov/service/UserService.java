package by.belstu.it.lyskov.service;

import by.belstu.it.lyskov.entity.User;
import by.belstu.it.lyskov.exception.RoleNotFoundException;
import by.belstu.it.lyskov.exception.UserAlreadyExistsException;
import by.belstu.it.lyskov.exception.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<User> findAllUsers(Pageable pageable);

    User findUser(Long id) throws UserNotFoundException;

    void addUser(User user) throws UserAlreadyExistsException, RoleNotFoundException;

    void updateUser(Long id, User newUser) throws UserNotFoundException;

    void deleteUser(Long id);
}
