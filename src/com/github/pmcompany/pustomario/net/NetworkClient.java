package com.github.pmcompany.pustomario.net;

import com.github.pmcompany.pustomario.core.Event;
import com.github.pmcompany.pustomario.core.EventHandler;
import com.github.pmcompany.pustomario.core.EventServer;

/**
 * @author dector (dector9@gmail.com)
 */
public interface NetworkClient extends EventServer, EventHandler {
    public void start();
    public void conectServer();
    public void joinGame(int x, int y);
    public void spectateGame();
    public void disconnectServer();
    public boolean isConnected();
}
