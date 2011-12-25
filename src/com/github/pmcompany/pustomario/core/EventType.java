package com.github.pmcompany.pustomario.core;

/**
 * @author dector (dector9@gmail.com)
 */
public enum EventType {
    ACCELERATE_X_PLAYER, ACCELERATE_Y_PLAYER,

    RUN_LEFT, RUN_RIGHT, JUMP,

    MOVE_PLAYER,

    // Service events
    BORN, ADD_NEW_PLAYER, JOIN_NEW_PLAYER, SPECTATE_PLAYER,
    SET_PLAYER_X, SET_PLAYER_Y
}
