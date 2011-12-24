package com.github.pmcompany.pustomario;

import com.github.pmcompany.pustomario.core.ConcreteGame;
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

    private String name;
    private NetworkClient clientNetwork;

    public Client() {
        // Create components
        game = new ConcreteGame();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void connectServer() {
        if (clientNetwork == null) {
            clientNetwork = new ConcreteNetworkClient(this, Network.HOST, Network.PORT);
        }

        if(! clientNetwork.isConnected()) {
            if (name == null) {
                enterName();
            }

            clientNetwork.connectServer();

//            lwjgl.addEventHandler(clientNetwork.getNetworkSender());
            clientNetwork.getNetworkReceiver().addEventHandler(game);
        }
    }

//    public void spectateGame() {
////        clientNetwork.spectateGame();
//    }
//
//    public void joinGame() {
//        clientNetwork.joinGame(game.getPlayerX(), game.getPlayerY());
//    }

    public void disconnectServer() {
        if (clientNetwork != null && clientNetwork.isConnected()) {
            clientNetwork.disconnectServer();

            lwjgl.removeEventHandler(clientNetwork.getNetworkSender());
            clientNetwork.getNetworkReceiver().removeEventHandler(game);
        }
    }

    private void enterName() {
        String newName = JOptionPane.showInputDialog("Enter player's name");
        if (! newName.isEmpty()) {
            name = newName;
        }
    }
}
