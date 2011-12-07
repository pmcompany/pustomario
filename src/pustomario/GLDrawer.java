package pustomario;

import org.lwjgl.util.ReadableColor;

import static org.lwjgl.opengl.GL11.*;


/**
 * @author dector
 */
public class GLDrawer {
    private int screenWidth;
    private int screenHeight;
    private PColor clearColor;

    public GLDrawer(int screenWidth, int screenHeight, PColor clearColor) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.clearColor = clearColor;

        this.clearColor = clearColor;
        glClearColor(clearColor.getRed(), clearColor.getGreen(),
                clearColor.getBlue(), clearColor.getAlpha());

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, screenWidth, screenHeight, 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);
    }

    public void drawRect(int x, int y, int width, int height, PColor color) {
        glColor3f(color.getRed(), color.getGreen(), color.getBlue());

        glBegin(GL_QUADS);
        glVertex2i(x, screenHeight - y);
        glVertex2i(x, screenHeight - (y + height));
        glVertex2i(x + width, screenHeight - (y + height));
        glVertex2i(x + width, screenHeight - y);
        glEnd();
    }

    public void update() {
        glClear(GL_COLOR_BUFFER_BIT);
    }
}
