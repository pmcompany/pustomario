package com.github.pmcompany.pustomario.net;

import com.github.pmcompany.pustomario.core.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author dector (dector9@gmail.com)
 */
public abstract class NetworkReceiver extends Thread implements EventServer {
    private BufferedReader in;
    private Connection connection;

    private List<EventHandler> handlers;

    public NetworkReceiver(Connection connection, InputStream in) {
        this.in = new BufferedReader(new InputStreamReader(in));
        this.connection = connection;

        handlers = Collections.synchronizedList(new LinkedList<EventHandler>());
    }

    public void run() {
        try {
            String inputLine;
            NetworkPackage p;

            while (connection.isConnected()) {
                inputLine = in.readLine();

                if (inputLine != null) {
                    p = new NetworkPackage(inputLine);

                    System.out.printf("Package received: %s%n", p.toString());

                    processPackage(p);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void processPackage(NetworkPackage p);

    public void addEventHandler(EventHandler handler) {
        handlers.add(handler);
    }

    public void removeEventHandler(EventHandler handler) {
        handlers.remove(handler);
    }

    public List<EventHandler> getEventHandlers() {
        return handlers;
    }
}
