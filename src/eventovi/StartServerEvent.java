package eventovi;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import server.Server;

public class StartServerEvent implements ActionListener {
	
	private Server server;
	
	public StartServerEvent(Server server) {
		this.server = server;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		server.startServer();
	}
}
