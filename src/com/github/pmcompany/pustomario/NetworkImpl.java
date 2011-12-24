package com.github.pmcompany.pustomario;

import com.github.pmcompany.pustomario.net.Network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author dector (dector9@gmail.com)
 */
public class NetworkImpl {
    private Socket socket;

    public NetworkImpl(String host, int port) throws IOException {
        this(new Socket(host, port));
    }

    public NetworkImpl(Socket socket) {
        this.socket = socket;
    }

    public OutputStream getOutputStream() throws IOException {
        return socket.getOutputStream();
    }

    public InputStream getInputStream() throws IOException {
        return socket.getInputStream();
    }

    public void close() throws IOException {
        getInputStream().close();
        getOutputStream().close();
        socket.close();
    }
}
