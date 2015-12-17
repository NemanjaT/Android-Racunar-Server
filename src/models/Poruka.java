package models;

import java.io.Serializable;
import java.util.Arrays;

public class Poruka implements Serializable {
	private String komanda;
	private String fajl;
	private String[] argumenti;
	private String ekstenzija;
	
	public Poruka() {}
	
	public Poruka(String komanda, String fajl, String ekstenzija, String... argumenti) {
		this.komanda    = komanda;
		this.fajl       = fajl;
		this.ekstenzija = ekstenzija;
		this.argumenti  = new String[argumenti.length];
		for(int i = 0; i < argumenti.length; i++) {
			this.argumenti[i] = argumenti[i];
		}
	}
	
	public String getKomanda() {
		return komanda;
	}
	
	public void setKomanda(String komanda) {
		this.komanda = komanda;
	}
	
	public String getFajl() {
		return fajl;
	}
	
	public void setFajl(String fajl) {
		this.fajl = fajl;
	}
	
	public String[] getArgumenti() {
		return argumenti;
	}
	
	public void setArgumenti(String[] argumenti) {
		this.argumenti = argumenti;
	}
	
	public String getEkstenzija() {
		return ekstenzija;
	}
	
	public void setEkstenzija(String ekstenzija) {
		this.ekstenzija = ekstenzija;
	}

	@Override
	public String toString() {
		return "Poruka [komanda=" + komanda + ", fajl=" + fajl + ", argumenti=" + Arrays.toString(argumenti)
				+ ", ekstenzija=" + ekstenzija + "]";
	}
}
