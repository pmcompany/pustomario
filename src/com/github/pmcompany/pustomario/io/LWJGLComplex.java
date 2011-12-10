package com.github.pmcompany.pustomario.io;

import com.github.pmcompany.pustomario.core.*;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.TrueTypeFont;

import java.util.LinkedList;
import java.util.List;

/**
 * @author dector (dector9@gmail.com)
 */
public class LWJGLComplex implements EventServer, InputServer, OutputHandler, GameManagerUser {
    private List<EventHandler> handlers;
    private GameManager gmanager;

    private GLDrawer drawer;
    private DataProvider game;

    private View view;

    // DEBUG BEGIN
    private Font textFont;
    // DEBUG END

    public LWJGLComplex(GameManager gmanager, DataProvider game, int screenWidth, int screenHeight) {
        this.gmanager = gmanager;

        try {
            Display.setDisplayMode(new DisplayMode(screenWidth, screenHeight));
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
            gmanager.turnOffGame();
        }

        drawer = new GLDrawer(screenWidth, screenHeight);
        drawer.setClearColor(View.BACK_COLOR);
        view = new View();
        this.game = game;

        handlers = new LinkedList<EventHandler>();

        // DEBUG BEGIN
        java.awt.Font font = new java.awt.Font(java.awt.Font.SERIF, java.awt.Font.PLAIN, 14);
        textFont = new TrueTypeFont(font, false);
        // DEBUG END
    }

    public void addInputHandler(InputHandler handler) {}

    public void removeInputHandler(InputHandler handler) {}

    public void checkInput() {
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            sendEvent(new GameEvent(EventType.ACCELERATE_X_PLAYER, 3.2f));
        } else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            sendEvent(new GameEvent(EventType.ACCELERATE_X_PLAYER, -3.2f));
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            sendEvent(new GameEvent(EventType.ACCELERATE_Y_PLAYER, 3.2f));
        } else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            sendEvent(new GameEvent(EventType.ACCELERATE_Y_PLAYER, -3.2f));
        }

        if (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                switch (Keyboard.getEventKey()) {
                    case Keyboard.KEY_D: gmanager.switchDebugMode(); break;
                }
            }
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            gmanager.turnOffGame();
        }
    }

    private void sendEvent(GameEvent event) {
        for (EventHandler h : handlers) {
            h.handleEvent(event);
        }
    }

    public void handleOutput() {
        drawer.update();

        drawMap();
        drawPlayer();

        Display.update();
    }

    public void addGameManager(GameManager gmanager) {
        // Formally implented
    }

    public void setTitle(String title) {
        Display.setTitle(title);
    }

    public void turnOff() {
        Display.destroy();
    }

    public boolean isCloseRequested() {
        return Display.isCloseRequested();
    }

    private void drawMap() {
//        int startTileX = view.getTileX(view.getLeftX());
//        int startTileY = view.getTileY(view.getLeftY());
//
//        char tile;
//        for (int x = startTileX; x < endTileX; x++) {
//            for (int y = startTileY; y < endTileY; y++) {
//                tile = [x][y];
//                if (tile != 0) {
//                    if (tile == '#') {
//                        drawWall(tileXToRelative(x) - startScreenX, tileYToRelative(y) - startScreenY);
//                    }
//                }
//            }
//        }

        int tile;
        for (int x = 1; x <= game.getMapWidth(); x++) {
            for (int y = 1; y <= game.getMapHeight(); y++) {
                tile = game.getTileAt(x, y);

                if (tile != 0) {
                    if (tile == '#') {
                        drawer.fillRect((x-1) * View.TILE_WIDTH, (y-1) * View.TILE_HEIGHT,
                                View.TILE_WIDTH, View.TILE_HEIGHT, View.WALL_COLOR);
                    }
                }
            }
        }
    }

    private void drawPlayer() {
        int px = game.getPlayerX();
        int py = game.getPlayerY();

        drawer.fillRect(px-1, py-1, View.TILE_WIDTH, View.TILE_HEIGHT, View.HERO_COLOR);

        int eyeX = px;
        int eyeY = py + (int)(0.65f * View.TILE_HEIGHT);
        int eyeW = (int)(0.2f * View.TILE_WIDTH);
        int eyeH = (int)(0.2f * View.TILE_HEIGHT);

        if (game.isPlayerWatchingRight()) {
            eyeX += (int)(0.60f * View.TILE_WIDTH);
        } else {
            eyeX += (int)(0.25f * View.TILE_WIDTH);
        }

        // DEBUG BEGIN
        if (gmanager.isDebugMode()) {

            Point tileP;
            Point absTileStartP;

            List<Point> crossedTiles = game.getPlayerCrossedTiles();
            for (int i = 0; i < crossedTiles.size(); i++) {
                tileP = crossedTiles.get(i);
                absTileStartP = game.countAbsByTile(tileP.getX(), tileP.getY());

//                drawer.drawString(10, 30 + i * 20, String.format("%d rect: %d:%d", i + 1, absTileStartP.getX(), absTileStartP.getY()), textFont, Color.red);
                drawer.drawRect(absTileStartP.getX() - 1, absTileStartP.getY() - 1, View.TILE_WIDTH-1, View.TILE_HEIGHT-1, PColor.BLUE);
            }

            int direction = 0;

            float speedX = game.getPlayerSpeedX();
            if (speedX != 0) {
                if (speedX > 0) {
                    direction |= VectorDirection.RIGHT;
                } else {
                    direction |= VectorDirection.LEFT;
                }
            }

            float speedY = game.getPlayerSpeedY();
            if (speedY != 0) {
                if (speedY > 0) {
                    direction |= VectorDirection.UP;
                } else {
                    direction |= VectorDirection.DOWN;
                }
            }

            List<Point> neighTiles = game.getPlayerNeighbourTiles(direction);
            for (Point neighTile : neighTiles) {
                absTileStartP = game.countAbsByTile(neighTile.getX(), neighTile.getY());

                if (game.isTileBlocked(neighTile.getX(), neighTile.getY())) {
                    drawer.drawRect(absTileStartP.getX() - 1, absTileStartP.getY() - 1, View.TILE_WIDTH-1, View.TILE_HEIGHT-1, PColor.RED);
                } else {
                    drawer.drawRect(absTileStartP.getX() - 1, absTileStartP.getY() - 1, View.TILE_WIDTH-1, View.TILE_HEIGHT-1, PColor.GREEN);
                }
            }

            drawer.drawString(10, 10, String.format("Player pos: %d:%d", px, py), textFont, Color.red);
            drawer.drawString(10, 30, String.format("Vx: %.3f\t\t Vy:%.3f", speedX, speedY), textFont, Color.red);

        }
        // DEBUG END

        drawer.fillRect(eyeX-1, eyeY-1, eyeW, eyeH, View.EYE_COLOR);
    }

    public void addEventHandler(EventHandler handler) {
        if (! handlers.contains(handler)) {
            handlers.add(handler);
        }
    }

    public void removeEventHandler(EventHandler handler) {
        handlers.remove(handler);
    }
}
