package by.belstu.it.lyskov.service.impl;

import by.belstu.it.lyskov.controller.jwt.UserDetailsInfo;
import by.belstu.it.lyskov.entity.User;
import by.belstu.it.lyskov.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByLogin(login);
        if (user.isEmpty())
            throw new UsernameNotFoundException("User with login \"" + login + "\" doesn't exist!");
        return new UserDetailsInfo(user.get().getId(),
                user.get().getLogin(), user.get().getPassword(),
                user.get().getName(), user.get().getEmail(), user.get().getRoles());
    }
}
