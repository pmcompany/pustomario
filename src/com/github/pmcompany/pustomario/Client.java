package com.github.pmcompany.pustomario;

import com.github.pmcompany.pustomario.core.ConcreteGame;
import com.github.pmcompany.pustomario.core.GameManager;
import com.github.pmcompany.pustomario.io.LWJGLComplex;
import com.github.pmcompany.pustomario.io.OutputHandler;
import com.github.pmcompany.pustomario.io.OutputServer;

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
        game = new ConcreteGame();
        lwjgl = new LWJGLComplex(this, game, SCREEN_WIDTH, SCREEN_HEIGHT);
//        preprocessor = new Preprocessor();

        // Configure components
        lwjgl.setTitle(GAME_TITLE);

        // Link components
        lwjgl.addInputHandler(preprocessor);
        lwjgl.addEventHandler(game);
//        preprocessor.addEventHandler(game);

        // Managed states initial value
        turnOff = false;
    }

    public void run() {
        game.initGame();
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
