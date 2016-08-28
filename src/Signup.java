import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Signup extends Thread{
	static final Logger log = Logger.getLogger( Signup.class.getName() );
	String user;
	byte[] pass;
	Socket sock;
	boolean ios;
	String nomos;
	String perioxi;
	byte[] salt;
	String avatar;
	Socket nsock;
	String email;
	
	
	Signup(String username,String area,String avatar,String email,Socket clientSocket,Socket nSocket,boolean ios){
		user = username;
		this.avatar = avatar;
		sock = clientSocket;
		this.email=email;
		nomos = area.split(" : ")[0];
		this.ios = ios;
		perioxi = area.split(" : ")[1];
		nsock = nSocket;
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
			//System.out.println("length1 "+length);
			if(length>0) {
			    pass = new byte[length];
			    dIn.readFully(pass, 0, length); // read the message
			    //System.out.println("array1 "+Arrays.toString(pass));
			}
			length = dIn.readInt();
			//System.out.println("length2 "+length);
			if(length>0){
				salt = new byte[length];
				if(this.ios){
					dIn.readFully(salt, 0, length-4);
					dIn.readFully(salt,0,length);
				}else{
					dIn.readFully(salt, 0, length);
				}
				//System.out.println("array2 "+Arrays.toString(salt));
			}
		}catch(Exception e){
			log.log(Level.SEVERE,e.toString(),e);
		}
		DBConnect x = new DBConnect("root",null,null);
		if(x.existsUsername(user)){
			PrintWriter pw;
			try {
				pw = new PrintWriter(sock.getOutputStream(),true);
				pw.println("toast:Username exists");
				sock.close();
				nsock.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.log(Level.SEVERE,e.toString(),e);
			}
		}else{
			x.accountCreate(user, pass,salt,nomos,perioxi,avatar,email,sock,nsock);
			PrintWriter pw;
			try {
				pw = new PrintWriter(sock.getOutputStream(),true);
				pw.println("SUCCESS");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.log(Level.SEVERE,e.toString(),e);
			}
		}
	}
	
}
