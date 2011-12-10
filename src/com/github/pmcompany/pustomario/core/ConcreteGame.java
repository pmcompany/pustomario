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

    private long updateTime;

    public ConcreteGame() {
    }

    public void initGame() {
        loadMap();

        updateTime = System.currentTimeMillis();
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
            for (int j = levelHeight; j > 0; j--) {
                String levelLine = reader.readLine();
                for (int i = 1; i <= levelWidth; i++) {
                    tile = levelLine.charAt(i-1);
                    if (tile == '#') {
                        map.setAt(i, j, levelLine.charAt(i-1));
                    } else if (tile == '@') {
                        player = new Player((i-1) * View.TILE_WIDTH + 1,
                                (j-1) * View.TILE_HEIGHT + 1);
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
        switch (e.getType()) {
            case ACCELERATE_X_PLAYER: {
                player.accelerateX(e.getFloatValue() * Physics.RUN_ACCELERATION_COEFFICIENT);
            } break;
            case ACCELERATE_Y_PLAYER: {
                player.accelerateY(e.getFloatValue() * Physics.RUN_ACCELERATION_COEFFICIENT);
            } break;
        }
    }

    public int getTileAt(int x, int y) {
        return map.getAt(x, y);
    }

    public boolean isTileBlocked(int x, int y) {
        return map.isBlocked(x, y);
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

    public void update() {
        long currTime = System.currentTimeMillis();
        long diff = currTime - updateTime;

        // Count dynamic objects position,
        // using physical laws and time value

        movePlayer();

        updateTime = currTime;
    }

    private void movePlayer() {
        int px = player.getX();
        int py = player.getY();
        float speedX = player.getSpeedX();
        float speedY = player.getSpeedY();

//        // Taske 4 point (which are in player coners)
//        // Apply for each point SpeedX and SpeedY vector
//
//        double speedSum = Math.sqrt(Math.pow(speedX, 2) + Math.pow(speedY, 2));
//
//
//
//        double cosFi = speedX / speedSum;
//        double sinFi = speedY / speedSum;
//
//        int step = Math.floor(View.TILE_WIDTH / );
//
//        // Test right-move with collisions
//        // Get tiles, what player is on
//
//        int[][] playerTiles = getPlayerTiles();
//
//
//        int dx = 0;
//        if (speedX > 0) {
//            for (int i = 0; i < speedX / View.TILE_WIDTH; ) {
//
//            }
//        }

        player.setX(px + (int)speedX);
        player.setY(py + (int)speedY);

        player.setSpeedX(0);
        player.setSpeedY(0);
    }

//    private int[][] getPlayerTiles() {
//        int[][] pTiles = new int[4][2];
//
//        int px = player.getX();
//        int py = player.getY();
//        int rx = View.TILE_XRADIUS;
//        int ry = View.TILE_YRADIUS;
//
//        pTiles[0][0] = px - rx;
//        pTiles[0][1] = py + ry;
//        pTiles[1][0] = px + rx;
//        pTiles[1][1] = py + ry;
//        pTiles[2][0] = px + rx;
//        pTiles[2][1] = py - ry;
//        pTiles[3][0] = px - rx;
//        pTiles[3][1] = py - ry;
//
//        return pTiles;
//    }
}
