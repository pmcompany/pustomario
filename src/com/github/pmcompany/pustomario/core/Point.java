package com.github.pmcompany.pustomario.core;

/**
 * @author dector (dector9@gmail.com)
 */
public class Point {
    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        Point p = (Point)obj;

        if (p.getX() == x && p.getY() == y) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return x + ":" + y;
    }
}
