package com.github.pmcompany.pustomario.net;

import com.github.pmcompany.pustomario.core.Event;
import com.github.pmcompany.pustomario.core.EventHandler;
import com.github.pmcompany.pustomario.core.EventServer;

/**
 * @author dector (dector9@gmail.com)
 */
public class Network implements EventHandler, EventServer {
    public static String HOST = "localhost";
    public static int PORT = 1447;

    private EventHandler remoteHandler; // Server
    private boolean output;


    public Network(boolean output) {
        this.output = output;
    }

    public void handleEvent(Event e) {
        remoteHandler.handleEvent(e);
    }

    public void addEventHandler(EventHandler handler) {
        remoteHandler = handler;
    }

    public void removeEventHandler(EventHandler handler) {
        remoteHandler = null;
    }
}
