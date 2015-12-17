package server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import models.Poruka;

public class Konekcija implements Runnable {
	private static int ID = 0;
	
	private final int id;
	private Socket konekcija;
	private ObjectInputStream input;
	private ObjectOutputStream output;
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
	
	public void posaljiPoruku(Poruka poruka) {
		try {
			output.writeObject(poruka);
			output.flush();
			log("+Poslao fajl: " + poruka);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void posaljiPoruku(String poruka) {
		try {
			output.writeObject(poruka);
			output.flush();
			log("+Poslao string fajl: " + poruka);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		work = true;
		try {
			try {
				output = new ObjectOutputStream(konekcija.getOutputStream());
				output.flush();
				input = new ObjectInputStream(konekcija.getInputStream());
				log("~Postavljen I/O Sistem!");
				/**
				 * izmeni da sistem salje (i prima) objekte klase Poruka (ne String)
				 */
				Poruka porukaa = new Poruka("dir", "C:/bullshit/", "dir");
				posaljiPoruku("BEGIN");
				posaljiPoruku("BEGIN2");
				posaljiPoruku("BEGIN3");
				posaljiPoruku("BEGINFUCKING4");
				posaljiPoruku("END");
				String poruka = "";
				do {
					try {
						poruka = (String)input.readObject();
						log(poruka);
					} catch (ClassNotFoundException cnf) {
						log("~Nepoznata klasa od strane klijenta!");
					}
				} while(!poruka.equals("END"));
				
			} catch (Exception eof) {
				log("~Prekinuta konekcija sa klijentom");
			} finally {
				konekcija.close();
				input.close();
				output.close();
				log("~Uklonjen I/O Sistem.");
				work = false;
			}
		} catch (IOException io) {
			io.printStackTrace();
		} 
	}
	
	private void log(String poruka) {
		System.out.println(this + ": " + poruka);
	}
}
