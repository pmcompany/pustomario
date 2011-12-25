package com.github.pmcompany.pustomario;

import com.github.pmcompany.pustomario.core.ConcreteGame;
import com.github.pmcompany.pustomario.net.*;
import com.github.pmcompany.pustomario.net.server.ConcreteNetworkServer;
import com.github.pmcompany.pustomario.net.server.NetClientsController;
import com.github.pmcompany.pustomario.net.server.NetworkServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author dector (dector9@gmail.com)
 */
public class Server implements Runnable, NetClientsController {
    private ConcreteGame game;

    private ServerSocket masterSocket;

    private boolean turnOff;

    private String name;
    private Map<String, NetworkServer> clientMap;

    public Server() {
        // Create components
        game = new ConcreteGame(null);
        clientMap = new LinkedHashMap<String, NetworkServer>();

        try {
            masterSocket = new ServerSocket(Network.PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Managed states initial value
        turnOff = false;
    }

    public void run() {
        game.initGame();

        Socket s;
        while (! turnOff) {
            try {
                s = masterSocket.accept();

                NetworkImpl network = new NetworkImpl(s);
                NetworkServer networkServer
                        = new ConcreteNetworkServer(network, this);
                networkServer.startListen();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        halt();
    }

    public void turnOffGame() {
        turnOff = true;
    }

    private void halt() {
        disconnectAll();

        System.out.println("Turning off...");
    }

    private void disconnectAll() {

    }

    public void addNewClient(String name, NetworkServer newNetworkServer) {
        NetworkReceiver nr = newNetworkServer.getNetworkReceiver();
        NetworkSender ns = newNetworkServer.getNetworkSender();

        for (NetworkServer oldNetworkServer : clientMap.values()) {
            nr.addEventHandler(oldNetworkServer.getNetworkSender());
            oldNetworkServer.getNetworkReceiver().addEventHandler(ns);
        }

        clientMap.put(name, newNetworkServer);
    }

    public void joinClient(String name, int x, int y, NetworkServer newNetworkServer) {
        game.addPlayer(name, x, y);
    }

    public void spectateClient(String name, NetworkServer networkServer) {
        NetworkReceiver nr = networkServer.getNetworkReceiver();
        NetworkSender ns = networkServer.getNetworkSender();

        for (NetworkServer oldNetworkServer : clientMap.values()) {
            oldNetworkServer.getNetworkReceiver().removeEventHandler(ns);
        }

        nr.addEventHandler((ConcreteNetworkServer)networkServer);
    }

    public void removeClient(String name) {
        if (hasClient(name)) {
            NetworkServer nsToRemove = clientMap.get(name);

            NetworkReceiver nr = nsToRemove.getNetworkReceiver();
            NetworkSender ns = nsToRemove.getNetworkSender();

            for (NetworkServer oldNetworkServer : clientMap.values()) {
                nr.removeEventHandler(oldNetworkServer.getNetworkSender());
                oldNetworkServer.getNetworkReceiver().removeEventHandler(ns);
            }

        }
    }

    public boolean hasClient(String name) {
        return clientMap.containsKey(name);
    }

    public String getClientName(NetworkServer server) {
        String name = null;

        for (String clientName : clientMap.keySet()) {
            if (clientMap.get(clientName).equals(server)) {
                name = clientName;
                break;
            }
        }

        return name;
    }

    public static void main(String[] args) {
        Server s = new Server();
        s.run();
    }
}
