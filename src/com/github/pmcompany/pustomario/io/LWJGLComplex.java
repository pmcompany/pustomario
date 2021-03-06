package com.github.pmcompany.pustomario.io;

import com.github.pmcompany.pustomario.core.*;
import com.github.pmcompany.pustomario.net.Network;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.TrueTypeFont;

import java.security.Key;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author dector (dector9@gmail.com)
 */
public class LWJGLComplex implements EventServer, InputServer, OutputHandler, GameManagerUser, EventHandler {
    private static final int SHOOT_TIME = 200;
    private static final int DRAW_SHOOT_TIME = 100;

    private List<EventHandler> handlers;
    private GameManager gmanager;

    private GLDrawer drawer;
    private DataProvider game;

    private View view;

    private int mouseX;
    private int mouseY;

    private long prevTime;
    private long delta;

    private boolean shooted;
    private long lastShootTime;
    private List<Point[]> shoot;
    private List<Long> shootTime;

    private boolean showStats;
    private boolean showHelp;

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

        Mouse.setGrabbed(true);

        drawer = new GLDrawer(screenWidth, screenHeight);
        drawer.setClearColor(View.BACK_COLOR);
        view = new View();
        this.game = game;

        handlers = new LinkedList<EventHandler>();

        // DEBUG BEGIN
        java.awt.Font font = new java.awt.Font(java.awt.Font.SANS_SERIF, java.awt.Font.PLAIN, 17);
        textFont = new TrueTypeFont(font, false);
        // DEBUG END

        shoot = new LinkedList<Point[]>();
        shootTime = new LinkedList<Long>();
        updateTime();
    }

    private void updateTime() {
        long currTime = System.currentTimeMillis();
        delta = currTime - prevTime;
        prevTime = currTime;
    }

    public void addInputHandler(InputHandler handler) {}

    public void removeInputHandler(InputHandler handler) {}

    public void checkInput() {
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)
                || Keyboard.isKeyDown(Keyboard.KEY_D)) {
            sendEvent(new GameEvent(EventType.RUN_RIGHT, gmanager.getName(), null));
        } else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)
                || Keyboard.isKeyDown(Keyboard.KEY_A)) {
            sendEvent(new GameEvent(EventType.RUN_LEFT, gmanager.getName(), null));
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_UP)
                || Keyboard.isKeyDown(Keyboard.KEY_SPACE)
                || Keyboard.isKeyDown(Keyboard.KEY_W)) {
            sendEvent(new GameEvent(EventType.JUMP, gmanager.getName(), null));
        }

        if (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                switch (Keyboard.getEventKey()) {
                    case Keyboard.KEY_F1: {
                        showHelp = !showHelp;
                    }; break;
                    case Keyboard.KEY_F2: gmanager.switchDebugMode(); break;
                    case Keyboard.KEY_F3: gmanager.changeName(); break;
                    case Keyboard.KEY_F4: Network.inputServerURL(); break;
                    case Keyboard.KEY_F5: gmanager.connectServer(); break;
                    case Keyboard.KEY_F6: gmanager.joinGame(); break;
                    case Keyboard.KEY_F8: gmanager.spectateGame(); break;
                    case Keyboard.KEY_F9: gmanager.disconnectServer(); break;
                }
            }
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            gmanager.turnOffGame();
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_TAB)) {
            if (! showStats) {
                showStats = true;
            }
        } else {
            if (showStats) {
                showStats = false;
            }
        }

        mouseX = Mouse.getX();
        mouseY = Mouse.getY();

