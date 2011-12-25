package com.github.pmcompany.pustomario.net.client;

import com.github.pmcompany.pustomario.core.Event;
import com.github.pmcompany.pustomario.core.EventHandler;
import com.github.pmcompany.pustomario.core.EventServer;
import com.github.pmcompany.pustomario.net.Connection;
import com.github.pmcompany.pustomario.net.NetworkReceiver;
import com.github.pmcompany.pustomario.net.NetworkSender;

/**
 * @author dector (dector9@gmail.com)
 */
public interface NetworkClient extends Connection {
    public void connectServer();
    public void joinGame(String name, int x, int y);
    public void spectateGame(String name);
    public void disconnectServer();
    public NetworkReceiver getNetworkReceiver();
    public NetworkSender getNetworkSender();
}
