package com.github.pmcompany.pustomario.io;

/**
 * @author dector (dector9@gmail.com)
 */

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import pustomario.PColor;

import static org.lwjgl.opengl.GL11.*;

public class GLDrawer {
    private int screenWidth;
    private int screenHeight;
    private PColor clearColor;

    public GLDrawer(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        clearColor = PColor.BLACK;

        initGL();

        Mouse.setGrabbed(true);
    }

    public void initGL() {
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glClearColor(clearColor.getRed(), clearColor.getGreen(), clearColor.getBlue(), clearColor.getAlpha());

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

    public void drawString(int x, int y, String s, Font font, Color color) {
        glEnable(GL_BLEND);

        font.drawString(x, y, s, color);

        glDisable(GL_BLEND);
    }

    public void drawLine(int x0, int y0, int x1, int y1, PColor color) {
        glColor3f(color.getRed(), color.getGreen(), color.getBlue());

        glBegin(GL_LINES);
        glVertex2i(x0, screenHeight - y0);
        glVertex2i(x1, screenHeight - y1);
        glEnd();
    }

    public void update() {
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public void setClearColor(PColor clearColor) {
        this.clearColor = clearColor;
    }
}

