package ru.restaurantsvoting.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserDto extends NamedDto {

    @Email
    @NotBlank
    @Size(max = 120)
    String email;

    @NotBlank
    @Size(min = 5, max = 32)
    String password;

    public UserDto(String name, String email, String password) {
        super(name);
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
