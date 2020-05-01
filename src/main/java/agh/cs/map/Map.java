package agh.cs.map;


import agh.cs.mapEntity.eatable.Carrion;
import agh.cs.mapEntity.eatable.EatableEntity;
import agh.cs.mapEntity.movable.Animal;
import agh.cs.mapEntity.eatable.Grass;
import agh.cs.mapEntity.IMapElement;
import agh.cs.mapEntity.movable.Herbivore;
import agh.cs.map.utils.MapVisualizer;
import agh.cs.map.utils.Vector2d;
import agh.cs.mapEntity.movable.MovableEntity;
import agh.cs.mapEntity.movable.Scavenger;
import agh.cs.utils.Random;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.lang.StrictMath.sqrt;

public class Map implements PropertyChangeListener, IMap {

    @Getter
    private Multimap<Vector2d, IMapElement> animalsCoords = ArrayListMultimap.create();
    private Set<Vector2d> whereCopulated = new HashSet<>();
    Set<Vector2d> emptySpacesInMap = new HashSet<>();
    Set<Vector2d> emptySpacesInJungle = new HashSet<>();
    public List<Animal> animals = new ArrayList<>();
    public List<Animal> babies = new ArrayList<>();

    private PropertyChangeSupport changes;
    private PropertyChangeListener evolutionScene;

    public final Vector2d lowerLeftMap;
    public final Vector2d upperRightMap;
    public final Vector2d lowerLeftJungle;
    public final Vector2d upperRightJungle;
    public final int moveEnergy;
    private final int plantEnergy;
    private final int carrionEnergy;
    private final int startEnergy;
    private final boolean isCarrionAndScavenger;


    public Map(int mapWidth, int mapHeight, int startEnergy, int moveEnergy, int plantEnergy, int carrionEnergy, int jungleRatio, boolean isCarrionAndScavenger) {
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.startEnergy = startEnergy;
        this.carrionEnergy = carrionEnergy;
        this.isCarrionAndScavenger = isCarrionAndScavenger;

        int jungleWidth = (int) (mapWidth / sqrt(jungleRatio));
        int jungleHeight = (int) (mapHeight / sqrt(jungleRatio));

        this.lowerLeftMap = new Vector2d(0, 0);
        this.upperRightMap = new Vector2d(mapWidth - 1, mapHeight - 1);

        this.lowerLeftJungle = new Vector2d((mapWidth - jungleWidth) / 2, (mapHeight - jungleHeight) / 2);
        this.upperRightJungle = new Vector2d(lowerLeftJungle.x + jungleWidth, lowerLeftJungle.y + jungleHeight);

        this.changes = new PropertyChangeSupport(this);

        for (int i = 0; i <= this.upperRightMap.x; i++) {
            for (int j = 0; j <= this.upperRightMap.y; j++) {
                updateEmptySpacesList(new Vector2d(i, j));
            }
        }
    }


    public void eatAtPos(Animal a) {

        Collection<IMapElement> objAt = objectsAt(a.getPosition());

        Optional<EatableEntity> eatable = objAt
                .stream()
                .filter(entity -> entity instanceof EatableEntity)
                .map(entity -> (EatableEntity) entity)
                .findFirst();

        if (eatable.isPresent()) {
            deleteEatableFromMap(eatable.get());
            if (eatable.get() instanceof Grass) {

                int max = objAt.stream()
                        .filter(entity -> entity instanceof Herbivore)
                        .map(entity -> ((Herbivore) entity).getEnergy())
                        .max(Integer::compareTo)
                        .orElse(-1);

                Set<Herbivore> strongestHerbivores = objAt.stream()
                        .filter(entity -> entity instanceof Herbivore)
                        .map(entity -> (Herbivore) entity)
                        .filter(animal -> animal.getEnergy() == max)
                        .collect(Collectors.toSet());

                int energyGiven = eatable.get().getFoodEnergy() / strongestHerbivores.size();

                strongestHerbivores.forEach(animal -> animal.eat(energyGiven));
            } else {
                int max = objAt.stream()
                        .filter(entity -> entity instanceof Scavenger)
                        .map(entity -> ((Animal) entity).getEnergy())
                        .max(Integer::compareTo)
                        .orElse(-1);

                objAt.stream()
                        .filter(entity -> entity instanceof Scavenger)
                        .map(entity -> (Scavenger) entity)
                        .filter(scavenger -> scavenger.getEnergy() == max)
                        .findFirst()
                        .ifPresent(luckyStrongest -> luckyStrongest.eat(eatable.get().getFoodEnergy()));

            }

        }
    }

