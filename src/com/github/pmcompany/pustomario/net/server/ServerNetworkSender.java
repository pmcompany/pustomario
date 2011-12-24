package com.github.pmcompany.pustomario.net.server;

import com.github.pmcompany.pustomario.net.Connection;
import com.github.pmcompany.pustomario.net.NetworkSender;

import java.io.OutputStream;

/**
 * @author dector (dector9@gmail.com)
 */
public class ServerNetworkSender extends NetworkSender {
    public ServerNetworkSender(Connection connection, OutputStream out) {
        super(connection, out);
    }
}
