package glavni;

import java.util.ArrayList;

import grafika.GlavniProzor;
import models.FileSystem;
import server.Server;

public class Glavni {
	
	public static void main(String...strings) {
		Server server = new Server();
		GlavniProzor glavniProzor = new GlavniProzor(server);
		
		FileSystem fs = new FileSystem();
		ArrayList<String> s = fs.getDrives();
		int i=0;for(String ss : s)
			System.out.println((i++) + ": " + ss);
	}
	
}
