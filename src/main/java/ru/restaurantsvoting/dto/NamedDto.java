package ru.restaurantsvoting.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class NamedDto extends BaseDto {

    @NotBlank
    @Size(min = 2, max = 120)
    protected String name;

    public NamedDto(String name) {
        this.name = name;
    }
}
