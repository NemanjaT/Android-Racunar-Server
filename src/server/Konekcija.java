package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import grafika.GlavniProzor;
import models.Poruka;

public class Konekcija implements Runnable {
	private static int ID = 0;
	
	private final int id;
	private Socket konekcija;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private volatile boolean work;
	
	private ArrayList<GlavniProzor> glavniProzori;
	
	public Konekcija(Socket konekcija) {
		this.id = ID++;
		this.work = false;
		this.konekcija = konekcija;
		this.glavniProzori = new ArrayList<>();
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
			output = new ObjectOutputStream(konekcija.getOutputStream());
			output.flush();
			input = new ObjectInputStream(konekcija.getInputStream());
			log("~Postavljen I/O Sistem!");
			posaljiPoruku(new Poruka("", "START", ""));
			posaljiPoruku(new Poruka("", "tekst1", ""));
			posaljiPoruku(new Poruka("", "tekst2", ""));
			posaljiPoruku(new Poruka("", "tekst3", ""));
			posaljiPoruku(new Poruka("", "tekst4", ""));
			posaljiPoruku(new Poruka("", "END", ""));
			posaljiPoruku(new Poruka("", "SHUTDOWN", ""));
			Poruka poruka = new Poruka();
			do {
				try {
					poruka = (Poruka)input.readObject();
					log(poruka);
				} catch (ClassNotFoundException cnf) {
					log("~Nepoznata klasa od strane klijenta!");
				}
			} while(!poruka.getFajl().equals("END"));
			
		} catch (Exception eof) {
			log("~Prekinuta konekcija sa klijentom");
		} finally {
			close();
		} 
	}
	
	public void close() {
		try {
			posaljiPoruku(new Poruka("", "SHUTDOWN", ""));
			konekcija.close();
			input.close();
			output.close();
			log("~Nasilno prekinuta konekcija (I/O sistem uklonjen.)");
			work = false;
			for(GlavniProzor gp : glavniProzori) {
				gp.checkPanels();
			}
		} catch (IOException io) {
			io.printStackTrace();
		}
	}
	
	public void addListener(GlavniProzor glavniProzor) {
		glavniProzori.add(glavniProzor);
	}
	
	private void log(Object poruka) {
		System.out.println(this + ": " + poruka);
		for(GlavniProzor gp : glavniProzori)
			gp.konekcijaLog(this, poruka.toString());
	}
}
