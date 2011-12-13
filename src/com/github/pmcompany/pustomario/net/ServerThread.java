package com.github.pmcompany.pustomario.net;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author dector (dector9@gmail.com)
 */
public class ServerThread extends Thread {
    private Socket s;
    private String name;

    public ServerThread(Socket s) {
        this.s = s;
    }

    public void run() {
        try {
            System.out.printf("Connected with %s%n", s.getLocalAddress().getHostName());

            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()), true);

            String str;
            NetworkPackage p;
            NetworkPackage response;
            boolean done = false;
            while (! done) {
                str = in.readLine();
                if (str != null) {
                    p = new NetworkPackage(str);

                    switch (p.getType()) {
                        case CONNECT: {
                            name = p.getValue();
                            response = new NetworkPackage(PackageType.CONNECTED, null);
                        } break;
                        default: {
                            response = NetworkPackage.defaultPackage();
                        }
                    }
                    out.println(response);

                    System.out.println("[" + name + "] " + p);
                    System.out.println("[" + name + "] " + " response " + response);
                }
            }

            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
