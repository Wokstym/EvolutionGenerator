package agh.cs.map.utils;


import java.util.Random;

public enum MapDirection {
    NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST , NORTHWEST;

    public String toString() {
        switch (this) {
            case EAST:
                return ">";
            case WEST:
                return "<";
            case NORTH:
                return "^";
            case SOUTH:
                return "v";
            case NORTHEAST:
                return "¬";
            case NORTHWEST:
                return "┌";
            case SOUTHEAST:
                return "┘";
            case SOUTHWEST:
                return "L";
        }
        return null;
    }

    static public MapDirection getRandDir()
    {
        Random rand = new Random();
        return MapDirection.values()[rand.nextInt(MapDirection.values().length)];
    }

    public MapDirection shiftDirection(int rotationVal)
    {
        if(rotationVal<0 || rotationVal>7) throw new IllegalArgumentException("Incorect rotation ");
        int index = (this.ordinal() + rotationVal) % MapDirection.values().length;
        return MapDirection.values()[index];
    }

    public Vector2d toUnitVector() {
        switch (this) {
            case EAST:
                return new Vector2d(1, 0);
            case WEST:
                return new Vector2d(-1, 0);
            case NORTH:
                return new Vector2d(0, 1);
            case SOUTH:
                return new Vector2d(0, -1);
            case NORTHEAST:
                return new Vector2d(1, 1);
            case NORTHWEST:
                return new Vector2d(-1, 1);
            case SOUTHEAST:
                return new Vector2d(1, -1);
            case SOUTHWEST:
                return new Vector2d(-1, -1);
        }
        throw  new IllegalArgumentException("Not Existing dir");
    }
}

