package server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class Konekcija implements Runnable {
	private static int ID = 0;
	
	private final int id;
	private Socket konekcija;
	private ObjectInputStream input;
	//private ObjectOutputStream output;
	private volatile boolean work;
	
	public Konekcija(Socket konekcija) {
		this.id = ID++;
		this.work = false;
		this.konekcija = konekcija;
	}
	
	public boolean works() {
		return this.work;
	}
	
	public String toString() {
		return "Konekcija[" + id + "]";
	}
	
	private void log(String poruka) {
		System.err.print(this + ":");
		System.out.println(" " + poruka);
	}

	@Override
	public void run() {
		work = true;
		try {
			try {
				//output = new ObjectOutputStream(konekcija.getOutputStream());
				//output.flush();
				input = new ObjectInputStream(konekcija.getInputStream());
				log("~Postavljen I/O Sistem!");
				String poruka = "";
				do {
					try {
						poruka = (String)input.readObject();
						log(poruka);
					} catch (ClassNotFoundException cnf) {
						log("~Nepoznata klasa od strane klijenta!");
					}
				} while(!poruka.equals("END"));
			} catch (EOFException eof) {
				log("~Prekinuta konekcija sa klijentom");
			} finally {
				konekcija.close();
				input.close();
				//output.close();
				log("~Uklonjen I/O Sistem.");
				work = false;
			}
		} catch (IOException io) {
			io.printStackTrace();
		} 
	}
}
