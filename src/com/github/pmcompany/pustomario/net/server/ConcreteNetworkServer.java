package com.github.pmcompany.pustomario.net.server;

import com.github.pmcompany.pustomario.core.*;
import com.github.pmcompany.pustomario.net.*;

import java.io.*;

/**
 * @author dector (dector9@gmail.com)
 */
public class ConcreteNetworkServer implements NetworkServer, Connection, EventHandler {
    private NetworkImpl network;
    private String host;
    private int port;

    private NetworkReceiver receiver;
    private NetworkSender sender;

    private boolean accepted;
    private boolean connected;
    private boolean joined;
    private NetClientsController controller;

    public ConcreteNetworkServer(NetworkImpl network, NetClientsController controller) {
        this.network = network;
        this.controller = controller;
    }

    public void startListen() {
        connected = true;

        try {
            receiver = new ServerNetworkReceiver(this, network.getInputStream());
            receiver.start();

            sender = new ServerNetworkSender(this, network.getOutputStream());
            sender.start();

            // Accept client
            receiver.addEventHandler(this);
            while (! accepted) {}
            receiver.removeEventHandler(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleEvent(Event e) {
        System.out.printf("NetworkServer handling event : %s%n", e.toString());

        if (e.getType() == EventType.ADD_NEW_PLAYER) {
            String name = e.getStringValue();

            if (! controller.hasClient(name)) {
                controller.addNewClient(name, this);
                accepted = true;

                try {
                    sender.send(new NetworkPackage(PackageType.CONNECTED, null));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } else {
                try {
                    sender.send(new NetworkPackage(PackageType.NAME_EXISTS, null));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                network.close();

                connected = false;
            } catch (IOException e) {
                System.out.println("I/O Disconnect error");
                e.printStackTrace();
            }
        }

        System.out.printf("Disconnected from %s:%d%n", host, port);
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isJoined() {
        return joined;
    }

    public NetworkReceiver getNetworkReceiver() {
        return receiver;
    }

    public NetworkSender getNetworkSender() {
        return sender;
    }
}
