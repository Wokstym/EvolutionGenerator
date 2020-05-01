package agh.cs.mapEntity.eatable;

import agh.cs.map.utils.Vector2d;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public class Grass implements EatableEntity {
    @Getter
    UUID uuid;
    @Getter
    private Vector2d position;
    @Getter
    private int foodEnergy;

    public Grass(Vector2d position, int foodEnergy) {
        this(UUID.randomUUID(), position, foodEnergy);
    }

    public String toString() {
        return "*";
    }

}
