package com.github.pmcompany.pustomario.net.server;

import com.github.pmcompany.pustomario.core.Event;
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
                String type = subStr.substring(0, subStr.indexOf(' '));
                value = subStr.substring(subStr.indexOf(' ') + 1, subStr.length());

                GameEvent e =
                        new GameEvent(EventType.valueOf(type), p.getSender(), value);

                handleEvent(e);
            } break;

            case CONNECT: {
                GameEvent e =
                        new GameEvent(EventType.ADD_NEW_PLAYER, p.getSender(), value);

                handleEvent(e);
            } break;

            case JOIN: {
                GameEvent e =
                        new GameEvent(EventType.JOIN_NEW_PLAYER, p.getSender(), value);

                System.out.println("JOIN NEW PLAYER " + p.getSender() + " " + p.getValue());

                try {
                    handleEvent(e);
                } catch (java.util.ConcurrentModificationException e1) {
                    // Ignore
                }

            } break;

            case SPECTATE: {
                GameEvent e =
                        new GameEvent(EventType.SPECTATE_PLAYER, p.getSender(), value);

                try {
                    handleEvent(e);
                } catch (java.util.ConcurrentModificationException e1) {
                    // Ignore
                }

            } break;
        }
    }

    private void handleEvent(Event e) {
        EventHandler handler;
        synchronized (handlers) {
            for (int i = 0; i < handlers.size(); i++) {
                handler = handlers.get(i);
                handler.handleEvent(e);
            }
        }
    }
}
