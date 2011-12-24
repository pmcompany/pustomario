package com.github.pmcompany.pustomario.net;

import com.github.pmcompany.pustomario.core.Event;
import com.github.pmcompany.pustomario.core.EventHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * @author dector (dector9@gmail.com)
 */
public abstract class NetworkSender extends Thread implements EventHandler {
    private PrintWriter out;
    private Connection connection;

    public NetworkSender(Connection connection, OutputStream out) {
        this.out = new PrintWriter(new OutputStreamWriter(out), true);
        this.connection = connection;
    }

    public void handleEvent(Event e) {
        if (connection.isConnected()) {
            try {
                send(new NetworkPackage(PackageType.GAME_EVENT, e));
            } catch (IOException e1) {
                System.out.println("Network error. Check connection!");
                e1.printStackTrace();
            }
        }
    }

    public void send(NetworkPackage p) throws IOException {
        if (connection.isConnected()) {
            out.println(p);
            System.out.printf("Package %s sended%n", p);
        } else {
            throw new NotConnectedException(String.format("Not connected to server"));
        }
    }
}
