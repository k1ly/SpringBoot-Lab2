package by.belstu.it.lyskov.controller.rest;

import by.belstu.it.lyskov.controller.mapper.DtoMapper;
import by.belstu.it.lyskov.dto.JwtDto;
import by.belstu.it.lyskov.controller.jwt.JwtManager;
import by.belstu.it.lyskov.dto.UserDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class JwtController {

    private final AuthenticationManager authenticationManager;
    private final JwtManager jwtManager;
    private final UserDetailsService userDetailsService;

    private final DtoMapper dtoMapper;

    public JwtController(AuthenticationManager authenticationManager, JwtManager jwtManager,
                         UserDetailsService userDetailsService, DtoMapper dtoMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtManager = jwtManager;
        this.userDetailsService = userDetailsService;
        this.dtoMapper = dtoMapper;
    }

    @PostMapping("/login")
    public String createToken(@Valid @RequestBody JwtDto jwtDto) throws AuthenticationException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtDto.getLogin(), jwtDto.getPassword()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtDto.getLogin());
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        return jwtManager.generateJwt(userDetails);
    }

    @PostMapping("/login/user")
    public UserDto getUser(@RequestParam String token) throws UsernameNotFoundException {
        UserDto userDto = null;
        String login = jwtManager.getLoginFromJwt(token);
        if (login != null)
            userDto = dtoMapper.map(userDetailsService.loadUserByUsername(login), UserDto.class);
        return userDto;
    }
}
