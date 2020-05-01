package agh.cs.utils;

import agh.cs.map.utils.Vector2d;
import agh.cs.mapEntity.movable.Animal;

import java.util.Collections;
import java.util.List;

public class Random {

    private static java.util.Random rand = new java.util.Random();

    public static int getRandomInRange(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }

    public static Vector2d genVectInBound(int minX, int maxX, int minY, int maxY) {
        int x, y;
        x = getRandomInRange(minX, maxX);
        y = getRandomInRange(minY, maxY);
        return new Vector2d(x, y);
    }

    public static Animal getRandomAnimal(List<Animal> animalList, Animal differentThan) {
        if (animalList.isEmpty()) throw new NullPointerException("Empty animalList");
        if (differentThan != null)
            animalList.remove(differentThan);
        if (animalList.isEmpty()) throw new NullPointerException("Empty animalList");
        Collections.shuffle(animalList);
        return animalList.get(0);
    }

}
