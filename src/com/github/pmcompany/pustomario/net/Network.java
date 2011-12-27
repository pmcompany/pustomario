package com.github.pmcompany.pustomario.net;

import com.github.pmcompany.pustomario.core.Event;
import com.github.pmcompany.pustomario.core.EventHandler;
import com.github.pmcompany.pustomario.core.EventServer;

import javax.swing.*;

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

    public static void inputServerURL() {
        String serverStr = JOptionPane.showInputDialog("Input server URL", HOST + ":" + PORT);

        if (serverStr != null) {
            String[] val = serverStr.split(":");

            if (val.length > 1) {
                HOST = val[0];
                PORT = Integer.parseInt(val[1]);
            }
        }
    }
}
