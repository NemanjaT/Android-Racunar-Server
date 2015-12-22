package grafika;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import server.Konekcija;

public class JPanelKonekcija extends JPanel {
	private Konekcija konekcija;
	private JTextArea textArea;
	private JButton killBtn;
	
	public JPanelKonekcija(Konekcija konekcija) {
		super();
		this.konekcija = konekcija;
		this.textArea = new JTextArea();
		this.textArea.setEditable(false);
		this.killBtn = new JButton("Zatvori konekciju");
		this.killBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				konekcija.close();
			}
		});
		
		setLayout(new BorderLayout());
		this.add(textArea, BorderLayout.CENTER);
		this.add(killBtn, BorderLayout.SOUTH);
	}
	
	public Konekcija getKonekcija() {
		return konekcija;
	}
}
