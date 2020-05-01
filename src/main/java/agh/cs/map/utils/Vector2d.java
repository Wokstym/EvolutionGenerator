package agh.cs.map.utils;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Vector2d {
    final public int x;
    final public int y;

    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }

    boolean precedes(Vector2d other) {
        return this.x <= other.x && this.y <= other.y;
    }

    boolean follows(Vector2d other) {
        return this.x >= other.x && this.y >= other.y;
    }

    Vector2d upperRight(Vector2d other) {
        return new Vector2d(Math.max(this.x, other.x), Math.max(this.y, other.y));
    }

    Vector2d lowerLeft(Vector2d other) {
        return new Vector2d(Math.min(this.x, other.x), Math.min(this.y, other.y));
    }

    public Vector2d add(Vector2d other) {
        return new Vector2d(this.x + other.x, this.y + other.y);
    }

    Vector2d subtract(Vector2d other) {
        return new Vector2d(this.x - other.x, this.y - other.y);
    }

    public Boolean isVectorInBoundary(Vector2d lowerLeft, Vector2d upperRight) {
        return this.follows(lowerLeft) && this.precedes(upperRight);
    }

    /* function used only for position shift by 1 unit */
    public Vector2d makeInBoundary(Vector2d lowLeftBound, Vector2d upRightBound) {
        int x = this.x;
        int y = this.y;
        if (x < lowLeftBound.x) {
            x = upRightBound.x + x + 1;
        }
        if (y < lowLeftBound.y) {
            y = upRightBound.y + y + 1;
        }
        if (x > upRightBound.x) {
            x = lowLeftBound.x + x % upRightBound.x-1;
        }
        if (y > upRightBound.y) {
            y = lowLeftBound.y + y % upRightBound.y-1;
        }
        return new Vector2d(x, y);
    }


    Vector2d opposite() {
        return new Vector2d(-this.x, -this.y);
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

