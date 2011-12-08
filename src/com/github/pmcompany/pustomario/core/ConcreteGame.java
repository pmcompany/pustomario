package com.github.pmcompany.pustomario.core;

import com.github.pmcompany.pustomario.io.View;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author dector (dector9@gmail.com)
 */
public class ConcreteGame implements EventHandler, DataProvider {
    private Map map;
    private Player player;

    public ConcreteGame() {
    }

    public void initGame() {
        loadMap();
    }

    private void loadMap() {
        try {
            BufferedReader reader;
            reader = new BufferedReader(new FileReader("level.pml"));

            String[] levelParams = reader.readLine().split(" ");

            int levelWidth = Integer.parseInt(levelParams[0]);
            int levelHeight = Integer.parseInt(levelParams[1]);
            map = new Map(levelWidth, levelHeight);

            int tile;
            for (int j = levelHeight - 1; j >= 0; j--) {
                String levelLine = reader.readLine();
                for (int i = 0; i < levelWidth; i++) {
                    tile = levelLine.charAt(i);
                    if (tile == '#') {
                        map.setAt(i, j, levelLine.charAt(i));
                    } else if (tile == '@') {
                        player = new Player(i * View.TILE_WIDTH + View.TILE_XRADIUS,
                                j * View.TILE_HEIGHT + View.TILE_YRADIUS);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (player == null) {
            player = new Player(1, 1);
        }
    }

    public void handleEvent(Event e) {
    }

    public int getTileAt(int x, int y) {
        return map.getAt(x, y);
    }

    public int getPlayerX() {
        return player.getX();
    }

    public int getPlayerY() {
        return player.getY();
    }

    public int getMapWidth() {
        return map.getWidth();
    }

    public int getMapHeight() {
        return map.getHeight();
    }

    public boolean isPlayerWatchingRight() {
        return player.isWatchingRight();
    }
}
