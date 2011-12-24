package com.github.pmcompany.pustomario;

import com.github.pmcompany.pustomario.core.*;
import com.github.pmcompany.pustomario.net.NetworkPackage;
import com.github.pmcompany.pustomario.net.PackageType;

import java.io.*;
import java.net.Socket;

/**
 * @author dector (dector9@gmail.com)
 */
public class ServerThread extends Thread {
    private Socket s;
    private String name;
    private DataProvider game;
    private TestServer server;
    private boolean joined;

    public ServerThread(Socket s, TestServer server, DataProvider game) {
        this.s = s;
        this.game = game;
        this.server = server;
    }

    public void run() {
        try {
            name = s.getLocalAddress().getHostName();
            System.out.printf("Connected with %s%n", name);

            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()), true);

            String str;
            NetworkPackage p;
            NetworkPackage response;
            boolean done = false;
            while (! done) {
                str = in.readLine();
                if (str != null) {
                    p = new NetworkPackage(str);

                    switch (p.getType()) {
                        case CONNECT: {
                            name = p.getValue();
                            response = new NetworkPackage(PackageType.CONNECTED, null);
                        } break;

                        case JOIN: {
                            String[] playerPos = p.getValue().split(" ");

                            String px = playerPos[0];
                            String py = playerPos[1];

                            response = new NetworkPackage(PackageType.JOINED, null);

                            joined = true;

                            server.handleEvent(new GameEvent(EventType.SET_PLAYER_X, Integer.parseInt(px)));
                            server.handleEvent(new GameEvent(EventType.SET_PLAYER_Y, Integer.parseInt(py)));
                        } break;

                        case GAME_EVENT: {
                            if (joined) {
                                System.out.println("Trying to handle " + p + " from " + name);
                                response = NetworkPackage.DEFAULT_PACKAGE;
                                server.handleEvent(new GameEvent(EventType.valueOf(p.getValue()), null));
                            } else {
                                response = NetworkPackage.REJECTED_PACKAGE;
                            }
                        } break;

                        case SPECTATE: {
                            int x = game.getPlayerX();
                            int y = game.getPlayerY();

                            response = new NetworkPackage(PackageType.SPECTATED,
                                    String.format("%d %d", x, y));

                            joined = false;
                        } break;

                        default: {
                            response = NetworkPackage.DEFAULT_PACKAGE;
                        }
                    }
                    out.println(response);

                    System.out.println("[" + name + "] " + p);
                    System.out.println("[" + name + "] " + " response " + response);
                }
            }

            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
