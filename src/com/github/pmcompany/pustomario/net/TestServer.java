package com.github.pmcompany.pustomario.net;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author dector (dector9@gmail.com)
 */
public class TestServer implements Runnable {
    private ServerSocket ss;
    
    public void run() {
        try {
            ss = new ServerSocket(Network.PORT);

            System.out.printf("Waiting for connections%n");

            Socket s = ss.accept();

            System.out.printf("Connected with %s%n", s.getLocalAddress().getHostName());

            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()), true);

            String str;
            boolean done = false;
            while (! done) {
                str = in.readLine();
                if (str != null) {
                    System.out.println(str);
                }
            }

            ss.close();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        TestServer s = new TestServer();
        s.run();
    }
}
