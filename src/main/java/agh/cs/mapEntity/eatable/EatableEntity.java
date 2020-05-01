package agh.cs.mapEntity.eatable;

import agh.cs.mapEntity.IMapElement;
import agh.cs.map.utils.Vector2d;

public interface EatableEntity extends IMapElement {
    int getFoodEnergy();
    Vector2d getPosition();
}