//        System.out.printf("Mouse clicked %d:%d%n", mouseX, mouseY);
        if (Mouse.isButtonDown(0)) {
            if (canShoot()) {
                lastShootTime = System.currentTimeMillis();
                shooted = true;

                Point playerPos = game.getPlayerPosition();
                playerPos.setX(playerPos.getX() + View.TILE_WIDTH/2);
                playerPos.setY(playerPos.getY() + View.TILE_HEIGHT/2);

                int sx = (mouseX - View.SCREEN_WIDTH/2) + playerPos.getX();
                int sy = (mouseY - View.SCREEN_HEIGHT/2) + playerPos.getY();

                sendEvent(new GameEvent(EventType.SHOOT, gmanager.getName(),
                        String.format("%d %d", sx, sy)));

                Point endP = game.countShoot(playerPos, new Point(sx, sy));
                shoot.add(new Point[] {playerPos, endP});
                shootTime.add(lastShootTime);
            }
        } else if (shooted) {
            shooted = false;
        }
    }

    private void drawHelp() {
        int statsWidth = 400;
        int statsHeight = 300;

        int statsX = (View.SCREEN_WIDTH - statsWidth) / 2;
        int statsY = (View.SCREEN_HEIGHT - statsHeight) / 2;

        int padding = 10;

        drawer.fillRect(statsX, statsY,
                statsWidth, statsHeight, PColor.BLACK);

        String[] lines = new String[] {"A - move left", "D - move right", "W - jump",
                "<Left mouse button> - shoot", "F1 - show this help",
                "F2 - Switch debug mode", "F3 - Change name", "F4 - Enter server URL",
                "F5 - Connect to server", "F6 - Join to server",
                "F9 - Disconnect from server", "<TAB> - Show stats"};
        for (int i = 1; i <= lines.length; i++) {
            drawer.drawString(statsX + padding, statsY + i*2*padding, lines[i-1], textFont, Color.white);

        }
    }

    private boolean canShoot() {
        return (prevTime - lastShootTime > SHOOT_TIME) && ! shooted;
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

        if (showStats) {
            drawStats();
        }

        if (showHelp) {
            drawHelp();
        }

        drawMouse();

        updateTime();

        Display.update();
        Display.sync(60);
    }

    private void drawStats() {
        int statsWidth = 400;
        int statsHeight = 300;

        int statsX = (View.SCREEN_WIDTH - statsWidth) / 2;
        int statsY = (View.SCREEN_HEIGHT - statsHeight) / 2;

        int padding = 10;

        drawer.fillRect(statsX, statsY,
                statsWidth, statsHeight, PColor.BLACK);

        drawer.drawString(statsX + padding, statsY + padding,
                String.format("Server: %s:%d", gmanager.getServerName(), gmanager.getServerPort()), textFont, Color.white);

        Iterator<Player> iter = game.getPlayersIterator();
        int i = 2;
        Player p;
        String playerStr;
        while (iter.hasNext()) {
            p = iter.next();

            playerStr = String.format("Player %d: `%s` (%d)", i-1, p.getName(), game.getScore(p));

            drawer.drawString(statsX + padding, statsY + i*2*padding, playerStr, textFont, Color.white);
            if (game.getWinnerName().equals(p.getName())) {
                drawer.drawString(statsX + padding + textFont.getWidth(playerStr),
                        statsY + i*2*padding, "*", textFont, Color.yellow);
            }

            i++;
        }
    }

    private void drawMouse() {
        drawer.drawLine(mouseX - View.MOUSE_LENGTH, mouseY - View.MOUSE_LENGTH,
                mouseX + View.MOUSE_LENGTH, mouseY + View.MOUSE_LENGTH, View.MOUSE_COLOR);
        drawer.drawLine(mouseX - View.MOUSE_LENGTH, mouseY + View.MOUSE_LENGTH,
                mouseX + View.MOUSE_LENGTH, mouseY - View.MOUSE_LENGTH, View.MOUSE_COLOR);

        int px = game.getPlayerX();
        int py = game.getPlayerY();

        int lScreen = px - View.SCREEN_WIDTH/2;
        int rScreen = lScreen + View.SCREEN_WIDTH;
        int dScreen = py - View.SCREEN_HEIGHT/2;
        int uScreen = dScreen + View.SCREEN_HEIGHT;

        int x1;
        int y1;
        int x2;
        int y2;

        Iterator<Point[]> iter = shoot.iterator();
        Iterator<Long> timeIter = shootTime.iterator();
        Point[] shoots;
        long time;
        while (iter.hasNext() && timeIter.hasNext()) {
            shoots = iter.next();
            time = timeIter.next();

            x1 = shoots[0].getX();
            y1 = shoots[0].getY();
            x2 = shoots[1].getX();
            y2 = shoots[1].getY();

            if (lScreen < x1 && x1 < rScreen
                    || dScreen < y1 && y1 < uScreen
                    || lScreen < x2 && x2 < rScreen
                    || dScreen < y2 && y2 < uScreen) {
//                System.out.printf(" %d:%d --- %d:%d%n", x1, y1, x2, y2);

                drawer.drawLine(View.SCREEN_WIDTH/2 - (px - x1),
                        View.SCREEN_HEIGHT/2 - (py - y1),
                        View.SCREEN_WIDTH/2 - (px - x2),
                        View.SCREEN_HEIGHT/2 - (py - y2),
                        View.BEAM_COLOR);
            }

            if (prevTime - time > DRAW_SHOOT_TIME) {
                iter.remove();
                timeIter.remove();
            }
        }
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

        int px = game.getPlayerX();
        int py = game.getPlayerY();

        Point stTileP = game.countTileByAbs(px - View.SCREEN_WIDTH/2, py - View.SCREEN_HEIGHT/2);
        if (stTileP.getX() <= 0) {
            stTileP.setX(1);
            view.setScreenStartX(View.SCREEN_WIDTH/2 - px);
        } else {
            view.setScreenStartX(-px + View.SCREEN_WIDTH/2 + game.countAbsByTile(stTileP.getX(), stTileP.getY()).getX() - 1);
        }
        if (stTileP.getY() <= 0) {
            stTileP.setY(1);
            view.setScreenStartY(View.SCREEN_HEIGHT/2 - py);
        } else {
            view.setScreenStartY(-py + View.SCREEN_HEIGHT/2 + game.countAbsByTile(stTileP.getX(), stTileP.getY()).getY() - 1);
        }

        Point endTileP = game.countTileByAbs(px + View.SCREEN_WIDTH/2, py + View.SCREEN_HEIGHT/2);
        if (endTileP.getX() > game.getMapWidth()) {
            endTileP.setX(game.getMapWidth());
        }
        if (endTileP.getY() > game.getMapHeight()) {
            endTileP.setY(game.getMapHeight());
        }

        int tile;
        for (int ix = stTileP.getX(); ix <= endTileP.getX(); ix++) {
            for (int iy = stTileP.getY(); iy <= endTileP.getY(); iy++) {
                tile = game.getTileAt(ix, iy);

                if (tile != 0) {
                    if (tile == '#') {
                        drawer.fillRect(view.getScreenStartX() + (ix - stTileP.getX()) * View.TILE_WIDTH,
                                view.getScreenStartY() + (iy - stTileP.getY()) * View.TILE_HEIGHT,
                                View.TILE_WIDTH, View.TILE_HEIGHT, View.WALL_COLOR);
                    }
                }
            }
        }
    }

    private void drawPlayer() {
        int realPx = View.SCREEN_WIDTH/2 - 1;
        int realPy = View.SCREEN_HEIGHT/2 - 1;

        int px = game.getPlayerX();
        int py = game.getPlayerY();

        Iterator<Player> players = game.getPlayersIterator();
        Player p;
        while (players.hasNext()) {
            p = players.next();

            if (! p.getName().equals(gmanager.getName())) {
                int playerX = p.getX();
                int playerY = p.getY();

                int leftScrX = px - View.SCREEN_WIDTH/2;
                int leftScrY = py - View.SCREEN_HEIGHT/2;
                int rightScrX = leftScrX + View.SCREEN_WIDTH;
                int rightScrY = leftScrY + View.SCREEN_HEIGHT;

                if (leftScrX <= playerX && playerX <= rightScrX
                        && leftScrY <= playerY && playerY <= rightScrY) {
                    playerX = realPx - (px - playerX);
                    playerY = realPy - (py - playerY);

                    drawer.fillRect(playerX, playerY,
                            View.TILE_WIDTH, View.TILE_HEIGHT, View.ALIENT_COLOR);

                    int eyeX = playerX;
                    int eyeY = playerY + (int)(0.65f * View.TILE_HEIGHT);
                    int eyeW = (int)(0.2f * View.TILE_WIDTH);
                    int eyeH = (int)(0.2f * View.TILE_HEIGHT);

                    if (p.isWatchingRight()) {
                        eyeX += (int)(0.70f * View.TILE_WIDTH);
                    } else {
                        eyeX += (int)(0.25f * View.TILE_WIDTH);
                    }

                    drawer.fillRect(eyeX-1, eyeY-1, eyeW, eyeH, View.EYE_COLOR);

                    drawer.drawString(playerX - View.TILE_WIDTH/2,
                            View.SCREEN_HEIGHT - playerY - 2*View.TILE_HEIGHT, p.getName(), textFont, Color.black);
                }
            }
        }

//        drawer.fillRect(view.getScreenStartX() + px-1, view.getScreenStartY() + py-1,
//                View.TILE_WIDTH, View.TILE_HEIGHT, View.HERO_COLOR);
        drawer.fillRect(realPx, realPy,
                View.TILE_WIDTH, View.TILE_HEIGHT, View.HERO_COLOR);

        drawer.drawString(realPx - View.TILE_WIDTH/2, realPy - 2*View.TILE_HEIGHT, game.getPlayerName(), textFont, Color.black);

        int eyeX = realPx;
        int eyeY = realPy + (int)(0.65f * View.TILE_HEIGHT);
        int eyeW = (int)(0.2f * View.TILE_WIDTH);
        int eyeH = (int)(0.2f * View.TILE_HEIGHT);

        if (View.SCREEN_WIDTH/2 + View.TILE_WIDTH/2 <= mouseX) {
            eyeX += (int)(0.70f * View.TILE_WIDTH);
        } else {
            eyeX += (int)(0.25f * View.TILE_WIDTH);
        }

        // DEBUG BEGIN
        if (gmanager.isDebugMode()) {

            Point tileP;
            Point absTileStartP;

            int pRelativeX = realPx - px;
            int pRelativeY = realPy - py;

            List<Point> crossedTiles = game.getPlayerCrossedTiles(game.getPlayer());
            for (int i = 0; i < crossedTiles.size(); i++) {
                tileP = crossedTiles.get(i);
                absTileStartP = game.countAbsByTile(tileP.getX(), tileP.getY());

//                drawer.drawString(10, 30 + i * 20, String.format("%d rect: %d:%d", i + 1, absTileStartP.getX(), absTileStartP.getY()), textFont, Color.red);
                drawer.drawRect(absTileStartP.getX() + pRelativeX,
                        absTileStartP.getY() + pRelativeY,
                        View.TILE_WIDTH, View.TILE_HEIGHT-1, PColor.BLUE);
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

            List<Point> neighTiles = game.getPlayerNeighbourTiles(game.getPlayer(), direction);
            for (Point neighTile : neighTiles) {
                absTileStartP = game.countAbsByTile(neighTile.getX(), neighTile.getY());

                if (game.isTileBlocked(neighTile.getX(), neighTile.getY())) {
                    drawer.drawRect(absTileStartP.getX() + pRelativeX,
                            absTileStartP.getY() + pRelativeY,
                            View.TILE_WIDTH, View.TILE_HEIGHT-1, PColor.RED);
                } else {
                    drawer.drawRect(absTileStartP.getX() + pRelativeX,
                            absTileStartP.getY() + pRelativeY,
                            View.TILE_WIDTH, View.TILE_HEIGHT-1, PColor.GREEN);
                }
            }

            drawer.drawString(10, 10, String.format("Player pos: %d:%d", px, py), textFont, Color.red);
            drawer.drawString(10, 30, String.format("Vx: %.3f\t\t Vy:%.3f", speedX, speedY), textFont, Color.red);
            drawer.drawString(10, 50, String.format("Start screen: %d:%d",
                    view.getScreenStartX(), view.getScreenStartY()), textFont, Color.red);
            drawer.drawString(10, 70, String.format("Players num %d", game.getPlayersNum()), textFont, Color.red);

        }
        // DEBUG END

        drawer.fillRect(eyeX-1, eyeY-1, eyeW, eyeH, View.EYE_COLOR);

        drawer.drawString(10, View.SCREEN_HEIGHT - 30,
                String.format("HP %d", game.getPlayer().getHp()), textFont, Color.red);
        String winnerString = String.format("Winner %s (%d)", game.getWinnerName(),
                game.getWinnerScore());
        drawer.drawString((View.SCREEN_WIDTH - textFont.getWidth(winnerString)) / 2, 10,
                winnerString, textFont, Color.red);
    }

    public void addEventHandler(EventHandler handler) {
        if (! handlers.contains(handler)) {
            handlers.add(handler);
        }
    }

    public void removeEventHandler(EventHandler handler) {
        handlers.remove(handler);
    }

    public void handleEvent(Event e) {
        switch (e.getType()) {
            case SHOOT: {
                String[] pos = e.getStringValue().split(" ");

                int sx = Integer.parseInt(pos[0]);
                int sy = Integer.parseInt(pos[1]);

                int x = game.getPlayerX(e.getSender());
                int y = game.getPlayerY(e.getSender());

                Point playerPoint = new Point(x, y);

                Point resultPoint = game.countShoot(playerPoint, new Point(sx, sy), false);

                System.out.printf("Adding shoot: %s -- %s%n", playerPoint, resultPoint);

                shoot.add(new Point[] {playerPoint, resultPoint});
                shootTime.add(lastShootTime);
            } break;
        }
    }
}
