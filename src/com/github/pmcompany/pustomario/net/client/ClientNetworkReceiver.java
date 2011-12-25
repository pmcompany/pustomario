package com.github.pmcompany.pustomario.net.client;

import com.github.pmcompany.pustomario.core.EventHandler;
import com.github.pmcompany.pustomario.core.EventType;
import com.github.pmcompany.pustomario.core.GameEvent;
import com.github.pmcompany.pustomario.net.Connection;
import com.github.pmcompany.pustomario.net.NetworkPackage;
import com.github.pmcompany.pustomario.net.NetworkReceiver;

import javax.swing.*;
import java.io.InputStream;

/**
 * @author dector (dector9@gmail.com)
 */
public class ClientNetworkReceiver extends NetworkReceiver {
    public ClientNetworkReceiver(Connection connection, InputStream in) {
        super(connection, in);
    }

    @Override
    public void processPackage(NetworkPackage p) {
        System.out.printf("Processing package from server: %s%n", p.toString());

        switch (p.getType()) {
            case GAME_EVENT: {
                for (EventHandler handler : getEventHandlers()) {
                    GameEvent e =
                            new GameEvent(EventType.valueOf(p.getValue()), null);
                    handler.handleEvent(e);
                }
            } break;

            case CONNECTED: {
                System.out.println("CONNECTED!");
            } break;

            case NAME_EXISTS: {
                JOptionPane.showMessageDialog(null, "Such name exists on server",
                        "Change name", JOptionPane.ERROR_MESSAGE);
            } break;

            case JOINED: {
                System.out.println("Joined to game");
                connection.setJoined(true);
            } break;
        }
    }
}