package glavni;

import grafika.GlavniProzor;
import server.Server;

public class Glavni {
	
	public static void main(String...strings) {
		Server server = new Server();
		GlavniProzor glavniProzor = new GlavniProzor(server);
	}
	
}
