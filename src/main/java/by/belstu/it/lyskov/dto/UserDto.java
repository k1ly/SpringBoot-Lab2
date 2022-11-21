package by.belstu.it.lyskov.dto;

import by.belstu.it.lyskov.entity.Role;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Data
public class UserDto {

    @Id
    @NotNull
    private Long id;

    @NotBlank
    private String name;

    @Email
    private String email;

    @NotNull
    @NotEmpty
    private Collection<Role> roles;
}
