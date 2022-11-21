package by.belstu.it.lyskov.dto;

import by.belstu.it.lyskov.controller.validation.Password;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class JwtDto {

    @NotNull
    @NotBlank
    private String login;

    @NotNull
    @Password
    private String password;
}
