package grafika;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import server.Konekcija;

public class JPanelKonekcija extends JPanel {
	private Konekcija konekcija;
	private JTextArea textArea;
	
	public JPanelKonekcija(Konekcija konekcija) {
		super();
		this.konekcija = konekcija;

		init();
	}

	public void addLog(String log) {
		textArea.setText(textArea.getText() + log + System.lineSeparator());
	}
	
	public Konekcija getKonekcija() {
		return konekcija;
	}

	private void init() {
		//This
		setLayout(new BorderLayout());

		//Text Area
		this.textArea = new JTextArea();
		this.textArea.setEditable(false);
		this.add(textArea, BorderLayout.CENTER);

		//Kill Btn
		JButton killBtn = new JButton("Zatvori konekciju");
		killBtn.addActionListener((ActionEvent e) -> konekcija.close());
		this.add(killBtn, BorderLayout.SOUTH);
	}
}
