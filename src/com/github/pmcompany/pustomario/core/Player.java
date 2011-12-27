package com.github.pmcompany.pustomario.core;

import com.github.pmcompany.pustomario.io.PColor;

/**
 * @author dector (dector9@gmail.com)
 */
public class Player {
    public static final int START_HP = 100;

    private String name;
    private int x;
    private int y;
    private int prevX;
    private int prevY;

    private int hp = START_HP;

    private float speedX;
    private float speedY;

    private boolean watchingRight;
    private boolean canJump;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;

        watchingRight = true;
        canJump = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean isCanJump() {
        return canJump;
    }

    public void setCanJump(boolean canJump) {
        this.canJump = canJump;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getPrevX() {
        return prevX;
    }

    public void setPrevX(int prevX) {
        this.prevX = prevX;
    }

    public int getPrevY() {
        return prevY;
    }

    public void setPrevY(int prevY) {
        this.prevY = prevY;
    }

    public boolean moved() {
        return (getPrevX() != getX()) || (getPrevY() != getY());
    }

    public Point getPosition() {
        return new Point(getX(), getY());
    }

    public int changeHP(int delta) {
        setHp(getHp() + delta);
        return getHp();
    }
}
