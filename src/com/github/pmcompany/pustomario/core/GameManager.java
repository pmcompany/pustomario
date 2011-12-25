package com.github.pmcompany.pustomario.core;

/**
 * @author dector (dector9@gmail.com)
 */
public interface GameManager {
    public void turnOffGame();

    public void switchDebugMode();
    public boolean isDebugMode();

    public String getName();
    public void changeName();
    public void connectServer();
//    public void spectateGame();
    public void joinGame();
    public void disconnectServer();

    public EventServer getMainEventServer();
}
