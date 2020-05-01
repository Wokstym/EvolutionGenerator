package agh.cs.map;

import agh.cs.map.utils.Vector2d;
import agh.cs.mapEntity.IMapElement;
import agh.cs.mapEntity.eatable.EatableEntity;
import agh.cs.mapEntity.movable.Animal;

import java.beans.PropertyChangeEvent;
import java.util.Collection;
import java.util.NavigableSet;
import java.util.TreeSet;

public interface IMap {
    void eatAtPos(Animal a);
    void breed(Vector2d pos);
    Collection<IMapElement> objectsAt(Vector2d position);
    void changeDeadAnimalToCarrion(Animal a);
    boolean isOccupied(Vector2d position);
    void place(Animal animal);
    void genGrass();

}
