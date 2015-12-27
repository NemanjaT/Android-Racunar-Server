package models;

import java.io.Serializable;
import java.util.ArrayList;

public class PorukaGroup implements Serializable {
	ArrayList<Poruka> poruke;
	
	public PorukaGroup() {
		poruke = new ArrayList<>();
	}

	public ArrayList<Poruka> getPoruke() {
		return poruke;
	}

	public void setPoruke(ArrayList<Poruka> poruke) {
		this.poruke = poruke;
	}

	@Override
	public String toString() {
		return "PorukaGroup [poruke=" + poruke + "]";
	}
}
