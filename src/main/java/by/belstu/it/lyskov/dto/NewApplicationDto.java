package by.belstu.it.lyskov.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class NewApplicationDto {

    @NotNull
    private Boolean active;

    @NotNull
    private TripDto trip;

    @NotNull
    private UserDto originator;

    @NotNull
    private UserDto companion;
}
