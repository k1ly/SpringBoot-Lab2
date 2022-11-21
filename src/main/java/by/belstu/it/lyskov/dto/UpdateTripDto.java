package by.belstu.it.lyskov.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class UpdateTripDto {

    private String title;

    private String description;

    private LocalDateTime startDate;

    private LocalDateTime finishDate;

    private UserDto originator;

    private UserDto companion;
}
