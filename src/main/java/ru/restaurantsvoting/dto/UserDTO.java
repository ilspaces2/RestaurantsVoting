package ru.restaurantsvoting.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends NamedDTO {

    @Email
    @NotBlank
    @Size(max = 120)
    String email;

    @NotBlank
    @Size(min = 5, max = 32)
    String password;
}
