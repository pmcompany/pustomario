package com.github.pmcompany.pustomario.net.client;

import com.github.pmcompany.pustomario.net.Connection;
import com.github.pmcompany.pustomario.net.NetworkSender;

import java.io.OutputStream;

/**
 * @author dector (dector9@gmail.com)
 */
public class ClientNetworkSender extends NetworkSender {
    public ClientNetworkSender(Connection connection, OutputStream out) {
        super(connection, out);
    }
}
