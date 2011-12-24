package com.github.pmcompany.pustomario.net;

import com.github.pmcompany.pustomario.core.Event;
import com.github.pmcompany.pustomario.core.EventHandler;
import com.github.pmcompany.pustomario.core.EventServer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author dector (dector9@gmail.com)
 */
public class NetworkReceiver extends Thread implements EventServer {
    private BufferedReader in;
    private Connection connection;

    public NetworkReceiver(Connection connection, InputStream in) {
        this.in = new BufferedReader(new InputStreamReader(in));
        this.connection = connection;
    }

    public void addEventHandler(EventHandler handler) {

    }

    public void removeEventHandler(EventHandler handler) {

    }
}
