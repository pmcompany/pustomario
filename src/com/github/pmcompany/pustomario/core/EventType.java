package com.github.pmcompany.pustomario.core;

/**
 * @author dector (dector9@gmail.com)
 */
public enum EventType {
    ACCELERATE_X_PLAYER, ACCELERATE_Y_PLAYER,

    RUN_LEFT, RUN_RIGHT, JUMP, SHOOT,

    MOVE_PLAYER, INCREMENT_HP, KILLED, REBORN,

    // Service events
    ADD_NEW_PLAYER, JOIN_NEW_PLAYER, SPECTATE_PLAYER
}
