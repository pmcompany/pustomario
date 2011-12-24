package com.github.pmcompany.pustomario.net.server;

import com.github.pmcompany.pustomario.Server;
import com.github.pmcompany.pustomario.core.EventHandler;
import com.github.pmcompany.pustomario.core.EventServer;
import com.github.pmcompany.pustomario.net.NetworkReceiver;
import com.github.pmcompany.pustomario.net.NetworkSender;

/**
 * @author dector (dector9@gmail.com)
 */
public interface NetworkServer {
    public void startListen();
    public void disconnect();
    public NetworkReceiver getNetworkReceiver();
    public NetworkSender getNetworkSender();
}
