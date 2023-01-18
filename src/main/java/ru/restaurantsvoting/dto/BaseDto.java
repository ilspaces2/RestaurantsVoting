package ru.restaurantsvoting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public abstract class BaseDto {

    @Schema(hidden = true)
    protected Integer id;

    @Schema(hidden = true)
    public boolean isNew() {
        return id == null;
    }
}
