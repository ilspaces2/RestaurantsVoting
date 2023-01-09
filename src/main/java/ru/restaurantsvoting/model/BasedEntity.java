package ru.restaurantsvoting.model;

import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

@MappedSuperclass
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class BasedEntity implements Persistable<Integer> {
    private Integer id;

    @Override
    public boolean isNew() {
        return id == null;
    }
}
