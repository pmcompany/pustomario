package com.github.pmcompany.pustomario.io;

import com.github.pmcompany.pustomario.core.GameManager;
import com.github.pmcompany.pustomario.core.GameManagerUser;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.util.LinkedList;
import java.util.List;

/**
 * @author dector (dector9@gmail.com)
 */
public class LWJGLComplex implements InputServer, OutputHandler, GameManagerUser {
    private List<InputHandler> handlers;
    private GameManager gmanager;

    private GLDrawer drawer;

    public LWJGLComplex(GameManager gmanager, int screenWidth, int screenHeight) {
        this.gmanager = gmanager;

        try {
            Display.setDisplayMode(new DisplayMode(screenWidth, screenHeight));
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
            gmanager.turnOffGame();
        }

        drawer = new GLDrawer(screenWidth, screenHeight);

        handlers = new LinkedList<InputHandler>();
    }

    public void addInputHandler(InputHandler handler) {
        if (! handlers.contains(handler)) {
            handlers.add(handler);
        }
    }

    public void removeInputHandler(InputHandler handler) {
        handlers.remove(handler);
    }

    public void checkInput() {
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            gmanager.turnOffGame();
        }
    }

    public void handleOutput() {
        drawer.update();

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
}
