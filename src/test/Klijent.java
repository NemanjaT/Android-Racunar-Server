package test;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import models.Poruka;

public class Klijent extends JFrame
{
	private static final long serialVersionUID = 1146175602814370591L;
	
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output; //odlazece poruke
	private ObjectInputStream input;   //dolazece poruke
	//private String message = "";
	private Poruka message = null;
	private String serverIP;
	private Socket connection;
	
	public Klijent(String host)
	{
		super("Klijent");
		serverIP = host;
		userText = new JTextField();
		userText.setEditable(true);
		userText.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendMessage(new Poruka("", e.getActionCommand(), ""));
				userText.setText("");
			}
		});
		this.add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		this.add(new JScrollPane(chatWindow), BorderLayout.CENTER);
		this.setSize(300, 300);
		this.setVisible(true);
	}
	
	public void connectServer()
	{
		try {
			//konekcija na server
			showMessage(new Poruka("", " Attempting connection... \n", ""));
			this.connection = new Socket(InetAddress.getByName(serverIP), 8080);
			showMessage(new Poruka("", "\n Connected to: " + connection.getInetAddress().getHostName(), ""));
			/* Kad se otvori konekcija
			 *     1)napravi streamove:
			 */
			output = new ObjectOutputStream(connection.getOutputStream());
			output.flush();
			input = new ObjectInputStream(connection.getInputStream());
			showMessage(new Poruka("", "\n Streams are setup! \n", ""));
			//     2)kod dok chattovanje traje
			ableToType(true);
			this.message = new Poruka("", "", "");
			do {
				try {
					this.message = (Poruka)input.readObject();
					showMessage(this.message);
				} catch (ClassNotFoundException e) {
					showMessage(new Poruka("", "\n UNKNOWN OBJECT \n", ""));
				}
			} while( !this.message.equals("SERVER - END") );
		} catch (EOFException e1) {
			showMessage(new Poruka("", "\n Client terminated connection", ""));
		} catch (IOException e2) {
			e2.printStackTrace();
		} finally {
			//sicenje
			showMessage(new Poruka("", "\n Closing connection... \n", ""));
			ableToType(false);
			try {
				output.close();
				input.close();
				connection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void sendMessage(final Poruka msg)
	{
		try {
			output.writeObject(msg);
			output.flush();
			showMessage(msg);
		} catch (IOException e) {
			chatWindow.append("\n ERROR SENDING THE MESSAGE \n");
		}
	}
	
	/*private void showMessage(final String msg)
	{
		SwingUtilities.invokeLater( new Runnable()
		{
			public void run()
			{
				chatWindow.append(msg);
			}
		});
	}*/

	private void showMessage(final Poruka msg) {
		SwingUtilities.invokeLater(() -> chatWindow.append(msg.getFajl()));
	}
	
	private void ableToType(final boolean tof)
	{
		SwingUtilities.invokeLater( new Runnable()
		{
			public void run()
			{
				userText.setEditable(tof);
			}
		});
	}
	
	public static void main(String...strings)
	{
		Klijent client = new Klijent("127.0.0.1");
		client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.connectServer();
	}
}
