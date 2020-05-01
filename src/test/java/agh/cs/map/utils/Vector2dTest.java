package agh.cs.map.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Vector2dTest {

    @Test
    void testToString() {
        assertEquals(new Vector2d(1,2).toString(),"(1,2)");
    }

    @Test
    void precedes() {
        assertTrue (new Vector2d(1,2).precedes(new Vector2d(2,2)));
    }

    @Test
    void follows() {

            assertTrue (new Vector2d(2,3).follows(new Vector2d(1,2)));
    }

    @Test
    void upperRight() {
        assertEquals (new Vector2d(2,1).upperRight(new Vector2d(1,2)), new Vector2d(2,2));
    }

    @Test
    void lowerLeft() {
        assertEquals (new Vector2d(2,1).lowerLeft(new Vector2d(1,2)), new Vector2d(1,1));
    }

    @Test
    void add() {
        assertEquals (new Vector2d(2,1).add(new Vector2d(1,2)), new Vector2d(3,3));
    }

    @Test
    public void subtract() {
        assertEquals (new Vector2d(2,1).subtract(new Vector2d(1,2)), new Vector2d(1,-1));
    }

    @Test
    void isVectorInBoundary() {
        Vector2d lowLeft = new Vector2d( 0,0);
        Vector2d upRight = new Vector2d(3,3);

        assertTrue(new Vector2d(1,2).isVectorInBoundary(lowLeft, upRight));
        assertFalse(new Vector2d(-1,1).isVectorInBoundary(lowLeft,upRight));
        assertFalse(new Vector2d(4,4).isVectorInBoundary(lowLeft,upRight));
        assertTrue(new Vector2d(0,0).isVectorInBoundary(lowLeft, upRight));
        assertTrue(new Vector2d(3,3).isVectorInBoundary(lowLeft, upRight));

    }

    @Test
    void makeInBoundary() {

        Vector2d lowLeft = new Vector2d( 0,0);
        Vector2d upRight = new Vector2d(3,3);

        assertEquals(new Vector2d(0,0).makeInBoundary(lowLeft, upRight), new Vector2d(0,0));
        assertEquals(new Vector2d(-1,0).makeInBoundary(lowLeft,upRight), new Vector2d(3,0));
        assertEquals(new Vector2d(-1,-1).makeInBoundary(lowLeft,upRight), new Vector2d(3,3));
        assertEquals(new Vector2d(4,4).makeInBoundary(lowLeft,upRight), new Vector2d(0,0));
    }

    @Test
    public void opposite() {
        assertEquals (new Vector2d(2,1).opposite(), new Vector2d(-2,-1));
    }

}