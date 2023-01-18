package ru.restaurantsvoting.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class NamedDto extends BaseDto {

    @NotBlank
    @Size(min = 2, max = 120)
    protected String name;

}
