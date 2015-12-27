package grafika;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

import eventovi.CloseServerEvent;
import eventovi.StartServerEvent;
import server.Konekcija;
import server.Server;

public class GlavniProzor extends JFrame {
	private JTabbedPane tabbedPane;
	private JTextArea textArea;

	private ArrayList<JPanelKonekcija> paneli;
	private Server server;
	
	public GlavniProzor(Server server) {
		paneli = new ArrayList<>();
		this.server = server;
		server.addListener(this);

		init();
	}
	
	public void addPanel(Konekcija konekcija) {
		JPanelKonekcija panel = new JPanelKonekcija(konekcija);
		paneli.add(panel);
		konekcija.addListener(this);
		tabbedPane.addTab(konekcija.toString(), panel);
	}
	
	public void checkPanels() {
		for (int i = paneli.size() - 1; i >= 0; i--) {
			if(!paneli.get(i).getKonekcija().works()) {
				tabbedPane.remove(paneli.get(i));
				paneli.remove(i);
			}
		}
	}

	public void addLog(String log) {
		textArea.setText(textArea.getText() + log + System.lineSeparator());
	}

	public void konekcijaLog(Konekcija kon, String log) {
		for(JPanelKonekcija jpk : paneli) {
			if(jpk.getKonekcija() == kon) {
				jpk.addLog(log);
			}
		}
	}

	private void init() {
		//This
		setTitle("Server aplikacija");
		setSize(600, 300);
		addWindowListener(new CloseServerEvent(server));
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException
				| InstantiationException
				| IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		//Tabbed Pane
		tabbedPane = new JTabbedPane();
		this.add(tabbedPane);

		//Glavni Panel
		JPanel glavniPanel = new JPanel();
		glavniPanel.setLayout(new BorderLayout());
		tabbedPane.addTab("Glavni", glavniPanel);

		//Server Start Btn
		JButton serverStartBtn = new JButton("...POKRENI SERVER...");
		serverStartBtn.addActionListener(new StartServerEvent(server));
		glavniPanel.add(serverStartBtn, BorderLayout.NORTH);

		//TextArea
		textArea = new JTextArea();
		textArea.setEditable(false);
		JScrollPane scroll = new JScrollPane(textArea);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		glavniPanel.add(scroll, BorderLayout.CENTER);
	}
}
