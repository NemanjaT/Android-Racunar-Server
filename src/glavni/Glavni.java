package glavni;

import java.util.ArrayList;

import grafika.GlavniProzor;
import models.FileSystem;
import server.Server;

public class Glavni {
	
	public static void main(String...strings) {

		Server server = new Server();
		GlavniProzor glavniProzor = new GlavniProzor(server);
		glavniProzor.setVisible(true);

	}
	
}
