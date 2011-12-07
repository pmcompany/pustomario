package com.github.pmcompany.pustomario;

import org.lwjgl.opengl.Display;

/**
 * @author dector (dector9@gmail.com)
 */
public class Client implements Runnable, GameManager, OutputServer {
    public static final String GAME_TITLE = "Pustomario";

    public static final int SCREEN_WIDTH = 640;
    public static final int SCREEN_HEIGHT = 480;

    private LWJGLComplex lwjgl;
    private Preprocessor preprocessor;
    private ConcreteGame game;

    private boolean turnOff;

    public Client() {
        // Create components
        lwjgl = new LWJGLComplex(this, SCREEN_WIDTH, SCREEN_HEIGHT);
        preprocessor = new Preprocessor();
        game = new ConcreteGame();

        // Configure components
        lwjgl.setTitle(GAME_TITLE);

        // Link components
        lwjgl.addInputHandler(preprocessor);
        preprocessor.addEventHandler(game);

        // Managed states initial value
        turnOff = false;
    }

    public void run() {
        while (! (turnOff || lwjgl.isCloseRequested())) {
            lwjgl.checkInput();
            lwjgl.handleOutput();
        }

        halt();
    }

    public void turnOffGame() {
        turnOff = true;
    }

    private void halt() {
        lwjgl.turnOff();

        System.out.println("Turning off...");
    }

    public void addOutputHandler(OutputHandler handler) {
        // Implements formally
    }

    public void removeOutputHandler(OutputHandler handler) {
        // Implements formally
    }
}
