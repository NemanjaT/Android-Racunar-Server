package grafika;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.*;

import eventovi.CloseServerEvent;
import eventovi.StartServerEvent;
import server.Konekcija;
import server.Server;

public class GlavniProzor extends JFrame {
	private JTabbedPane tabbedPane;
	private JButton serverStartBtn;
	
	private JPanel glavniPanel;
	private ArrayList<JPanelKonekcija> paneli;
	private Server server;
	
	public GlavniProzor(Server server) {
		tabbedPane = new JTabbedPane();
		paneli = new ArrayList<JPanelKonekcija>();
		this.server = server;
		server.addListener(this);
		serverStartBtn = new JButton("...POKRENI SERVER...");
		
		setTitle("Server aplikacija");
		setSize(600, 300);
		addWindowListener(new CloseServerEvent(server));
		setVisible(true);
		add(tabbedPane);
		
		this.glavniPanel = new JPanel();
		
		glavniPanel.setLayout(new GridLayout(3, 3));
		glavniPanel.add(serverStartBtn);
		serverStartBtn.addActionListener(new StartServerEvent(server));
		
		tabbedPane.addTab("Glavni", glavniPanel);
	}
	
	public void addPanel(Konekcija konekcija) {
		JPanelKonekcija panel = new JPanelKonekcija(konekcija);
		paneli.add(panel);
		tabbedPane.addTab(konekcija.toString(), panel);
	}
	
	public Server getServer() {
		return server;
	}
}
