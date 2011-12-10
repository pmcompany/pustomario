package com.github.pmcompany.pustomario.core;

import java.util.List;

/**
 * @author dector (dector9@gmail.com)
 */
public interface DataProvider {
    public int getTileAt(int x, int y);

    public int getPlayerX();
    public int getPlayerY();
    public boolean isPlayerWatchingRight();
    public List<Point> getPlayerCrossedTiles();

    public int getMapWidth();
    public int getMapHeight();

    public void update();
}
