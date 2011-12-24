package com.github.pmcompany.pustomario.net;

import com.github.pmcompany.pustomario.core.*;

import java.io.*;
import java.net.Socket;

/**
 * @author dector (dector9@gmail.com)
 */
public class ConcreteNetworkServer extends Thread implements NetworkServer {
    private GameManager gmanager;
    private EventHandler handler;

    private String host;
    private int port;

    private Socket s;
    private BufferedReader in;
    private PrintWriter out;
    private boolean connected;
    private boolean joined;

    public ConcreteNetworkServer(GameManager gmanager, String host, int port) {
        this.gmanager = gmanager;
        this.host = host;
        this.port = port;
    }

    public void addEventHandler(EventHandler handler) {
        this.handler = handler;
    }

    public void removeEventHandler(EventHandler handler) {
        this.handler = null;
    }

    public void handleEvent(Event e) {
        if (isConnected()) {
            try {
                send(new NetworkPackage(PackageType.GAME_EVENT, e));
            } catch (IOException e1) {
                System.out.println("Network error. Check connection!");
                e1.printStackTrace();
            }
        }
    }

    public void run() {}

    private NetworkPackage send(NetworkPackage p) throws IOException {
        NetworkPackage result = null;

        String ans;
        if (isConnected()) {
            out.println(p);
            if ((ans = in.readLine()) != null) {
                result = new NetworkPackage(ans);
            }
        } else {
            throw new NotConnectedException(String.format("Not connected to %s:%d", host, port));
        }

        System.out.printf("Package %s sended%n", p);
        return result;
    }

    public void conectServer() {
        System.out.printf("Connecting with %s:%d ...%n", host, port);

        try {
            s = new Socket(host, port);

            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()), true);
        } catch (IOException e) {
            System.out.printf("Can't connectServer to %s:%d%n", host, port);
            e.printStackTrace();
        }

        System.out.printf("Connected to %s:%d%n", host, port);
        connected = true;

        try {
            NetworkPackage result =
                    send(new NetworkPackage(PackageType.CONNECT, gmanager.getName()));

            if (result.getType() == PackageType.CONNECTED) {
                System.out.println("Connected OK");
            }
        } catch (IOException e) {
            System.out.println("Sending name failed");
            e.printStackTrace();
        }
    }

    public void joinGame(int x, int y) {
        if (isConnected()) {
            try {
                System.out.println("Sending join game request");

                NetworkPackage answ = send(new NetworkPackage(PackageType.JOIN,
                        String.format("%d %d", x, y)));
                if (answ.getType() == PackageType.JOINED) {
                    joined = true;
                    System.out.println("Player joined");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void spectateGame() {
//        if (isConnected()) {
//            try {
//                NetworkPackage answ;
//
//                System.out.println("Sending spectateGame request");
//
//                answ = send(NetworkPackage.spectatePackage());
//                if (answ.getType() == PackageType.SPECTATED) {
//                    String[] playerPos = answ.getValue().split(" ");
//
//                    System.out.printf("Player pos: %s%n", answ.getValue());
//
//                    handler.handleEvent(new GameEvent(EventType.SET_PLAYER_X,
//                            Integer.parseInt(playerPos[0])));
//                    handler.handleEvent(new GameEvent(EventType.SET_PLAYER_Y,
//                            Integer.parseInt(playerPos[1])));
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

    public void disconnectServer() {
        if (isConnected()) {
            try {
                s.close();
                in.close();
                out.close();

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

    public void disconnect() {

    }
}
