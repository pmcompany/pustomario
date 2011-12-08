package com.github.pmcompany.pustomario.core;

/**
 * @author dector (dector9@gmail.com)
 */
public class Player {
    private int x;
    private int y;

    private boolean watchingRight;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;

        watchingRight = true;
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

    public boolean isWatchingRight() {
        return watchingRight;
    }
}
