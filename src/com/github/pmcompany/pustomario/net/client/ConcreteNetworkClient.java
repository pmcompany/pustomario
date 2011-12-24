package com.github.pmcompany.pustomario.net.client;

import com.github.pmcompany.pustomario.core.*;
import com.github.pmcompany.pustomario.net.*;
import com.github.pmcompany.pustomario.net.client.ClientNetworkReceiver;
import com.github.pmcompany.pustomario.net.client.ClientNetworkSender;
import com.github.pmcompany.pustomario.net.client.NetworkClient;

import java.io.*;

/**
 * @author dector (dector9@gmail.com)
 */
public class ConcreteNetworkClient implements NetworkClient {
    private GameManager gmanager;

    private NetworkImpl network;
    private String host;
    private int port;

    private NetworkReceiver receiver;
    private NetworkSender sender;

    private boolean connected;
    private boolean joined;

    public ConcreteNetworkClient(GameManager gmanager, String host, int port) {
        this.gmanager = gmanager;
        this.host = host;
        this.port = port;
    }

    public void connectServer() {
        System.out.printf("Connecting with %s:%d ...%n", host, port);

        try {
            network = new NetworkImpl(host, port);

            sender = new ClientNetworkSender(this, network.getOutputStream());
            sender.start();

            System.out.printf("Connected to %s:%d%n", host, port);
            connected = true;

            receiver = new ClientNetworkReceiver(this, network.getInputStream());
            receiver.start();
        } catch (IOException e) {
            System.out.printf("Can't connectServer to %s:%d%n", host, port);
            e.printStackTrace();
        }

        try {
            sender.send(new NetworkPackage(PackageType.CONNECT, gmanager.getName()));
        } catch (IOException e) {
            System.out.println("Sending name failed");
            e.printStackTrace();
        }
    }

    public void disconnectServer() {
        if (isConnected()) {
            try {
                network.close();

                connected = false;
                System.out.printf("Disconnected from %s:%d%n", host, port);
            } catch (IOException e) {
                System.out.println("I/O Disconnect error");
                e.printStackTrace();
            }
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isJoined() {
        return joined;
    }

    public NetworkSender getNetworkSender() {
        return sender;
    }

    public NetworkReceiver getNetworkReceiver() {
        return receiver;
    }
}
