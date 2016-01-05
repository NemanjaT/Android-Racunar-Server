package sockets;

import sockets.dependencies.ConnectionListener;
import models.Poruka;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Connection implements Runnable {
    private static int ID = 0;

    private final int id;
    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean works;

    private ArrayList<ConnectionListener> listeners;

    public Connection(Socket connection) {
        this.id = ID++;
        this.works = false;
        this.connection = connection;
        this.listeners = new ArrayList<>();
    }

    public Connection(Socket connection, ConnectionListener listener) {
        this(connection);
        addListener(listener);
    }

    public boolean works() {
        return this.works;
    }

    @Override
    public String toString() {
        return "Connection[" + id + "]";
    }

    public void sendMsg(Poruka msg) {
        try {
            output.writeObject(msg);
            output.flush();
            log("+File sent: " + msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        works = true;
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            log("~Setting up I/O!");
            /**
             * TEMP START
             */
            sendMsg(new Poruka("", "START", ""));
            sendMsg(new Poruka("", "tekst1", ""));
            sendMsg(new Poruka("", "tekst2", ""));
            sendMsg(new Poruka("", "tekst3", ""));
            sendMsg(new Poruka("", "tekst4", ""));
            sendMsg(new Poruka("", "END", ""));
            sendMsg(new Poruka("", "SHUTDOWN", ""));
            /**
             * TEMP END
             */
            Poruka msg = new Poruka();
            do {
                try {
                    msg = (Poruka)input.readObject();
                    log(msg);
                } catch (ClassNotFoundException cnf) {
                    log("~Unknown class recieved from client!");
                }
            } while(!msg.getFajl().equals("BLA"));

        } catch (Exception eof) {
            log("~Connection with the client shut down.");
        } finally {
            close();
        }
    }

    public void close() {
        try {
            if(connection == null || connection.isClosed())
                return;
            connection.close();
            input.close();
            output.close();
            input = null;
            output = null;
            connection = null;
            log("~Connection lost(I/O shut down).");
            works = false;
            listeners.forEach(ConnectionListener::connectionClosedEvent);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public void addListener(ConnectionListener listener) {
        listeners.add(listener);
    }

    public void setListeners(ArrayList<ConnectionListener> listeners) {
        this.listeners = listeners;
    }

    private void log(Object poruka) {
        System.out.println(this + ": " + poruka);
        for(ConnectionListener cl : listeners)
            cl.connectionLogEvent(this, poruka.toString());
    }
}
