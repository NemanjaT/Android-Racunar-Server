package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private final int port = 8888;
	
	private ServerSocket server;
	private Konekcija[] konekcije;
	
	public Server() {
		konekcije = new Konekcija[4];
		this.startServer();
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
	
	public static void main(String...strings) throws IOException {
		new Server();
	}
}
