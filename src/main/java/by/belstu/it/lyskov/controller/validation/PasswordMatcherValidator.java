package by.belstu.it.lyskov.controller.validation;

import by.belstu.it.lyskov.dto.NewUserDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class PasswordMatcherValidator implements ConstraintValidator<PasswordMatcher, NewUserDto> {

    @Override
    public boolean isValid(NewUserDto userDto, ConstraintValidatorContext constraintValidatorContext) {
        return Objects.equals(userDto.getPassword(), userDto.getMatchingPsw());
    }
}
