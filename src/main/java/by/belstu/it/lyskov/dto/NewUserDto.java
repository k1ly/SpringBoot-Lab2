package by.belstu.it.lyskov.dto;

import by.belstu.it.lyskov.controller.validation.Password;
import by.belstu.it.lyskov.controller.validation.PasswordMatcher;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@PasswordMatcher
public class NewUserDto {

    @NotBlank
    private String login;

    @Password
    private String password;

    @NotBlank
    private String matchingPsw;

    @NotBlank
    private String name;

    @Email
    private String email;
}
