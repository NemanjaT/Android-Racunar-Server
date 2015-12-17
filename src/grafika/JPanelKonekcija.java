package grafika;

import java.util.ArrayList;

import javax.swing.JPanel;

import server.Konekcija;

public class JPanelKonekcija extends JPanel {
	private Konekcija konekcija;
	
	public JPanelKonekcija(Konekcija konekcija) {
		super();
		this.konekcija = konekcija;
	}
	
	public Konekcija getKonekcija() {
		return konekcija;
	}
}
