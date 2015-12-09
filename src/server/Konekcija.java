package server;

import java.net.Socket;

public class Konekcija extends Socket {
	private static int ID = 0;
	
	private int id;
	
	public Konekcija() {
		super();
		id = ID++;
	}
	
	public int getId() {
		return id;
	}
	
	public String toString() {
		return "Socket[" + id + "]";
	}
}
