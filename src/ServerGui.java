import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;


@SuppressWarnings("serial")
public class ServerGui extends JFrame{
	JPanel jp;
	JButton start,shutdown;
	public static volatile JLabel online;
	public static volatile JLabel inGame;
	JScrollPane scroll;
	static volatile JTextArea log;
	static volatile int onlinePlayers = 0;
	static volatile int ingamePlayers = 0;
	String ip = null;
	
	public ServerGui(){
		super("Revenge Server");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setSize(700,500);
		setResizable(false);
		getContentPane().setLayout(null);
		jp = new JPanel();
		jp.setBounds(0, 0, 700, 500);
		start = new JButton("Start");
		start.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Main.start();
				start.setEnabled(false);
				shutdown.setEnabled(true);
			}
			
		});
		shutdown = new JButton("Shutdown");
		shutdown.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Main.stop();
				start.setEnabled(true);
				shutdown.setEnabled(false);
			}
			
		});
		online = new JLabel("Online: "+onlinePlayers);
		inGame = new JLabel("Ingame: "+ingamePlayers);
		log = new JTextArea(25,58);
		log.setEditable(false);
		log.setBackground(Color.BLACK);
		log.setForeground(Color.WHITE);
		log.setLineWrap(true);
		log.setWrapStyleWord(false);
		DefaultCaret caret = (DefaultCaret)log.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		scroll = new JScrollPane(log);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jp.add(start);
		jp.add(online);
		jp.add(inGame);
		shutdown.setEnabled(false);
		jp.add(shutdown);
		jp.add(scroll);
		URL whatismyip = null;
		BufferedReader in=null;
		try {
			whatismyip = new URL("http://checkip.amazonaws.com");
			in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
			ip = in.readLine();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JLabel ipL = new JLabel("IP = "+ip);
		jp.add(ipL);
		getContentPane().add(jp);
		setVisible(true);
	}

	public static void setOnline(int num){
		online.setText("Online: "+num);
	}
	public static void increaseIngame(){
		ingamePlayers+=2;
		inGame.setText("Ingame: "+ingamePlayers);
	}
	public static void decreaseIngame(){
		ingamePlayers-=2;
		inGame.setText("Ingame: "+ingamePlayers);
	}
	public static void log(String s){
		log.append(s);
	}
	 
}


