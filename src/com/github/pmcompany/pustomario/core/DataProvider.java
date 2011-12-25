package com.github.pmcompany.pustomario.core;

import java.util.Iterator;
import java.util.List;

/**
 * @author dector (dector9@gmail.com)
 */
public interface DataProvider {
    public int getTileAt(int x, int y);
    public boolean isTileBlocked(int x, int y);

    public Point countTileByAbs(int absX, int absY);
    public Point countAbsByTile(int tX, int tY);

    public Iterator<Player> getPlayersIterator();
    public String getPlayerName();
    public int getPlayerX();
    public int getPlayerY();
    public int getPlayerX(String name);
    public int getPlayerY(String name);

    public float getPlayerSpeedX();
    public float getPlayerSpeedY();
    public boolean isPlayerWatchingRight();
    public float getPlayerSpeedX(String name);
    public float getPlayerSpeedY(String name);
    public boolean isPlayerWatchingRight(String name);

    public List<Point> getPlayerCrossedTiles();
    public List<Point> getPlayerNeighbourTiles(int direction);

    public int getMapWidth();
    public int getMapHeight();

    public int getPlayersNum();
}
