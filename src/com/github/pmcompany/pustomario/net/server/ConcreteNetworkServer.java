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
    private boolean spectated;

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
                String name = e.getSender();

                if (! controller.hasClient(name)) {
                    accepted = true;
                    controller.addNewClient(name, this);

                    try {
                        sender.send(new NetworkPackage(PackageType.CONNECTED, name, null));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    try {
                        sender.send(new NetworkPackage(PackageType.NAME_EXISTS, name, null));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }

            } break;

            case JOIN_NEW_PLAYER: {
                String[] playerInfo = e.getStringValue().split(" ");
                int x = Integer.parseInt(playerInfo[0]);
                int y = Integer.parseInt(playerInfo[1]);

                System.out.println("Joining new player " + e.getSender());

                controller.joinClient(e.getSender(), x, y, this);

                try {
                    sender.send(new NetworkPackage(PackageType.JOINED, e.getSender(), null));

//                    receiver.removeEventHandler(this);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } break;

            case SPECTATE_PLAYER: {
                String name = e.getSender();

                System.out.println("Spectating player " + name);

                controller.spectateClient(name, this);

                try {
                    sender.send(new NetworkPackage(PackageType.SPECTATED, e.getSender(), null));

//                    receiver.removeEventHandler(this);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } break;
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

    public void setSpectated(boolean spectated) {
        this.spectated = spectated;
    }

    public boolean isSpectated() {
        return spectated;
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
