package server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private final int port = 8080;
	
	private ServerSocket server;
	private Socket[] konekcije;
	private ObjectInputStream input;
	//private ObjectOutputStream output;
	
	public Server() {
		konekcije = new Socket[4];
		for(int i = 0; i < konekcije.length; i++)
			konekcije[i] = new Socket();
		try {
			server = new ServerSocket(port, 4);
			System.out.println("Ceka se konekcija na konekciji (port: " + port + ")...");
			while( true ) {
				konekcije[0] = server.accept();
				System.out.println("Prihvacena konekcija: " + konekcije[0].getInetAddress());
				/*output = new ObjectOutputStream(konekcije[0].getOutputStream());
				output.flush();*/
				input = new ObjectInputStream(konekcije[0].getInputStream());
				System.out.println("I/O sistem postavljen!");
				String poruka = "";
				do {
					try {
						poruka = (String)input.readObject();
						System.out.println(konekcije[0].getInetAddress() + ":\t" + poruka);
					} catch (ClassNotFoundException e) {
						System.err.println("Nije poznat tip podatka");
					} catch (EOFException e2) {
						System.err.println("Sistem vise ne prima poruke!");
					}
				} while (!poruka.equals("END"));
				System.out.println("*---. ZATVORENA KONEKCIJA .---*");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				input.close();
				//output.close();
				server.close();
				for(Socket s : konekcije)
					if(!s.isClosed())
						s.close();
				System.out.println("*---. ZATVOREN I/O .---*");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String...strings) throws IOException {
		new Server();
	}
}
