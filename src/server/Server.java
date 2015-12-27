package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import grafika.GlavniProzor;

public class Server {
	private static final int PORT           = 8080;
	private static final int BROJ_KONEKCIJA = 4;
	
	private ServerSocket server;
	private Konekcija[] konekcije;
	
	private ArrayList<GlavniProzor> glavniProzori;
	private boolean work;
	
	public Server() {
		konekcije = new Konekcija[BROJ_KONEKCIJA];
		glavniProzori = new ArrayList<>();
	}
	
	public void startServer() {
		try {
			server = new ServerSocket(PORT);
			log("SERVER SLUSA NA PORTU: " + PORT);
			work = true;
			while (work) {
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
			log("Socket prekinut - ugasen server");
		}
	}
	
	public void closeServer() {
		if(server == null || server.isClosed())
			return;
		try {
			server.close();
			log("SERVER ZATVOREN");
			work = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean serverRunning() {
		return server != null && !server.isClosed();
	}
	
	public void addListener(GlavniProzor glavniProzor) {
		glavniProzori.add(glavniProzor);
	}

	private void log(String log) {
		System.out.println(log);
		for(GlavniProzor gp : glavniProzori)
			gp.addLog(log);
	}
}
