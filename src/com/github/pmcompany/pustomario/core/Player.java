package com.github.pmcompany.pustomario.core;

/**
 * @author dector (dector9@gmail.com)
 */
public class Player {
    private int x;
    private int y;

    private float speedX;
    private float speedY;

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

    public void setWatchingRight(boolean watchingRight) {
        this.watchingRight = watchingRight;
    }

    public float getSpeedX() {
        return speedX;
    }

    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

    public float getSpeedY() {
        return speedY;
    }

    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }

    public void accelerateX(float accelerationX) {
        speedX += accelerationX;

        if (accelerationX != 0) {
            if (accelerationX > 0) {
                watchingRight = true;
            } else if (accelerationX <0) {
                watchingRight = false;
            }
        }
    }

    public void accelerateY(float accelerationY) {
        speedY += accelerationY;
    }
}
