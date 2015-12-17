package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private final int port = 8080;
	private final int brojKonekcija = 4;
	
	private ServerSocket server;
	private Konekcija[] konekcije;
	
	public Server() {
		konekcije = new Konekcija[brojKonekcija];
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
						break;
					}
				}
			}
		} catch (IOException io) {
			io.printStackTrace();
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
}
