package by.belstu.it.lyskov.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateApplicationDto {

    @NotNull
    private Boolean active;
}
