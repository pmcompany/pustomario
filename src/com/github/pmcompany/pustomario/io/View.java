package com.github.pmcompany.pustomario.io;

/**
 * @author dector (dector9@gmail.com)
 */
public class View {
    public static final int SCREEN_WIDTH = 640;
    public static final int SCREEN_HEIGHT = 480;

    public static final int TILE_WIDTH = 16;
    public static final int TILE_HEIGHT = 16;

//    public static final int TILE_WIDTH = 32;
//    public static final int TILE_HEIGHT = 32;

    public static final PColor WALL_COLOR = PColor.BLACK;
    public static final PColor HERO_COLOR = PColor.RED;
    public static final PColor ALIENT_COLOR = PColor.BLUE;
    public static final PColor EYE_COLOR = PColor.BLACK;
    public static final PColor BACK_COLOR = new PColor(0.3f, 0.3f, 0.3f, 0.3f);

    private int screenStartX;
    private int screenStartY;

    public View() {
        screenStartX = 0;
        screenStartY = 0;
    }

    public int getScreenStartX() {
        return screenStartX;
    }

    public void setScreenStartX(int screenStartX) {
        this.screenStartX = screenStartX;
    }

    public int getScreenStartY() {
        return screenStartY;
    }

    public void setScreenStartY(int screenStartY) {
        this.screenStartY = screenStartY;
    }
}
