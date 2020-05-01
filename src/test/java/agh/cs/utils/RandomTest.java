package agh.cs.utils;

import agh.cs.mapEntity.movable.Animal;
import agh.cs.mapEntity.movable.Herbivore;
import lombok.Value;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RandomTest {




    @RepeatedTest(value = 12, name="Test {displayName} - {currentRepetition} / {totalRepetitions}")
    void getRandomAnimal() {
        List<Animal> animalList = new ArrayList<>();

        assertThrows(NullPointerException.class, () -> {
            Random.getRandomAnimal(animalList,null);
        });

        animalList.add(new Herbivore(null, 0, null, 0));
        animalList.add(new Herbivore(null, 1, null, 0));
        animalList.add(new Herbivore(null, 2, null, 0));
        animalList.add(new Herbivore(null, 3, null, 0));

        int energy = Random.getRandomAnimal(animalList, null).getEnergy();
        assertTrue(0 == energy || 1 == energy || 2 == energy || 3 == energy);

       Animal animal = new Herbivore(null, 4,null,0);
       animalList.add(animal);
       assertNotEquals(4, Random.getRandomAnimal(animalList, animal).getEnergy());


    }
}