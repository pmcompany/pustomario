package com.github.pmcompany.pustomario.io;

import com.github.pmcompany.pustomario.core.*;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

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
    }

    public void addInputHandler(InputHandler handler) {}

    public void removeInputHandler(InputHandler handler) {}

    public void checkInput() {
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            sendEvent(new GameEvent(EventType.ACCELERATE_PLAYER, 3.2f));
        } else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            sendEvent(new GameEvent(EventType.ACCELERATE_PLAYER, -3.2f));
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
        for (int x = 0; x < game.getMapWidth(); x++) {
            for (int y = 0; y < game.getMapHeight(); y++) {
                tile = game.getTileAt(x, y);

                if (tile != 0) {
                    if (tile == '#') {
                        drawer.drawRect(x * View.TILE_WIDTH, y * View.TILE_HEIGHT,
                                View.TILE_WIDTH, View.TILE_HEIGHT, View.WALL_COLOR);
                    }
                }
            }
        }
    }

    private void drawPlayer() {
        int px = game.getPlayerX();
        int py = game.getPlayerY();

        drawer.drawRect(px - View.TILE_XRADIUS, py - View.TILE_YRADIUS,
                View.TILE_WIDTH, View.TILE_HEIGHT, View.HERO_COLOR);

        int eyeX = px;
        int eyeY = py + (int)(0.25f * View.TILE_YRADIUS);
        int eyeW = (int)(0.5f * View.TILE_XRADIUS);
        int eyeH = (int)(0.5f * View.TILE_YRADIUS);

//        if (game.isPlayerWatchingRight()) {
//            eyeX += (int)(0.25 * View.TILE_XRADIUS);
//        } else {
//            eyeX -= (int)(0.25 * View.TILE_XRADIUS);
//        }

        drawer.drawRect(eyeX, eyeY, eyeW, eyeH, View.EYE_COLOR);
    }

    public void addEventHandler(EventHandler handler) {
        if (! handlers.contains(handler)) {
            handlers.add(handler);
        }
    }

    public void removeEventHandler(EventHandler handler) {
        handlers.remove(handler);
    }

    public void updateGame() {
        game.update();
    }
}
