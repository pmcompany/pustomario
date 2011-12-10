package com.github.pmcompany.pustomario.core;

import java.util.List;

/**
 * @author dector (dector9@gmail.com)
 */
public interface DataProvider {
    public int getTileAt(int x, int y);
    public boolean isTileBlocked(int x, int y);

    public Point countTileByAbs(int absX, int absY);
    public Point countAbsByTile(int tX, int tY);

    public int getPlayerX();
    public int getPlayerY();
    public float getPlayerSpeedX();
    public float getPlayerSpeedY();
    public boolean isPlayerWatchingRight();

    public List<Point> getPlayerCrossedTiles();
    public List<Point> getPlayerNeighbourTiles(int direction);

    public int getMapWidth();
    public int getMapHeight();
}
