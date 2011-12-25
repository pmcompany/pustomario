package com.github.pmcompany.pustomario.net.server;

import com.github.pmcompany.pustomario.core.EventHandler;
import com.github.pmcompany.pustomario.core.EventType;
import com.github.pmcompany.pustomario.core.GameEvent;
import com.github.pmcompany.pustomario.net.Connection;
import com.github.pmcompany.pustomario.net.NetworkPackage;
import com.github.pmcompany.pustomario.net.NetworkReceiver;

import java.io.InputStream;

/**
 * @author dector (dector9@gmail.com)
 */
public class ServerNetworkReceiver extends NetworkReceiver {
    public ServerNetworkReceiver(Connection connection, InputStream in) {
        super(connection, in);
    }

    @Override
    public void processPackage(NetworkPackage p) {
        System.out.printf("Processing package %s%n", p.toString());

        String value = p.getValue();

        switch (p.getType()) {
            case GAME_EVENT: {
                String subStr = value.substring(value.indexOf(' ') + 1, value.length());
                value = subStr.substring(0, subStr.indexOf(' '));

                GameEvent e =
                        new GameEvent(EventType.valueOf(value), p.getSender(), null);

                for (EventHandler handler : getEventHandlers()) {
                    handler.handleEvent(e);
                }
            } break;

            case CONNECT: {
                GameEvent e =
                        new GameEvent(EventType.ADD_NEW_PLAYER, p.getSender(), value);

                for (EventHandler handler : getEventHandlers()) {
                    handler.handleEvent(e);
                }
            } break;

            case JOIN: {
                GameEvent e =
                        new GameEvent(EventType.JOIN_NEW_PLAYER, p.getSender(), value);

                try {
                    for (EventHandler handler : getEventHandlers()) {
                        handler.handleEvent(e);
                    }
                } catch (java.util.ConcurrentModificationException e1) {
                    // Ignore
                }

            } break;

            case SPECTATE: {
                GameEvent e =
                        new GameEvent(EventType.SPECTATE_PLAYER, p.getSender(), value);

                try {
                    for (EventHandler handler : getEventHandlers()) {
                        handler.handleEvent(e);
                    }
                } catch (java.util.ConcurrentModificationException e1) {
                    // Ignore
                }

            } break;
        }
    }
}
