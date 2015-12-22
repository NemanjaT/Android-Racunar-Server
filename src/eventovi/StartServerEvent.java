package eventovi;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import server.Server;

public class StartServerEvent implements ActionListener {
	
	private Server server;
	
	public StartServerEvent(Server server) {
		this.server = server;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(!server.serverRunning()) {
			((JButton)(arg0.getSource())).setText("...PREKINI SERVER...");
			new Thread(new Runnable() {
				@Override
				public void run() {
					server.startServer();
				}
			}).start();
		} else {
			((JButton)(arg0.getSource())).setText("...POKRENI SERVER...");
			new Thread(new Runnable() {
				@Override
				public void run() {
					server.closeServer();
				}
			}).start();
		}
	}
}
