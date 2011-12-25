//package com.github.pmcompany.pustomario;
//
//import com.github.pmcompany.pustomario.core.*;
//import com.github.pmcompany.pustomario.net.Network;
//
//import java.io.*;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.LinkedList;
//import java.util.List;
//
///**
// * @author dector (dector9@gmail.com)
// */
//public class TestServer implements Runnable, EventServer, EventHandler {
//    private ServerSocket ss;
//    private List<EventHandler> handlers;
//    private ConcreteGame game;
//
//    public TestServer() {
//        handlers = new LinkedList<EventHandler>();
//        game = new ConcreteGame();
//        game.initGame();
//
//        addEventHandler(game);
//    }
//
//    public void run() {
//        try {
//            ss = new ServerSocket(Network.PORT);
//
//            System.out.printf("Waiting for connections%n");
//
//            boolean done = false;
//
//            while (! done) {
//                Socket s = ss.accept();
//                new ServerThread(s, this, game).start();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void main(String[] args) {
//        TestServer s = new TestServer();
//        s.run();
//    }
//
//    public void addEventHandler(EventHandler handler) {
//        handlers.add(handler);
//    }
//
//    public void removeEventHandler(EventHandler handler) {
//        handlers.remove(handler);
//    }
//
//    public void handleEvent(Event e) {
//        for (EventHandler handler : handlers) {
//            handler.handleEvent(e);
//        }
//    }
//}
