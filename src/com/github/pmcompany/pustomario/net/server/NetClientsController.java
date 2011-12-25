package com.github.pmcompany.pustomario.net.server;

import com.github.pmcompany.pustomario.net.server.NetworkServer;

/**
 * @author dector (dector9@gmail.com)
 */
public interface NetClientsController {
    public void addNewClient(String name, NetworkServer networkServer);
    public void joinClient(String name, int x, int y, NetworkServer networkServer);
    public void removeClient(String name);
    public boolean hasClient(String name);
    public String getClientName(NetworkServer server);
}
