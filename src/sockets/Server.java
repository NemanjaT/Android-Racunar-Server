package sockets;

import sockets.dependencies.ConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static final int PORT             = 8080;
    private static final int CONNECTION_COUNT = 4;

    private ServerSocket server;
    private Connection[] connections;

    private ArrayList<ConnectionListener> listeners;
    private volatile boolean works;

    public Server() {
        connections = new Connection[CONNECTION_COUNT];
        listeners = new ArrayList<>();
    }

    public Server(ConnectionListener listener) {
        this();
        addListener(listener);
    }

    public void startServer() {
        try {
            if(running()) {
                log("Server already running");
                return;
            }
            server = new ServerSocket(PORT);
            log("SERVER LISTENING ON PORT: " + PORT);
            works = true;
            while (works) {
                Socket socket = server.accept();
                for(int i = 0; i < connections.length; i++) {
                    if(connections[i] == null || !connections[i].works()) {
                        connections[i] = new Connection(socket);
                        connections[i].setListeners(listeners);
                        new Thread(connections[i]).start();
                        for(ConnectionListener cl : listeners)
                            cl.connectionCreatedEvent(connections[i]);
                        break;
                    }
                }
            }
        } catch (IOException io) {
            server = null;
            works = false;
            log("Socket terminated - shutting down. Reason: " + io.getMessage());
        }
    }

    public void closeServer() {
        if(server == null || server.isClosed())
            return;
        try {
            server.close();
            log("SERVER CLOSED");
            works = false;
            listeners.forEach(ConnectionListener::serverOff);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean running() {
        return server != null && !server.isClosed();
    }

    public void addListener(ConnectionListener listener) {
        listeners.add(listener);
    }

    private void log(String log) {
        System.out.println(log);
        for(ConnectionListener cl : listeners)
            cl.serverLogEvent(log);
    }
}
