package ru.restaurantsvoting.security.jwt;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequestLogin {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 5, max = 32)
    private String password;
}