    public void breed(Vector2d pos) {
        Collection<IMapElement> objAtAnimPos = objectsAt(pos);
        if (objAtAnimPos.size() < 2) return;
        int energyReqToCop = this.startEnergy % 2 == 0 ? this.startEnergy / 2 : (this.startEnergy / 2) + 1;

        if (whereCopulated.contains(pos)) return;


        Animal p1;
        Animal p2;

        int max = objAtAnimPos.stream()
                .filter(entity -> entity instanceof Animal)
                .map(entity -> ((Animal) entity).getEnergy())
                .max(Integer::compareTo)
                .orElse(-1);

        List<Animal> animalWithBiggestEnergy = getAnimalsWithSameEnergy(pos, max);


        if (animalWithBiggestEnergy.size() == 2) {
            p1 = animalWithBiggestEnergy.get(0);
            p2 = animalWithBiggestEnergy.get(1);
        } else if (animalWithBiggestEnergy.size() < 2) {
            p1 = animalWithBiggestEnergy.get(0);

            int secondBiggest = objAtAnimPos
                    .stream()
                    .filter(entity -> entity instanceof Animal)
                    .map(entity -> ((Animal) entity).getEnergy())
                    .filter(e -> e != max)
                    .max(Integer::compareTo)
                    .orElse(-1);
            p2 = Random.getRandomAnimal(getAnimalsWithSameEnergy(pos, secondBiggest), null);
        } else {
            p1 = Random.getRandomAnimal(animalWithBiggestEnergy, null);
            p2 = Random.getRandomAnimal(animalWithBiggestEnergy, p1);
        }

        if (p2.getEnergy() < energyReqToCop) return;
        this.babies.add(p1.breed(p2));
        whereCopulated.add(pos);
    }

    public void changeDeadAnimalToCarrion(Animal a) {
        Vector2d deadPos = a.getPosition();
        this.animals.remove(a);
        this.animalsCoords.get(deadPos).remove(a);
        if (isCarrionAndScavenger) {
            Carrion c = new Carrion(deadPos, carrionEnergy);
            this.animalsCoords.put(deadPos, c);
            changes.firePropertyChange(new PropertyChangeEvent(this, "eatable added", a, c));
        }
        changes.firePropertyChange(new PropertyChangeEvent(this, "removed animal", a, 0));
    }

    public void place(Animal animal) {

        this.animalsCoords.put(animal.getPosition(), animal);
        this.animals.add(animal);
        updateEmptySpacesList(animal.getPosition());
        changes.firePropertyChange(new PropertyChangeEvent(this, "animal created", 0, animal));
        animal.addPropertyChangeListener(this);
        animal.addPropertyChangeListener(evolutionScene);
    }

    public void genGrass() {

        Vector2d[] mapPos = this.emptySpacesInMap.toArray(new Vector2d[0]);
        Vector2d[] junglePos = this.emptySpacesInJungle.toArray(new Vector2d[0]);
        genRandomGrassFrom(mapPos);
        genRandomGrassFrom(junglePos);

    }

    private void genRandomGrassFrom(Vector2d[] positions) {
        if (positions.length == 0)
            return;

        Vector2d grassPoss = positions[Random.getRandomInRange(0, positions.length - 1)];
        Grass newGrass = new Grass(grassPoss, plantEnergy);
        this.animalsCoords.put(grassPoss, newGrass);
        changes.firePropertyChange(new PropertyChangeEvent(this, "eatable added", 0, newGrass));
        updateEmptySpacesList(grassPoss);

    }

    public void clearWhereCopulatedMap() {
        this.whereCopulated.clear();
    }

    private List<Animal> getAnimalsWithSameEnergy(Vector2d pos, int energy) {
        return objectsAt(pos)
                .stream()
                .filter(entity -> entity instanceof MovableEntity)
                .map(mEntity -> (Animal) mEntity)
                .filter(a -> a.getEnergy() == energy)
                .collect(Collectors.toList());
    }

    public void deleteEatableFromMap(EatableEntity e) {
        this.animalsCoords.remove(e.getPosition(), e);
        updateEmptySpacesList(e.getPosition());
        changes.firePropertyChange(new PropertyChangeEvent(this, "deleted eatable", e.getPosition(), e));
    }

    public boolean isOccupied(Vector2d position) {
        return this.animalsCoords.containsKey(position);
    }


    public Collection<IMapElement> objectsAt(Vector2d position) {
        return this.animalsCoords.get(position);
    }

    private void updateEmptySpacesList(Vector2d atPos) {
        if (atPos.isVectorInBoundary(lowerLeftJungle, upperRightJungle)) {
            if (isOccupied(atPos))
                this.emptySpacesInJungle.remove(atPos);
            else
                this.emptySpacesInJungle.add(atPos);
        } else {
            if (isOccupied(atPos))
                this.emptySpacesInMap.remove(atPos);
            else
                this.emptySpacesInMap.add(atPos);
        }
    }

    public void addGrass(Vector2d grassPoss) {
        this.animalsCoords.put(grassPoss, new Grass(grassPoss, plantEnergy));
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName().equals("animal moved")) {
            IMapElement animal = (IMapElement) event.getSource();
            Vector2d oldPos = (Vector2d) event.getOldValue();
            Vector2d newPos = (Vector2d) event.getNewValue();
            this.animalsCoords.remove(oldPos, animal);
            this.animalsCoords.put(newPos, animal);
            updateEmptySpacesList(oldPos);
            updateEmptySpacesList(newPos);
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        this.evolutionScene = l;
        changes.addPropertyChangeListener(l);
    }


    private MapVisualizer board = new MapVisualizer(this);

    public String toString() {
        return board.draw(lowerLeftMap, upperRightMap);
    }

}
