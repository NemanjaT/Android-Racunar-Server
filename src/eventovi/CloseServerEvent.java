package eventovi;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import server.Server;

public class CloseServerEvent extends WindowAdapter{
	
	private Server server;
	
	public CloseServerEvent(Server server) {
		this.server = server;
	}
	
	@Override
	public void windowClosing(WindowEvent arg0) {
		server.closeServer();
		super.windowClosing(arg0);
	}
}
