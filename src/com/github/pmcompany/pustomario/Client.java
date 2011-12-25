package com.github.pmcompany.pustomario;

import com.github.pmcompany.pustomario.core.ConcreteGame;
import com.github.pmcompany.pustomario.core.EventHandler;
import com.github.pmcompany.pustomario.core.EventServer;
import com.github.pmcompany.pustomario.core.GameManager;
import com.github.pmcompany.pustomario.io.LWJGLComplex;
import com.github.pmcompany.pustomario.io.OutputHandler;
import com.github.pmcompany.pustomario.io.OutputServer;
import com.github.pmcompany.pustomario.io.View;
import com.github.pmcompany.pustomario.net.*;
import com.github.pmcompany.pustomario.net.client.ConcreteNetworkClient;
import com.github.pmcompany.pustomario.net.client.NetworkClient;

import javax.swing.*;

/**
 * @author dector (dector9@gmail.com)
 */
public class Client implements Runnable, GameManager, OutputServer {
    public static final String GAME_TITLE = "Pustomario";

    private LWJGLComplex lwjgl;
    private ConcreteGame game;

    private boolean debug;
    private boolean turnOff;

    private NetworkClient clientNetwork;

    public Client() {
        // Create components
        game = new ConcreteGame(enterName());
        lwjgl = new LWJGLComplex(this, game, View.SCREEN_WIDTH, View.SCREEN_HEIGHT);

        // Configure components
        lwjgl.setTitle(GAME_TITLE);

        // Link components
        lwjgl.addEventHandler(game);

        // Managed states initial value
        turnOff = false;
    }

    public void run() {
        game.initGame();
        while (! (turnOff || lwjgl.isCloseRequested())) {
            lwjgl.checkInput();
            game.preUpdate();
            lwjgl.handleOutput();
            game.postUpdate();
        }

        halt();
    }

    public void turnOffGame() {
        turnOff = true;
    }

    private void halt() {
        lwjgl.turnOff();

        disconnectServer();

        System.out.println("Turning off...");
    }

    public void addOutputHandler(OutputHandler handler) {
        // Implements formally
    }

    public void removeOutputHandler(OutputHandler handler) {
        // Implements formally
    }

    public void switchDebugMode() {
        debug = !debug;
    }

    public boolean isDebugMode() {
        return debug;
    }

    public void connectServer() {
        if (clientNetwork == null) {
            clientNetwork = new ConcreteNetworkClient(this, Network.HOST, Network.PORT);
        }

        if(! clientNetwork.isConnected()) {
            if (game.getPlayerName() == null) {
                changeName();
            }

            clientNetwork.connectServer();

//            lwjgl.addEventHandler(clientNetwork.getNetworkSender());
            clientNetwork.getNetworkReceiver().addEventHandler(game);
        }
    }

    public void spectateGame() {
        if (clientNetwork != null && clientNetwork.isConnected()) {
            clientNetwork.spectateGame(game.getPlayerName());
        }
    }

    public void joinGame() {
        if (clientNetwork != null && clientNetwork.isConnected()) {
            clientNetwork.joinGame(game.getPlayerName(), game.getPlayerX(), game.getPlayerY());
        }
    }

    public void disconnectServer() {
        if (clientNetwork != null && clientNetwork.isConnected()) {
            clientNetwork.disconnectServer();

            lwjgl.removeEventHandler(clientNetwork.getNetworkSender());
            clientNetwork.getNetworkReceiver().removeEventHandler(game);
        }
    }

    private String enterName() {
        return JOptionPane.showInputDialog("Enter player's name");
    }

    public String getName() {
        return game.getPlayerName();
    }

    public void changeName() {
        String name = enterName();
        if (! name.isEmpty()) {
            game.setPlayerName(name);
        }
    }

    public EventServer getMainEventServer() {
        return lwjgl;
    }

    public EventHandler getMainEventHandler() {
        return game;
    }

    public void addNewPlayer(String name, int x, int y) {
        game.addPlayer(name, x, y);
    }
}
