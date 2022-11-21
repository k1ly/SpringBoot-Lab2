package by.belstu.it.lyskov.listener;

import by.belstu.it.lyskov.entity.Role;
import by.belstu.it.lyskov.entity.User;
import by.belstu.it.lyskov.exception.RoleNotFoundException;
import by.belstu.it.lyskov.repository.RoleRepository;
import by.belstu.it.lyskov.repository.UserRepository;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    public SetupDataLoader(PasswordEncoder passwordEncoder, RoleRepository roleRepository, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup)
            return;
        createRoleIfNotFound("ROLE_ADMIN");
        createRoleIfNotFound("ROLE_USER");
        if (userRepository.findByLogin("admin").isEmpty()) {
            Optional<Role> userRole = roleRepository.findByName("ROLE_USER");
            Optional<Role> adminRole = roleRepository.findByName("ROLE_ADMIN");
            if (userRole.isEmpty() || adminRole.isEmpty()) {
                throw new RuntimeException("Admin role doesn't exist! Init data was not set up!");
            }
            User admin = new User();
            admin.setLogin("admin");
            admin.setPassword(passwordEncoder.encode("Admin111"));
            admin.setName("admin");
            admin.setRoles(List.of(userRole.get(), adminRole.get()));
            userRepository.save(admin);
        }
        alreadySetup = true;
    }

    private void createRoleIfNotFound(String roleName) {
        Optional<Role> entity = roleRepository.findByName(roleName);
        if (entity.isEmpty()) {
            Role role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
        }
    }
}
