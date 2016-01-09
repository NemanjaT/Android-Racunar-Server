package sockets;

import models.FileSystem;
import sockets.dependencies.ConnectionListener;
import models.Poruka;

import java.io.File;
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
    private FileSystem fileSystem;

    private ArrayList<ConnectionListener> listeners;

    public Connection(Socket connection) {
        this.id = ID++;
        this.works = false;
        this.connection = connection;
        this.listeners = new ArrayList<>();
        this.fileSystem = new FileSystem();
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
            handleResponse(new Poruka("GETDIRS", "", ""));
            Poruka msg = new Poruka();
            do {
                try {
                    msg = (Poruka)input.readObject();
                    log("+File recieved: " + msg.toString());
                    handleResponse(msg);
                } catch (ClassNotFoundException cnf) {
                    log("~Unknown class recieved from client!");
                }
            } while(!msg.getKomanda().equals("SHUTDOWN"));
            sendMsg(new Poruka("", "SHUTDOWN", ""));
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

    private void handleResponse(Poruka poruka) {
        String upperKomanda = "";
        if(poruka.getKomanda() != null)
            upperKomanda = poruka.getKomanda().toUpperCase();
        if(poruka.getFajl().equals("") && upperKomanda.equals("DIR")) {
            handleResponse(new Poruka("GETDIRS", "", ""));
            return;
        }
        switch(upperKomanda) {
            case "SHUTDOWN":
                return;
            case "DIR":
                ArrayList<Poruka> filesAndDirectories = fileSystem.getFilesAndDirectories(poruka.getFajl());
                sendMsg(new Poruka("", "START", ""));
                sendMsg(new Poruka("", "..", ""));
                filesAndDirectories.forEach(this::sendMsg);
                sendMsg(new Poruka("", "END", ""));
                break;
            case "GETDIRS":
                ArrayList<String> dirs = fileSystem.getDrives();
                sendMsg(new Poruka("", "START", ""));
                for(String dir : dirs)
                    sendMsg(new Poruka("", dir, ""));
                sendMsg(new Poruka("", "END", ""));
                break;
            case "RUN":
                fileSystem.openFileLocally(poruka.getFajl());
                break;
            case "DELETE":
                if(fileSystem.deleteFileLocally(poruka.getFajl())) {
                    String[] putanjaDelovi = poruka.getFajl().split("\\\\");
                    String temp = "";
                    for(int i = 0; i < putanjaDelovi.length - 1; i++) {
                        temp += putanjaDelovi[i] + "\\";
                    }
                    handleResponse(new Poruka("DIR", temp, ""));
                } else {
                    sendMsg(new Poruka("", "NE", ""));
                }
                break;
            case "TAKE":
                File file = new File(poruka.getFajl());
                if(!file.isFile())
                    sendMsg(new Poruka("", "NE", ""));
                String[] ekstenzija = poruka.getFajl().split("\\\\");
                sendMsg(new Poruka("GIVE", String.valueOf((int)file.length()), ekstenzija[ekstenzija.length - 1]));
                fileSystem.sendFileToClient(file, output);
                if(fileSystem.sendFileToClient(file, output)) {
                    System.out.println("uspesno slanje podataka");
                } else {
                    System.out.println("zajebali smo slanje podataka");
                }
                break;
            default:
                sendMsg(new Poruka("", "START", ""));
                sendMsg(new Poruka("", "~*~ ~*~ ~*~ ~*~ ~*~", ""));
                sendMsg(new Poruka("", "Komanda ne postoji!", ""));
                sendMsg(new Poruka("", "~*~ ~*~ ~*~ ~*~ ~*~", ""));
                sendMsg(new Poruka("", "END", ""));
                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                        handleResponse(new Poruka("GETDIRS", "", ""));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
        }
    }
}
