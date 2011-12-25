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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleEvent(Event e) {
        System.out.printf("NetworkServer handling event : %s%n", e.toString());

        switch (e.getType()) {
            case ADD_NEW_PLAYER: {
                String name = e.getStringValue();

                if (! controller.hasClient(name)) {
                    accepted = true;
                    controller.addNewClient(name, this);

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

            } break;

            case JOIN_NEW_PLAYER: {
                String[] playerInfo = e.getStringValue().split(" ");
                int x = Integer.parseInt(playerInfo[1]);
                int y = Integer.parseInt(playerInfo[2]);

                System.out.println("Joining bew player " + playerInfo[0]);

                controller.joinClient(playerInfo[0], x, y, this);

                try {
                    sender.send(new NetworkPackage(PackageType.JOINED, null));

                    receiver.removeEventHandler(this);
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

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public void setJoined(boolean joined) {
        this.joined = joined;
    }

    public String getUserName() {
        return controller.getClientName(this);
    }
}
