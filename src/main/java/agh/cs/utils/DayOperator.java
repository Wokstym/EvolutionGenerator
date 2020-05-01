package agh.cs.utils;

import agh.cs.map.Map;
import agh.cs.mapEntity.movable.Animal;
import agh.cs.mapEntity.movable.Herbivore;
import agh.cs.mapEntity.movable.Scavenger;
import agh.cs.mapEntity.utils.Genes;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


public class DayOperator {
    @Getter
    private Map map;
    private List<Animal> dead;


    public DayOperator(Map map, int nrOfStartHerbivores, int nrOfStartScavenger, int startEnergy, int energyNeededToMove) {

        this.map = map;
        dead = new ArrayList<>();
        for (int i = 0; i < nrOfStartHerbivores; i++)
            map.place(new Herbivore(startEnergy, map, energyNeededToMove));

        for (int i = 0; i < nrOfStartScavenger; i++)
            map.place(new Scavenger(startEnergy, map, energyNeededToMove/2));

    }

    public void nextDay() {

        dead.clear();
        map.clearWhereCopulatedMap();
        map.babies.clear();

        map.animals
                .stream()
                .filter(a -> a.getEnergy() <= 0)
                .forEach(a -> dead.add(a));

        dead.forEach(a -> map.changeDeadAnimalToCarrion(a));

        map.animals.forEach(a -> a.rotate(a.getGenes().getGenesArray()[Random.getRandomInRange(0, Genes.NROFGENES - 1)]));

        map.animals.forEach(Animal::move);

        map.animals.forEach(a -> map.eatAtPos(a));

        map.animals.forEach(a -> map.breed(a.getPosition()));

        map.babies.forEach(b -> map.place(b));

        map.genGrass();

    }
}
