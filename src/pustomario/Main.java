package pustomario;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.Display;

import java.io.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author dector
 */
public class Main {
    public static final int SCREEN_WIDTH = 640;
    public static final int SCREEN_HEIGHT = 480;

    public static final int TILES_X = 40;
    public static final int TILES_Y = 30;

    public static final int TILE_WIDTH = SCREEN_WIDTH / TILES_X;
    public static final int TILE_HEIGHT = SCREEN_HEIGHT / TILES_Y;

    public static final float SPEED_SCALE = 1;
    public static final float GRAVITY_ACCELERATION=-0.2f;
    public static final float JUMP_SPEED = 5f;
    public static final float RUN_SPEED = 0.8f;
    public static final float ONE_ON_FRICTION = 0.8f;

    public static final int HORISONTAL_SCROLL_BORDER = SCREEN_WIDTH / 3;
    public static final int VERTICAL_SCROLL_BORDER = SCREEN_HEIGHT / 3;

    private int levelWidth;
    private int levelHeight;
    private char[][] level;

    private float playerXf;
    private float playerYf;

    private boolean goRight = true;

//    private boolean[] canMove;
//    private boolean isInJump;

//    private float prevYf;

    private float speedX;
    private float speedY;

    private int startScreenX;
    private int startScreenY;

    public static void main(String[] args) {
        Main main = new Main();
        main.start();
    }

    public void start() {
        initGraphics();
        initGL();
        initGame();

        while (!Display.isCloseRequested()) {
            resetGame();
            readInput();
            updateGame();
            updateGraphics();

            Display.update();
			Display.sync(60);
        }

        Display.destroy();
    }

    private void resetGame() {
//        speedX = 0;
//        speedY = 0;
    }

    private void initGraphics() {
        try {
            Display.setDisplayMode(new DisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT));
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }

