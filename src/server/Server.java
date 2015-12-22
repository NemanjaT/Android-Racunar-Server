package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import grafika.GlavniProzor;

public class Server {
	private final int port = 8080;
	private final int brojKonekcija = 4;
	
	private ServerSocket server;
	private Konekcija[] konekcije;
	
	private ArrayList<GlavniProzor> glavniProzori;
	
	public Server() {
		konekcije = new Konekcija[brojKonekcija];
		glavniProzori = new ArrayList<GlavniProzor>();
	}
	
	public void startServer() {
		try {
			server = new ServerSocket(port);
			System.out.println("SERVER SLUSA NA PORTU: " + port);
			while (true) {
				Socket socket = server.accept();
				for(int i = 0; i < konekcije.length; i++) {
					if(konekcije[i] == null || !konekcije[i].works()) {
						konekcije[i] = new Konekcija(socket);
						new Thread(konekcije[i]).start();
						for(GlavniProzor gp : glavniProzori)
							gp.addPanel(konekcije[i]);
						break;
					}
				}
			}
		} catch (IOException io) {
			server = null;
			System.out.println("Socket prekinut - ugasen server");
		}
	}
	
	public void closeServer() {
		if(server == null || server.isClosed())
			return;
		try {
			server.close();
			System.out.println("SERVER ZATVOREN");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean serverRunning() {
		if(server == null)
			return false;
		return !server.isClosed();
	}
	
	public void addListener(GlavniProzor glavniProzor) {
		glavniProzori.add(glavniProzor);
	}
}
