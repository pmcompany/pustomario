package com.github.pmcompany.pustomario.net;

import com.github.pmcompany.pustomario.core.EventHandler;
import com.github.pmcompany.pustomario.core.EventServer;

/**
 * @author dector (dector9@gmail.com)
 */
public interface NetworkServer extends EventServer, EventHandler {
    public void start();
    public void disconnect();
    public boolean isConnected();
}
