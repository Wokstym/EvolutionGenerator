package agh.cs.map.utils;

import org.junit.jupiter.api.Test;

import java.util.SortedMap;

import static agh.cs.map.utils.MapDirection.*;
import static org.junit.jupiter.api.Assertions.*;

class MapDirectionTest {

    @Test
    void shiftDirection() {

        assertEquals(EAST, NORTH.shiftDirection(2));
        assertEquals(NORTH, NORTHWEST.shiftDirection(1));
        assertEquals(NORTHEAST, NORTHWEST.shiftDirection(2));
        assertEquals(EAST, NORTHWEST.shiftDirection(3));
        assertEquals(SOUTHEAST, NORTHWEST.shiftDirection(4));
        assertEquals(SOUTH, NORTHWEST.shiftDirection(5));
        assertEquals(SOUTHWEST, NORTHWEST.shiftDirection(6));
        assertEquals(WEST, NORTHWEST.shiftDirection(7));

        assertThrows(IllegalArgumentException.class, () -> {
            NORTH.shiftDirection(-1);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            NORTH.shiftDirection(8);
        });

    }

    @Test
    void toUnitVector() {

        assertEquals(NORTH.toUnitVector(), new Vector2d(0,1));
        assertEquals(NORTHEAST.toUnitVector(), new Vector2d(1,1));
        assertEquals(EAST.toUnitVector(), new Vector2d(1,0));
        assertEquals(SOUTHEAST.toUnitVector(), new Vector2d(1,-1));
        assertEquals(SOUTH.toUnitVector(), new Vector2d(0,-1));
        assertEquals(SOUTHWEST.toUnitVector(), new Vector2d(-1,-1));
        assertEquals(WEST.toUnitVector(), new Vector2d(-1,0));
        assertEquals(NORTHWEST.toUnitVector(), new Vector2d(-1,1));

    }
}