        Display.setTitle("Pustomario");

    }

    private void initGL() {
        glClearColor(0.3f, 0.3f, 0.3f, 0f);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, SCREEN_WIDTH, SCREEN_HEIGHT, 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);
    }

    private void initGame() {
        loadLevel();

//        canMove = new boolean[8];
    }

    private void loadLevel() {
        try {
            BufferedReader reader;
            reader = new BufferedReader(new FileReader("level.pml"));
            String[] levelParams = reader.readLine().split(" ");
            levelWidth = Integer.parseInt(levelParams[0]);
            levelHeight = Integer.parseInt(levelParams[1]);
            level = new char[levelWidth][levelHeight];

            char currentChar;
            for (int j = levelHeight - 1; j >= 0; j--) {
                String levelLine = reader.readLine();
                for (int i = 0; i < levelWidth; i++) {
                    char tile = levelLine.charAt(i);
                    if (tile == '#') {
                        level[i][j] = levelLine.charAt(i);
                    } else if (tile == '@') {
                        playerXf = TILE_WIDTH * i;
                        playerYf = TILE_HEIGHT * j;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readInput() {
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            speedX += RUN_SPEED;
            goRight = true;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            speedX -= RUN_SPEED;
            goRight = false;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            jump();
//            speedY = 1;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            speedY = -1;
        }

//        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
//            jump(true);
//        } else {
//            jump(false);
//        }
//        while(Keyboard.next()) {
//            if (Keyboard.getEventKeyState()) {
//                if (Keyboard.getEventKey() == Keyboard.KEY_UP) {
//                    jump();
//                }
//            }
//        }
//        else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
//            moveHero(5);
//        }
    }

    private void updateGame() {
        if (canMove(5)){
            speedY += GRAVITY_ACCELERATION;
        }

        speedX *= ONE_ON_FRICTION;

        moveHero();
        checkHeroPosition();
    }

    private void checkHeroPosition() {
        if (startScreenX + SCREEN_WIDTH - HORISONTAL_SCROLL_BORDER < playerXf
                && startScreenX + SCREEN_WIDTH < tileXToRelative(levelWidth)) {
            startScreenX += speedX;
        } else if (playerXf < startScreenX + HORISONTAL_SCROLL_BORDER
                && 0 < startScreenX) {
            startScreenX += speedX;
        }

        if (SCREEN_HEIGHT + startScreenY - playerYf < VERTICAL_SCROLL_BORDER) {
            startScreenY += speedY;
        }
    }

    private boolean canMove(int direction) {
//
//        int leftSpace = countLeftSpace((int)playerXf);
//        int rightSpace = countRightSpace((int)playerXf);
//        int topSpace = countTopSpace((int)playerYf);
//        int bottomSpace = countBottomSpace((int)playerYf);
//
//        if (level[playerTileX][playerTileY] == 0 && level[playerTileX][playerTileYn] == 0
//                && level[playerTileXn][playerTileY] == 0 && level[playerTileXn][playerTileYn] == 0) {
//            switch (direction) {
//                case 1: canMove = (topSpace > 0 || level[playerTileX][playerTileY+1] == 0 && level[playerTileXn][playerTileY+1] == 0); break;
//                case 3: canMove = (rightSpace > 0 || level[playerTileX+1][playerTileY] == 0 && level[playerTileX+1][playerTileYn] == 0); break;
//                case 5: canMove = (bottomSpace > 0 || level[playerTileX][playerTileY-1] == 0 && level[playerTileXn][playerTileY-1] == 0); break;
////                case 6: canMove = (bottomSpace > 0 && rightSpace > 0 || level[playerTileX-1][playerTileY] == 0
////                        && level[playerTileX][playerTileY-1] == 0); break;
//                case 7: canMove = (leftSpace > 0 || level[playerTileX-1][playerTileY] == 0 && level[playerTileX-1][playerTileYn] == 0); break;
//            }
//        } else {
//            switch (direction) {
//                case 1: playerYf -= TILE_HEIGHT - topSpace;
//                case 3: playerXf -= TILE_WIDTH - rightSpace;
//                case 5: playerYf += TILE_HEIGHT - bottomSpace;
//                case 7: playerXf += TILE_WIDTH - leftSpace;
//            }
//        }

        return getDirectionSpace(direction) > 0;
    }

    private int getDirectionSpace(int direction) {
        int playerTileX = relativeXToTile((int)playerXf);
        int playerTileXn = relativeXToTile((int)playerXf, true);
        int playerTileY = relativeYToTile((int)playerYf);
        int playerTileYn = relativeYToTile((int)playerYf, true);

        switch (direction) {
            case 1: return getTopSpace(playerTileX, playerTileY, playerTileXn, playerTileYn);
            case 3: return getRightSpace(playerTileX, playerTileY, playerTileXn, playerTileYn);
            case 5: return getBottomSpace(playerTileX, playerTileY, playerTileXn, playerTileYn);
            case 7: return getLeftSpace(playerTileX, playerTileY, playerTileXn, playerTileYn);
            default: return 0;
        }
    }

    private int getRightSpace(int x0, int y0, int x1, int y1) {
        int rightSpace = countRightSpace((int)playerXf);

        boolean end = false;
        while (! end) {
            x1++;
            if (level[x1][y0] != 0 || level[x1][y1] != 0) {
                end = true;
            } else {
                rightSpace += TILE_WIDTH;
            }
        }

        return rightSpace;
    }

    private int getLeftSpace(int x0, int y0, int x1, int y1) {
        int leftSpace = countLeftSpace((int) playerXf);

        boolean end = false;
        while (! end) {
            x0--;
            if (level[x0][y0] != 0 || level[x0][y1] != 0) {
                end = true;
            } else {
                leftSpace += TILE_WIDTH;
            }
        }

        return leftSpace;
    }

    private int getTopSpace(int x0, int y0, int x1, int y1) {
        int topSpace = countTopSpace((int) playerYf);

        boolean end = false;
        while (! end) {
            y1++;
            if (level[x0][y1] != 0 || level[x1][y1] != 0) {
                end = true;
            } else {
                topSpace += TILE_HEIGHT;
            }
        }

        return topSpace;
    }

    private int getBottomSpace(int x0, int y0, int x1, int y1) {
        int bottomSpace = countBottomSpace((int)playerYf);

        boolean end = false;
        while (! end) {
            y0--;
            if (level[x0][y0] != 0 || level[x1][y0] != 0) {
                end = true;
            } else {
                bottomSpace += TILE_HEIGHT;
            }
        }

        return bottomSpace;
    }

    private int tileXToRelative(int x) {
        return x * TILE_WIDTH;
    }

    private int tileYToRelative(int y) {
        return y * TILE_HEIGHT;
    }

    private int relativeXToTile(int x) {
        return relativeXToTile(x, false);
    }

    private int relativeXToTile(int x, boolean ceil) {
        if (ceil) {
            return (x+TILE_WIDTH-1)/TILE_WIDTH;
        } else {
            return x/TILE_WIDTH;
        }
    }

    private int relativeYToTile(int y) {
        return relativeYToTile(y, false);
    }

    private int relativeYToTile(int y, boolean ceil) {
        if (ceil) {
            return (y+TILE_HEIGHT-1)/TILE_HEIGHT;
        } else {
            return y/TILE_HEIGHT;
        }
    }

    private int countLeftSpace(int x) {
        return x % TILE_WIDTH;
    }

    private int countRightSpace(int x) {
        return (x+TILE_WIDTH) % TILE_WIDTH;
    }

    private int countTopSpace(int y) {
            return (y+TILE_HEIGHT) % TILE_HEIGHT;
    }

    private int countBottomSpace(int y) {
            return (y) % TILE_HEIGHT;
    }

    private void updateGraphics() {
        glClear(GL_COLOR_BUFFER_BIT);

        int startTileX = relativeXToTile(startScreenX);
        int endTileX = Math.min(startTileX + HORISONTAL_SCROLL_BORDER, levelWidth);
        int startTileY = relativeYToTile(startScreenY);
        int endTileY = Math.min(startTileY + VERTICAL_SCROLL_BORDER, levelHeight);

        char tile;
        for (int x = startTileX; x < endTileX; x++) {
            for (int y = startTileY; y < endTileY; y++) {
                tile = level[x][y];
                if (tile != 0) {
                    if (tile == '#') {
//                        drawWall(x - startTileX, y - startTileY);
                        drawWall(tileXToRelative(x) - startScreenX, tileYToRelative(y) - startScreenY);
                    }
                }
            }
        }

        drawHero((int)(playerXf - startScreenX), (int)(playerYf - startScreenY));
    }

    public void drawWall(int x, int y) {
        glColor3f(0f, 0f, 0f);

        int tileX = TILE_WIDTH * relativeXToTile(x);
        int tileY = TILE_HEIGHT * relativeYToTile(y);

        int x0 = x % TILE_WIDTH;
        int y0 = y % TILE_HEIGHT;

        glBegin(GL_QUADS);
        glVertex2i(x0 + tileX, y0 + SCREEN_HEIGHT - tileY);
        glVertex2i(x0 + tileX, y0 + SCREEN_HEIGHT - tileY - TILE_HEIGHT);
        glVertex2i(x0 + tileX + TILE_WIDTH, y0 + SCREEN_HEIGHT - tileY - TILE_HEIGHT);
        glVertex2i(x0 + tileX + TILE_WIDTH, y0 + SCREEN_HEIGHT - tileY);
        glEnd();
    }

    public void drawHero(int x, int y) {
        glColor3f(1.0f, 0f, 0f);
        glBegin(GL_QUADS);
        glVertex2i(x, SCREEN_HEIGHT - y);
        glVertex2i(x, SCREEN_HEIGHT - (y + TILE_HEIGHT));
        glVertex2i(x + TILE_WIDTH, SCREEN_HEIGHT - (y + TILE_HEIGHT));
        glVertex2i(x + TILE_WIDTH, SCREEN_HEIGHT - y);
        glEnd();

        int eyeX;
        int eyeY = y + (int)((float)5/8 * TILE_HEIGHT);
        int eyeW = (int)((float)1/4 * TILE_WIDTH);
        int eyeH = (int)((float)1/4 * TILE_HEIGHT);
        if (goRight) {
            eyeX = x + (int)((float)5/8 * TILE_WIDTH);
        } else {
            eyeX = x + (int)((float)1/8 * TILE_WIDTH);
        }

        glColor3f(0f, 0f, 0f);
        glBegin(GL_QUADS);
        glVertex2i(eyeX, SCREEN_HEIGHT - eyeY);
        glVertex2i(eyeX, SCREEN_HEIGHT - (eyeY + eyeH));
        glVertex2i(eyeX + eyeW, SCREEN_HEIGHT - (eyeY + eyeH));
        glVertex2i(eyeX + eyeW, SCREEN_HEIGHT - eyeY);
        glEnd();

//        System.out.println("Player: " + x + " " + y);
//        System.out.println("Eye: " + eyeX + " " + eyeY + " " + eyeW + " " + eyeH);
    }

    public void moveHero() {
        if (speedX > 0 && canMove(3)) {
            playerXf += Math.min(speedX*SPEED_SCALE, getDirectionSpace(3));
        } else if (speedX < 0 && canMove(7)) {
            playerXf -= Math.min(Math.abs(speedX*SPEED_SCALE), getDirectionSpace(7));
        } else {
            speedX = 0;
        }

        if (speedY > 0 && canMove(1)) {
            playerYf += Math.min(speedY*SPEED_SCALE, getDirectionSpace(1));
        } else if (speedY < 0 && canMove(5)) {
            playerYf -= Math.min(Math.abs(speedY*SPEED_SCALE), getDirectionSpace(5));
        } else {
            speedY = 0;
        }
    }

    public void jump() {
        boolean canPushDown = ! canMove(5);
        if (canPushDown) {
            speedY += JUMP_SPEED;
        }
    }
}
