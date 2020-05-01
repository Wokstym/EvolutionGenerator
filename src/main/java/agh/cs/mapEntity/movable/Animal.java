package agh.cs.mapEntity.movable;

import agh.cs.map.Map;
import agh.cs.map.utils.MapDirection;
import agh.cs.map.utils.Vector2d;
import agh.cs.mapEntity.utils.Genes;
import agh.cs.utils.Random;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static agh.cs.map.utils.MapDirection.NORTH;

@AllArgsConstructor
public abstract class Animal implements MovableEntity {
    @Getter
    UUID uuid;
    @Getter
    protected Vector2d position;
    @Getter
    protected int energy;
    @Getter
    protected MapDirection dir;
    protected Map map;
    @Getter
    protected Genes genes;
    protected int energyNeededToMove;

    protected PropertyChangeSupport changes;


    public void rotate(int rotationVal) {
        this.dir = this.dir.shiftDirection(rotationVal);
    }

    public void move() {
        Vector2d oldPos = this.position;
        this.position = posAfterMove();
        changes.firePropertyChange(new PropertyChangeEvent(this, "animal moved", oldPos, this.position));
        this.energy -= energyNeededToMove;
    }

    Vector2d posAfterMove() {
        Vector2d moveCoords = this.dir.toUnitVector();
        moveCoords = this.position.add(moveCoords);
        return moveCoords.makeInBoundary(map.lowerLeftMap, map.upperRightMap);
    }

    public void decreaseEnergyBy(int e) {
        this.energy -= e;
    }

    public void eat(int e) {
        this.energy += e;
    }


    public Animal breed(Animal parent2) {
        int babyEnergy = this.getEnergy() / 4 + parent2.getEnergy() / 4;
        this.decreaseEnergyBy(this.getEnergy() / 4);
        parent2.decreaseEnergyBy(parent2.getEnergy() / 4);


        Vector2d babyPos;
        List<Vector2d> emptyPosAround = new ArrayList<Vector2d>();
        MapDirection iDir = NORTH;
        for (int i = 0; i < 8; i++, iDir = iDir.shiftDirection(1)) {
            babyPos = iDir.toUnitVector().add(this.position);
            if (!this.map.isOccupied(babyPos)) emptyPosAround.add(babyPos);
        }


        babyPos = !emptyPosAround.isEmpty() ?
                emptyPosAround.get(Random.getRandomInRange(0, emptyPosAround.size() - 1)) :
                MapDirection.values()[Random.getRandomInRange(0, 7)].toUnitVector().add(this.position);

        return createMyChild(babyPos, babyEnergy, parent2);
    }

    protected abstract Animal createMyChild(Vector2d babyPos, int babyEnergy, Animal parent2);


    public String toString() {
        return this.dir.toString();
    }


    public void addPropertyChangeListener(PropertyChangeListener l) {

        changes.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        changes.removePropertyChangeListener(l);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Animal other = (Animal) obj;
        return other.uuid.equals(this.uuid);
    }

}
