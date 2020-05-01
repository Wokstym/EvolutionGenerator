package agh.cs.mapEntity.movable;

import agh.cs.mapEntity.eatable.Grass;
import agh.cs.mapEntity.utils.Genes;
import agh.cs.map.Map;
import agh.cs.map.utils.MapDirection;
import agh.cs.map.utils.Vector2d;
import agh.cs.utils.Random;
import lombok.Builder;

import java.beans.PropertyChangeSupport;
import java.util.Optional;
import java.util.UUID;

public class Scavenger extends Animal {


    @Builder
    private Scavenger(Vector2d initialPosition, int energy, Map map, Animal p1, Animal p2, int energyNeededToMove) {
        super(UUID.randomUUID(), initialPosition, energy, MapDirection.getRandDir(), map, new Genes(p1.getGenes(), p2.getGenes()), energyNeededToMove, null);
        this.changes = new PropertyChangeSupport(this);
    }

    @Builder
    public Scavenger(Vector2d initialPosition, int energy, Map map, int energyNeededToMove) {
        super(UUID.randomUUID(), initialPosition, energy, MapDirection.getRandDir(), map, new Genes(), energyNeededToMove, null);
        this.changes = new PropertyChangeSupport(this);
    }

    @Builder
    public Scavenger(int energy, Map map, int energyNeededToMove) {
        this(null, energy, map, energyNeededToMove);
        Vector2d pos;
        int i = 0;
        do {
            pos = Random.genVectInBound(map.lowerLeftMap.x, map.upperRightMap.x, map.lowerLeftMap.y, map.upperRightMap.y);
        } while (map.isOccupied(pos) && i++ < map.upperRightMap.x * map.upperRightMap.y);
        this.position = pos;
    }

    public void move() {
        Vector2d posAfterMove = posAfterMove();
        if (!this.map.objectsAt(posAfterMove).isEmpty()) {

            if (this.map.objectsAt(posAfterMove)
                    .stream()
                    .anyMatch(iMapElement -> iMapElement instanceof Herbivore)) return;

            Optional<Grass> grass = this.map.objectsAt(posAfterMove)
                    .stream()
                    .filter(iMapElement -> iMapElement instanceof Grass)
                    .map(iMapElement -> (Grass) iMapElement)
                    .findFirst();

            grass.ifPresent(grassVal -> this.map.deleteEatableFromMap(grassVal));
        }
        super.move();
    }

    @Override
    public void eat(int e){
        super.eat(e);
    }

    public Animal breed(Animal parent2) {
        if (!(parent2 instanceof Scavenger)) {
            throw new IllegalArgumentException("Scavenger can't breed with" + parent2);
        }
        return super.breed(parent2);
    }

    protected Animal createMyChild(Vector2d babyPos, int babyEnergy, Animal parent2) {
        return new Scavenger(babyPos, babyEnergy, this.map, this, parent2, map.moveEnergy);
    }

    public String toString() {
        return "W";
    }

}
