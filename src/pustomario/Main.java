package pustomario;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import java.io.*;

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

    private GLDrawer drawer;

    private static final PColor MOUSE_COLOR = PColor.RED;
    private static final PColor WALL_COLOR = PColor.BLACK;
    private static final PColor HERO_COLOR = PColor.RED;
    private static final PColor EYE_COLOR = PColor.BLACK;
    private static final PColor BACKGROUND_COLOR = new PColor(0.3f, 0.3f, 0.3f, 0.3f);

//    private boolean[] canMove;
//    private boolean isInJump;

//    private float prevYf;

    private float speedX;
    private float speedY;

    private int startScreenX;
    private int startScreenY;

    private int mouseX = SCREEN_WIDTH/2;
    private int mouseY = SCREEN_HEIGHT/2;

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
        drawer = new GLDrawer(SCREEN_WIDTH, SCREEN_HEIGHT, BACKGROUND_COLOR);
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

        mouseX = Mouse.getX();
        mouseY = Mouse.getY();
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
        return getDirectionSpace(direction) > 0;
    }

    private int getDirectionSpace(int direction) {
        int playerTileX = relativeXToTile((int) playerXf);
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
        int rightSpace = countRightSpace((int) playerXf);

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
        drawer.update();

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
                        drawWall(tileXToRelative(x) - startScreenX, tileYToRelative(y) - startScreenY);
                    }
                }
            }
        }

        drawHero((int) (playerXf - startScreenX), (int) (playerYf - startScreenY));

        drawer.drawString(10, 10, String.format("Mouse %d:%d", mouseX, mouseY), Color.red);
        drawer.drawString(10, 30, String.format("Player %d:%d", (int) playerXf, (int) playerYf), Color.red);
        drawer.drawString(10, 50, String.format("Spaces L%d  R%d  T%d  B%d", getDirectionSpace(7),
                getDirectionSpace(3), getDirectionSpace(1), getDirectionSpace(5)), Color.red);

        drawMouse();
    }

    private void drawMouse() {
        int length = 5;

        drawer.drawLine(mouseX - length, mouseY + length, mouseX + length, mouseY - length, MOUSE_COLOR);
        drawer.drawLine(mouseX - length, mouseY - length, mouseX + length, mouseY + length, MOUSE_COLOR);
    }

    public void drawWall(int x, int y) {
        int tileX = TILE_WIDTH * relativeXToTile(x);
        int tileY = TILE_HEIGHT * relativeYToTile(y);

        int x0 = x % TILE_WIDTH;
        int y0 = y % TILE_HEIGHT;

        drawer.drawRect(x0 + tileX, y0 + tileY, TILE_WIDTH, TILE_HEIGHT, WALL_COLOR);
    }

    public void drawHero(int x, int y) {
        drawer.drawRect(x, y, TILE_WIDTH, TILE_HEIGHT, HERO_COLOR);

        int eyeX;
        int eyeY = y + (int)((float)5/8 * TILE_HEIGHT);
        int eyeW = (int)((float)1/4 * TILE_WIDTH);
        int eyeH = (int)((float)1/4 * TILE_HEIGHT);
        if (goRight) {
            eyeX = x + (int)((float)5/8 * TILE_WIDTH);
        } else {
            eyeX = x + (int)((float)1/8 * TILE_WIDTH);
        }

        drawer.drawRect(eyeX, eyeY, eyeW, eyeH, EYE_COLOR);
    }

    public void moveHero() {
        if (speedX > 0 && canMove(3)) {
            playerXf += Math.min(speedX*SPEED_SCALE, getDirectionSpace(3));
        } else if (speedX < 0 && canMove(7)) {
            playerXf -= Math.min(Math.abs(speedX * SPEED_SCALE), getDirectionSpace(7));
        } else {
            speedX = 0;
        }

        if (speedY > 0 && canMove(1)) {
            playerYf += Math.min(speedY*SPEED_SCALE, getDirectionSpace(1));
        } else if (speedY < 0 && canMove(5)) {
            playerYf -= Math.min(Math.abs(speedY * SPEED_SCALE), getDirectionSpace(5));
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
