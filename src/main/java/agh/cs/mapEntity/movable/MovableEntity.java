package agh.cs.mapEntity.movable;

import agh.cs.mapEntity.IMapElement;
import agh.cs.map.utils.Vector2d;
import agh.cs.mapEntity.eatable.EatableEntity;

import java.beans.PropertyChangeListener;

public interface MovableEntity extends IMapElement {
    Vector2d getPosition();
    void rotate(int rotationVal);
    void move();
    void decreaseEnergyBy(int energyGained);
    void addPropertyChangeListener(PropertyChangeListener l);
    void removePropertyChangeListener(PropertyChangeListener l);
    Animal breed (Animal parent2);
    public void eat(int e);


}
