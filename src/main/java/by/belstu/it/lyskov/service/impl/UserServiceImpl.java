package by.belstu.it.lyskov.service.impl;

import by.belstu.it.lyskov.entity.Role;
import by.belstu.it.lyskov.entity.User;
import by.belstu.it.lyskov.exception.RoleNotFoundException;
import by.belstu.it.lyskov.exception.UserAlreadyExistsException;
import by.belstu.it.lyskov.exception.UserNotFoundException;
import by.belstu.it.lyskov.repository.RoleRepository;
import by.belstu.it.lyskov.repository.UserRepository;
import by.belstu.it.lyskov.service.MailService;
import by.belstu.it.lyskov.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(PasswordEncoder passwordEncoder, MailService mailService,
                           UserRepository userRepository, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Page<User> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public User findUser(Long id) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty())
            throw new UserNotFoundException("User with id \"" + id + "\" doesn't exist!");
        return user.get();
    }

    @Override
    public void addUser(User newUser) throws UserAlreadyExistsException, RoleNotFoundException {
        Optional<User> user = userRepository.findByLogin(newUser.getLogin());
        if (user.isPresent())
            throw new UserAlreadyExistsException("User already exists!");
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        Optional<Role> role = roleRepository.findByName("ROLE_USER");
        if (role.isEmpty())
            throw new RoleNotFoundException("User role doesn't exist!");
        newUser.setRoles(List.of(role.get()));
        userRepository.save(newUser);
//        new Thread(() -> mailService.sendSimpleMessage(newUser.getEmail(), "Welcome", "Вы успешно зарегистрировались!")).start();
    }

    @Override
    public void updateUser(Long id, User newUser) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty())
            throw new UserNotFoundException("User with id \"" + id + "\" doesn't exist!");
        user.get().setLogin(newUser.getLogin());
        user.get().setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.get().setName(newUser.getName());
        user.get().setRoles(newUser.getRoles());
        userRepository.save(user.get());
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
