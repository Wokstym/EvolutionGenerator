package agh.cs.mapEntity.movable;

import agh.cs.mapEntity.eatable.Carrion;
import agh.cs.mapEntity.eatable.EatableEntity;
import agh.cs.mapEntity.utils.Genes;
import agh.cs.map.Map;
import agh.cs.map.utils.MapDirection;
import agh.cs.map.utils.Vector2d;
import agh.cs.mapEntity.IMapElement;
import agh.cs.mapEntity.eatable.Grass;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Builder;

import java.beans.PropertyChangeSupport;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

import agh.cs.utils.Random;

import static agh.cs.map.utils.MapDirection.*;


public class Herbivore extends Animal {
    @Builder
    private Herbivore(Vector2d initialPosition, int energy, Map map, Animal p1, Animal p2, int energyNeededToMove) {
        super(UUID.randomUUID(), initialPosition, energy, MapDirection.getRandDir(), map, new Genes(p1.getGenes(), p2.getGenes()), energyNeededToMove, null);
        this.changes = new PropertyChangeSupport(this);
    }

    @Builder
    public Herbivore(Vector2d initialPosition, int energy, Map map, int energyNeededToMove) {
        super(UUID.randomUUID(), initialPosition, energy, MapDirection.getRandDir(), map, new Genes(), energyNeededToMove, null);
        this.changes = new PropertyChangeSupport(this);
    }

    @Builder
    public Herbivore(int energy, Map map, int energyNeededToMove) {
        this(Random.genVectInBound(map.lowerLeftMap.x, map.upperRightMap.x, map.lowerLeftMap.y, map.upperRightMap.y), energy, map, energyNeededToMove);
    }

    public void move() {
        Vector2d posAfterMove = posAfterMove();
        if (!this.map.objectsAt(posAfterMove).isEmpty() &&
                        this.map.objectsAt(posAfterMove)
                                .stream()
                                .anyMatch(iMapElement -> iMapElement instanceof Carrion || iMapElement instanceof Scavenger)
        )
            return;
        super.move();
    }


    public Animal breed(Animal parent2) {
        if (!(parent2 instanceof Herbivore)) {
            throw new IllegalArgumentException("Herbivore can't breed with" + parent2.getClass());
        }
        return super.breed(parent2);

    }

    protected Animal createMyChild(Vector2d babyPos, int babyEnergy, Animal parent2) {
        return new Herbivore(babyPos, babyEnergy, this.map, this, parent2, map.moveEnergy);
    }

}


