package by.belstu.it.lyskov.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class NewTripDto {

    @NotNull
    @NotBlank
    private String title;

    @NotNull
    @NotBlank
    private String description;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime finishDate;
}
