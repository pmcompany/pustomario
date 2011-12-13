package com.github.pmcompany.pustomario.net;

import com.github.pmcompany.pustomario.core.Event;
import com.github.pmcompany.pustomario.core.EventHandler;
import com.github.pmcompany.pustomario.core.EventServer;

/**
 * @author dector (dector9@gmail.com)
 */
public interface NetworkClient extends EventServer, EventHandler {
    public void start();
    public void connect();
    public void spectate();
    public void disconnect();
    public boolean isConnected();
}
