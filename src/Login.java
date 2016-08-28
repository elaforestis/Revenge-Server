
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Login extends Thread{
	static final Logger log = Logger.getLogger( Login.class.getName() );
	String user;
	byte[] pass;
	Socket sock;
	Socket nsock;
	
	Login(String username,Socket clientSocket,Socket notificationsSocket){
		user = username;
		sock = clientSocket;
		nsock = notificationsSocket;
		start();
	}
	public void run(){
		DataInputStream dIn = null;
		try {
			dIn = new DataInputStream(sock.getInputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try{
			int length = dIn.readInt();                    // read length of incoming message
			if(length>0) {
			    pass = new byte[length];
			    dIn.readFully(pass, 0, length); // read the message
			}
		}catch(Exception e){
			log.log(Level.SEVERE,e.toString(),e);
		}
		//dcSameIpCon(sock);
		DBConnect x = new DBConnect(user,null,null);
		boolean isLogged = isLoggedIn(user,sock);
		if(x.login(user, pass,sock,nsock) && !isLogged){
			PrintWriter pw;
			try {
				pw = new PrintWriter(sock.getOutputStream(),true);
				pw.println("SUCCESS");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.log(Level.SEVERE,e.toString(),e);
				
			}
		}else if(isLogged){
			PrintWriter pw;
			try {
				pw = new PrintWriter(sock.getOutputStream(),true);
				pw.println("toast:User is already logged in. Try again in 30 seconds.");
				sock.close();
				nsock.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.log(Level.SEVERE,e.toString(),e);
				
			}

		}else{
			PrintWriter pw;
			try {
				pw = new PrintWriter(sock.getOutputStream(),true);
				pw.println("toast:Invalid username/password");
				sock.close();
				nsock.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.log(Level.SEVERE,e.toString(),e);
				
			}

		}
	}
/*	public void dcSameIpCon(Socket sock){
		for(int i =0;i<Matchmaking.onlinePlayers.size();i++){
			if(((Player)Matchmaking.onlinePlayers.get(i)).username.equalsIgnoreCase(user)){
				if(((Player)Matchmaking.onlinePlayers.get(i)).clientSocket.getInetAddress().toString().equals(sock.getInetAddress().toString())){
					try {
						((Player)Matchmaking.onlinePlayers.get(i)).clientSocket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}*/
/*	public boolean isLoggedIn(Socket sock){
		for(int i =0;i<Matchmaking.onlinePlayers.size();i++){
			if(((Player)Matchmaking.onlinePlayers.get(i)).username.equalsIgnoreCase(user)){
				if(!((Player)Matchmaking.onlinePlayers.get(i)).clientSocket.getInetAddress().toString().equals(sock.getInetAddress().toString())){
					return true;
				}else if(((Player)Matchmaking.onlinePlayers.get(i)).clientSocket.getInetAddress().toString().equals(sock.getInetAddress().toString())){
					try {
						((Player)Matchmaking.onlinePlayers.get(i)).clientSocket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return false;
				}
			}
		}
		return false;
	}
	public static boolean[] isLoggedIn(String[] username){
		boolean[] areLoggedIn = new boolean[username.length];
out:	for(int i =0;i<Matchmaking.onlinePlayers.size();i++){
			for(int j = 0;j<username.length;j++){
				if(((Player)Matchmaking.onlinePlayers.get(i)).username.equalsIgnoreCase(username[j])){
						areLoggedIn[j] = true;
						continue out;
				}
			}
		}
		return areLoggedIn;
	}
	public static boolean isLoggedIn(String username){
		
		for(int i =0;i<Matchmaking.onlinePlayers.size();i++){
				if(((Player)Matchmaking.onlinePlayers.get(i)).username.equalsIgnoreCase(username)){
						return true;
				}
		}
		return false;
	}
	public static Socket getSocketByUser(String username){
		for(int i =0;i<Matchmaking.onlinePlayers.size();i++){
			if(((Player)Matchmaking.onlinePlayers.get(i)).username.equalsIgnoreCase(username)){
					return ((Player)Matchmaking.onlinePlayers.get(i)).clientSocket;
			}
		}
		return null;
	}
	public static Socket getNSocketByUser(String username){
		for(int i =0;i<Matchmaking.onlinePlayers.size();i++){
			if(((Player)Matchmaking.onlinePlayers.get(i)).username.equalsIgnoreCase(username)){
					return ((Player)Matchmaking.onlinePlayers.get(i)).nSocket;
			}
		}
		return null;
	}
	public static Player getPlayerByUser(String username){
		for(int i =0;i<Matchmaking.onlinePlayers.size();i++){
			if(((Player)Matchmaking.onlinePlayers.get(i)).username.equalsIgnoreCase(username)){
					return ((Player)Matchmaking.onlinePlayers.get(i));
			}
		}
		return null;
	}*/
	
	public boolean isLoggedIn(String user,Socket sock){
			Player p = Matchmaking.onlinePlayers.get(user);
			if(p==null){
				return false;
			}else{
				if(p.clientSocket.getInetAddress().toString().equals(sock.getInetAddress().toString())){
					try {
						Matchmaking.onlinePlayers.remove(user);
						p.clientSocket.close();
						p.nSocket.close();
					} catch (IOException e) {
						log.log(Level.SEVERE,e.toString(),e);
					}
					return false;
				}else if(!p.clientSocket.getInetAddress().toString().equals(sock.getInetAddress().toString())){
					return true;
				}
			}
			return true;
	}
	
	public static boolean[] isLoggedIn(String[] username){
		boolean[] areLoggedIn = new boolean[username.length];
		for(int i=0;i<username.length;i++){
			if(Matchmaking.onlinePlayers.get(username[i])!=null){
				areLoggedIn[i] = true;
			}
		}
		return areLoggedIn;
	}
	
	public static boolean isLoggedIn(String username){
		if(Matchmaking.onlinePlayers.get(username)!=null){
			return true;
		}else{
			return false;
		}
	}
	
	public static Socket getSocketByUser(String username){
		return Matchmaking.onlinePlayers.get(username).clientSocket;
	}
	public static Socket getNSocketByUser(String username){
		return Matchmaking.onlinePlayers.get(username).nSocket;
	}
	
	public static Player getPlayerByUser(String username){
		return Matchmaking.onlinePlayers.get(username);
	}
}
