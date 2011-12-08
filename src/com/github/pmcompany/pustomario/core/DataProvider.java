package com.github.pmcompany.pustomario.core;

/**
 * @author dector (dector9@gmail.com)
 */
public interface DataProvider {
    public int getTileAt(int x, int y);

    public int getPlayerX();
    public int getPlayerY();

    public int getMapWidth();
    public int getMapHeight();
}
