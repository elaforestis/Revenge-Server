import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class UnauthorisedLogin extends Thread{
	static final Logger log = Logger.getLogger( Login.class.getName() );
	String user;
	byte[] pass;
	Socket sock;
	Socket nsock;
	
	UnauthorisedLogin(String username,Socket clientSocket,Socket notificationsSocket){
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
		
		DBConnect x = new DBConnect(user,null,null);
		boolean isLogged = isLoggedIn(user,sock);
		if(!isLogged){
			if(Main.allUsernames.contains(user)){
				//System.out.println("Unauthorised Login");
				//unauthorised device
				//if the attempted password's hash with the stored salt matches the stored hash: log him in, send him the stored hash
				//else "lathos stoixeia"
	            
	            Key aesKey = new SecretKeySpec("BF8qwkm8CxOnSlNk".getBytes(), "AES");
	            byte[] decrypted = null;
	            try{
		            Cipher cipher = Cipher.getInstance("AES");
		            cipher.init(Cipher.DECRYPT_MODE, aesKey);
					decrypted = cipher.doFinal(pass);
	            }catch(Exception e){
	            	log.log(Level.SEVERE,e.toString(),e);
	            }
	            if(new String(decrypted).startsWith("override-")){
	            	byte[] dbpass = DBConnect.getPassword(user);
	            	if(Arrays.equals(decrypted, dbpass)){
		            	//correct pass entered! log him in, send the pass
		            	x.login(user, decrypted,sock,nsock);
		    			PrintWriter pw;
		    			try {
		    				pw = new PrintWriter(sock.getOutputStream(),true);
		    				pw.println("SUCCESS-INCOMING-PASS:");
		    	               DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
		    	                dos.writeInt(decrypted.length);
		    	                dos.write(decrypted);
		    			} catch (IOException e) {
		    				// TODO Auto-generated catch block
		    				log.log(Level.SEVERE,e.toString(),e);
		    				
		    			}
	            	}else{
						PrintWriter pw;
						try {
							pw = new PrintWriter(sock.getOutputStream(),true);
							pw.println("toast:Λάθος στοιχεία!");
							sock.close();
							nsock.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							log.log(Level.SEVERE,e.toString(),e);
							
						}
		            }
	            }else{
	/*	            System.out.println(Arrays.toString(decrypted));
		            System.out.println(new String(decrypted));*/
		            byte[] hashed = DBConnect.getPassword(user);
		            byte[] salt = DBConnect.getSalt(user);
		            byte[] attemptedHashed = null;
		            try {
						attemptedHashed = getEncryptedPassword(new String(decrypted),salt);
					} catch (NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvalidKeySpecException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            if(Arrays.equals(hashed, attemptedHashed)){
		            	//correct pass entered! log him in, send the pass
		            	x.login(user, attemptedHashed,sock,nsock);
		    			PrintWriter pw;
		    			try {
		    				pw = new PrintWriter(sock.getOutputStream(),true);
		    				pw.println("SUCCESS-INCOMING-PASS:");
		    	               DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
		    	                dos.writeInt(hashed.length);
		    	                dos.write(hashed);
		    			} catch (IOException e) {
		    				// TODO Auto-generated catch block
		    				log.log(Level.SEVERE,e.toString(),e);
		    				
		    			}
		            }else{
						PrintWriter pw;
						try {
							pw = new PrintWriter(sock.getOutputStream(),true);
							pw.println("toast:Λάθος στοιχεία!");
							sock.close();
							nsock.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							log.log(Level.SEVERE,e.toString(),e);
							
						}
		            }
	            }
			}else{
				PrintWriter pw;
				try {
					pw = new PrintWriter(sock.getOutputStream(),true);
					pw.println("toast:Λάθος στοιχεία!");
					sock.close();
					nsock.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.log(Level.SEVERE,e.toString(),e);
					
				}
			}
		}else{
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

		}
	}
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
	 public byte[] getEncryptedPassword(String password, byte[] salt)
			   throws NoSuchAlgorithmException, InvalidKeySpecException {
			  // PBKDF2 with SHA-1 as the hashing algorithm. Note that the NIST
			  // specifically names SHA-1 as an acceptable hashing algorithm for PBKDF2
			  String algorithm = "PBKDF2WithHmacSHA1";
			  // SHA-1 generates 160 bit hashes, so that's what makes sense here
			  int derivedKeyLength = 160;
			  // Pick an iteration count that works for you. The NIST recommends at
			  // least 1,000 iterations:
			  // http://csrc.nist.gov/publications/nistpubs/800-132/nist-sp800-132.pdf
			  // iOS 4.x reportedly uses 10,000:
			  // http://blog.crackpassword.com/2010/09/smartphone-forensics-cracking-blackberry-backup-passwords/
			  int iterations = 20000;

			  KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, derivedKeyLength);

			  SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);

			  return f.generateSecret(spec).getEncoded();
			 }
}