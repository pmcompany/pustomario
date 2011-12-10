package com.github.pmcompany.pustomario.core;

/**
 * @author dector (dector9@gmail.com)
 */
public class Map {
    // Index = X + width * Y
    //
    //
    //  x+w*(y+1)-1  x+w*(y+1)  x+w*(y+1)+1
    //             \     ^    /
    //
    //  x+w*y-1   <    x+w*y   >    x+w*y+1
    //
    //             /     V    \
    //  x+w*(y-1)-1  x+w*(y-1)  x+w*(y-1)+1
    private int[] grid;
    private int width;
    private int height;

    public Map(int width, int height) {
        this.width = width;
        this.height = height;

        grid = new int[width*height];
    }

    public int getAt(int x, int y) {
        return grid[index(x, y)];
    }

    public boolean isBlocked(int x, int y) {
        return getAt(x, y) == '#';
    }

    public void setAt(int x, int y, int value) {
        grid[index(x, y)] = value;
    }

    private int index(int x, int y) {
        return x-1 + width*(y-1);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
