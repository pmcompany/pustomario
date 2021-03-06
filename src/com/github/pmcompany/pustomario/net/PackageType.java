package com.github.pmcompany.pustomario.net;

/**
 * @author dector (dector9@gmail.com)
 */
public enum PackageType {
    // Client requests
    CONNECT, SPECTATE, JOIN, DISCONNECT, GAME_EVENT,

    // Server responses
    CONNECTED, NAME_EXISTS, SPECTATED, JOINED,

    // Default response
    REJECTED, OK;

    public int getValue() {
        return ordinal();
    }
}
