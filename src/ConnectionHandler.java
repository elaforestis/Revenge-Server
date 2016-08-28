import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ConnectionHandler implements Runnable {
	final static Logger log = Logger.getLogger( Main.class.getName() );
	private InputStreamReader inputStreamReader;
	private BufferedReader bufferedReader;
	private String message;
    String from = "elaforestis@gmail.com";
	private Socket clientSocket;
	private Socket nSocket;
	
	ConnectionHandler(Socket socket,Socket nSocket){
		clientSocket = socket;
		this.nSocket = nSocket;
	}

	@Override
	public void run() {
		try{
		inputStreamReader = new InputStreamReader(clientSocket.getInputStream(),"UTF8");
		bufferedReader = new BufferedReader(inputStreamReader); // get the client message
		message = bufferedReader.readLine();
		System.out.println("message: "+message);
		if(message.startsWith("LOGIN")){
			String messagearray[] = message.split("\\-\\$\\-");
			if(messagearray.length>=2 && messagearray[1]!=null){
				new Login(messagearray[1],clientSocket,nSocket);
			}else{
				PrintWriter pw;
				try {
					pw = new PrintWriter(clientSocket.getOutputStream(),true);
					pw.println("toast:Error");
					clientSocket.close();
					nSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.log(Level.SEVERE,e.toString(),e);
					
				}

			}
		}else if(message.startsWith("SIGNUP")){
			String messagearray[] = message.split("\\-\\$\\-");
			if(messagearray.length>=5 && messagearray[1]!=null && messagearray[2]!=null && messagearray[3]!=null && messagearray[4]!=null){
				if(message.startsWith("SIGNUPIOS")){
					new Signup(messagearray[1],messagearray[2],messagearray[3],messagearray[4],clientSocket,nSocket,true);
				}else{
					new Signup(messagearray[1],messagearray[2],messagearray[3],messagearray[4],clientSocket,nSocket,false);
				}
			}else{
				PrintWriter pw;
				try {
					pw = new PrintWriter(clientSocket.getOutputStream(),true);
					pw.println("toast:Error");
					clientSocket.close();
					nSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.log(Level.SEVERE,e.toString(),e);
					
				}

			}
		}else if(message.startsWith("FORGOTPASS")){
			String messagearray[] = message.split("\\-\\$\\-");
			   MimeMessage message = null;
		         // Create a default MimeMessage object.
		         message = new MimeMessage(Main.session);

		         // Set From: header field of the header.
		         message.setFrom(new InternetAddress(from));

		         // Set Subject: header field
		         message.setSubject("Εκδίκηση - Ανάκτηση Κωδικού");
		         String tempPass = "override-"+randomString(8);
		         DBConnect.setPassword(messagearray[1],tempPass);
		         String email = DBConnect.getEmail(messagearray[1]);
		         // Now set the actual message
		         message.setText(messagearray[1]+",\nΛάβαμε το μήνυμα για την ανάκτηση του κωδικού πρόσβασής σας στην Εκδίκηση. \nΟ κωδικός σας είναι: " +tempPass+"\nΣας συνιστούμε να τον αλλάξετε μέσω των ρυθμίσεων στο προφίλ σας.\nΦιλικά,\nΗ ομάδα της Εκδίκησης");
		 		InternetAddress[] ia;
				try {
					ia = new InternetAddress[]{new InternetAddress(email)};
					 Main.transport.sendMessage(message,ia );
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				deleteOpenfire(messagearray[1]);
				PrintWriter pw;
				try {
					pw = new PrintWriter(clientSocket.getOutputStream(),true);
					pw.println("ok");
					clientSocket.close();
					nSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.log(Level.SEVERE,e.toString(),e);
					
				}

		}else if(message.startsWith("UNAUTHORISED-LOGIN")){
			String messagearray[] = message.split("\\-\\$\\-");
			if(messagearray.length>=2 && messagearray[1]!=null){
				new UnauthorisedLogin(messagearray[1],clientSocket,nSocket);
			}else{
				PrintWriter pw;
				try {
					pw = new PrintWriter(clientSocket.getOutputStream(),true);
					pw.println("toast:Error");
					clientSocket.close();
					nSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.log(Level.SEVERE,e.toString(),e);
					
				}

			}
		}else{
			PrintWriter pw;
			try {
				pw = new PrintWriter(clientSocket.getOutputStream(),true);
				pw.println("e psit, esi me ip "+clientSocket.getInetAddress().getHostAddress()+" gamiesai!");
				clientSocket.close();
				nSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.log(Level.SEVERE,e.toString(),e);
				
			}
		}
		}catch(Exception e){
			log.log(Level.SEVERE,e.toString(),e);
		}

	}
	String randomString( int len ){
		final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		Random rnd = new Random();
		   StringBuilder sb = new StringBuilder( len );
		   for( int i = 0; i < len; i++ ) 
		      sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
		   return sb.toString();
		}
	private static void deleteOpenfire(String user) {
		try{
			String url = "http://127.0.0.1:9090/plugins/restapi/v1/users/"+user;
			
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	
			con.setRequestMethod("DELETE");
	
			//add request header
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			con.setRequestProperty("Authorization", "dAMVlUbNlb86NEbyCrw3");
	
			//int responseCode = con.getResponseCode();
			//System.out.println("Response Code : " + responseCode);
		}catch(Exception e){
			log.log(Level.SEVERE,e.toString(),e);
		}

	}
}
