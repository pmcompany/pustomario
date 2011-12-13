package com.github.pmcompany.pustomario.net;

import com.github.pmcompany.pustomario.core.Event;
import com.github.pmcompany.pustomario.core.EventHandler;
import com.github.pmcompany.pustomario.core.EventServer;
import com.github.pmcompany.pustomario.core.GameManager;

import java.io.*;
import java.net.Socket;

/**
 * @author dector (dector9@gmail.com)
 */
public class ConcreteNetworkClient extends Thread implements NetworkClient {
    private GameManager gmanager;
    private EventHandler handler;

    private String host;
    private int port;

    private Socket s;
    BufferedReader in;
    PrintWriter out;
    private boolean connected;

    public ConcreteNetworkClient(GameManager gmanager, String host, int port) {
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

    public void connect() {
        System.out.printf("Connecting with %s:%d ...%n", host, port);

        try {
            s = new Socket(host, port);

            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()), true);

            connected = true;
        } catch (IOException e) {
            System.out.printf("Can't connect to %s:%d%n", host, port);
            e.printStackTrace();
        }

        System.out.printf("Connected to %s:%d%n", host, port);

        try {
            NetworkPackage result =
                    send(new NetworkPackage(PackageType.CONNECT, gmanager.getName()));

            if (result.getType() == PackageType.CONNECTED) {
                System.out.println("Connected OK");
            }
        } catch (IOException e) {
            System.out.println("Sending name failed");
        }
    }

    public void disconnect() {
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
}
