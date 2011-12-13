package com.github.pmcompany.pustomario.net;

/**
 * @author dector (dector9@gmail.com)
 */
public enum PackageType {
    // Client requests
    CONNECT, SPECTATE, JOIN, DISCONNECT, GAME_EVENT,

    // Server responses
    CONNECTED,

    // Default response
    OK;

    public int getValue() {
        return ordinal();
    }
}
