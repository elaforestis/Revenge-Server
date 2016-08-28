import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;



public class Player extends Thread{
	public static volatile ArrayList<String> bothReplayPressed = new ArrayList<String>();
	static final Logger log = Logger.getLogger( Player.class.getName() );
	public String username;
	public int winsRow;
	public int points;
	public int fastestTime;
	public int games;
	public Socket clientSocket;
	public String nomos;
	boolean finalize = true;
	public String perioxi;
	public int avatar; //1=boy, 2=girl
	public String friends;
	public DBConnect d;
	boolean isChallengeQueued = false;
	volatile boolean forceBreak = false;
	Socket nSocket;
	int rank;
	int nomosRank;
	int perioxiRank;
	public Socket previousOpponentSocket;
	public Player previousOpponent;
	public long lastGameSystemMilis;
	public Connection generalTable,nomosTable;

	public Player(String u,Socket sock,Socket nsock,boolean gameEnd){
		if(!gameEnd){
			username = u;
			clientSocket = sock;
			nSocket = nsock;
			//Matchmaking.onlinePlayers.add(this);
			Matchmaking.onlinePlayers.put(username,this);
			start();
		}else{
			username = u;
			clientSocket = sock;
			nSocket = nsock;
			//Matchmaking.onlinePlayers.add(this);
			Matchmaking.onlinePlayers.put(username,this);
			start();
		}
	}
	

	protected void finalize(){
		if(finalize){
			System.out.println("killed player "+username);
			try{
				generalTable.close();
				nomosTable.close();
			}catch(Exception e){
				log.log(Level.SEVERE, e.toString(),e);
			}
		}
	}
	public void run() {
		String message = "";
		while(true){
			if(forceBreak){
				System.out.println(username+" broke out");
				break;
			}
			try{
				InputStreamReader inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
				BufferedReader br= new BufferedReader(inputStreamReader); // get the client message
				message = br.readLine();
				System.out.println(username+":"+message);
			} catch (Exception ex) {
				System.out.println(username+" disconnected");
				Matchmaking.onlinePlayers.remove(username);
				try{
					clientSocket.close();
					nSocket.close();
				}catch(Exception e){
					log.log(Level.SEVERE, e.toString(),e);
				}
				break;
			}
			if(message==null){
				System.out.println(username+" disconnected");
				Matchmaking.onlinePlayers.remove(username);
				try{
					clientSocket.close();
					nSocket.close();
				}catch(Exception e){
					log.log(Level.SEVERE, e.toString(),e);
				}
				break;
			}else if(message.contains("challenge_cancelled:")){
				System.out.println("challenge cancelled");
				this.isChallengeQueued=false;
				PrintWriter pw;
				try {
					pw = new PrintWriter(clientSocket.getOutputStream(),true);
					pw.println("challenge_ok_cancelled");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.log(Level.SEVERE,e.toString(),e);
					
				}
			}else if(message.equals("going_in")){
				break;
			}else if(message.contains("changePass")){
				byte[] pass = null;
				byte[] salt = null;
				DataInputStream dIn = null;
				try {
					dIn = new DataInputStream(clientSocket.getInputStream());
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
					length = dIn.readInt();
					if(length>0){
						salt = new byte[length];
						dIn.readFully(salt, 0, length);
					}
				}catch(Exception e){
					log.log(Level.SEVERE,e.toString(),e);
				}
				
				d.changePass(username,pass,salt);
			}else if(message.contains("changeEmail:")){
				String newEmail = message.split("changeEmail:")[1];
				d.changeEmail(username,newEmail);
			}else if(message.equals("game_queue")){
				System.out.println("Queued");
				Matchmaking.queuedPlayers.add(this);
			}else if(message.equals("cancel_queue")){
				System.out.println("Cancelled Queue");
				Matchmaking.queuedPlayers.remove(this);
				PrintWriter pw;
				try {
					pw = new PrintWriter(clientSocket.getOutputStream(),true);
					pw.println("game_queue_ok_cancelled");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.log(Level.SEVERE,e.toString(),e);
					
				}
			}else if(message.equals("request_profile")){
				PrintWriter pw = null;
				try {
					String newnomos1 = nomos.replace("1", "").replace("2", "").replace("3", "").replace("4", "");
					String newnomos = "";
					for (int i=0;i<Main.NOMOI.length;i++){
						if(Main.NOMOI[i].equals(newnomos1)){
							newnomos = Main.NOMOILOWERCASE[i];
						}
					}
					pw = new PrintWriter(clientSocket.getOutputStream(),true);
					
					points = d.getPoints(username);
					rank = d.getRank(username);
					int dailyRank = d.getDailyRank(username);
					nomosRank = d.getNomosRank(username, nomos);
					int nomosDailyRank = d.getNomosDailyRank(username,nomos);
					perioxiRank = d.getPerioxiRank(username, nomos, perioxi);
					int perioxiDailyRank = d.getPerioxiDailyRank(username,nomos,perioxi);
					
					if(games!=0){
						if(!(fastestTime==2147000000)){
							pw.println("profile:"+"<b><big><u>ΚΑΤΑΤΑΞΗ</u></big><br></b><b><br>Πανελλαδικά</b><font color=\"blue\"><br><big><big>#"+rank+"</big></big></font><br><b>Νομός "+newnomos +"</b><font color=\"blue\"><br><big><big>#"+nomosRank+"</big></big></font><br><b>"+perioxi.replace("_", " ")+"</b><br><font color=\"blue\"><big><big>#"+ perioxiRank+"</big></big></font><br><br>"+"<b>Πόντοι:</b><br><font color=\"blue\">"+points+"</font><br><br>"+"<b>Παιχνίδια:</b><br><font color=\"blue\">"+games+"</font><br><br><b>Γρηγορότερος χρόνος:</b><br><font color=\"blue\">"+ fastestTime+"ms</font>"+"profileDAILY:"+"<b><big><u>ΚΑΤΑΤΑΞΗ</u></big><br></b><b><br>Πανελλαδικά</b><font color=\"blue\"><br><big><big>#"+dailyRank+"</big></big></font><br><b>Νομός "+newnomos +"</b><font color=\"blue\"><br><big><big>#"+nomosDailyRank+"</big></big></font><br><b>"+perioxi.replace("_", " ")+"</b><br><font color=\"blue\"><big><big>#"+perioxiDailyRank+"</big></big></font>");//TODO
							
						}else{
							pw.println("profile:"+"<b><big><u>ΚΑΤΑΤΑΞΗ</u></big><br></b><b><br>Πανελλαδικά</b><font color=\"blue\"><br><big><big>#"+rank+"</big></big></font><br><b>Νομός "+newnomos +"</b><font color=\"blue\"><br><big><big>#"+nomosRank+"</big></big></font><br><b>"+perioxi.replace("_", " ")+"</b><br><font color=\"blue\"><big><big>#"+ perioxiRank+"</big></big></font><br><br>"+"<b>Πόντοι:</b><br><font color=\"blue\">"+points+"</font><br><br>"+"<b>Παιχνίδια:</b><br><font color=\"blue\">"+games+"</font><br><br><b>Γρηγορότερος χρόνος:</b><br><font color=\"blue\">Απροσδιόριστος</font>"+"profileDAILY:"+"<b><big><u>ΚΑΤΑΤΑΞΗ</u></big><br></b><b><br>Πανελλαδικά</b><font color=\"blue\"><br><big><big>#"+dailyRank+"</big></big></font><br><b>Νομός "+newnomos +"</b><font color=\"blue\"><br><big><big>#"+nomosDailyRank+"</big></big></font><br><b>"+perioxi.replace("_", " ")+"</b><br><font color=\"blue\"><big><big>#"+perioxiDailyRank+"</big></big></font>");//TODO
							
						}
					}else{
						if(!(fastestTime==2147000000)){
							pw.println("profile:"+"<b><big><u>ΚΑΤΑΤΑΞΗ</u></big><br></b><b><br>Πανελλαδικά<t></b><font color=\"blue\"><br><big><big>#"+rank+"</big></big></font><br><b>Νομός "+newnomos+"</b><font color=\"blue\"><br><big><big>#"+nomosRank+"</big></big></font><br><b>"+perioxi.replace("_", " ")+"</b><br><font color=\"blue\"><big><big>#"+ perioxiRank+"</big></big></font><br><br>"+"<b>Πόντοι:</b><br><font color=\"blue\">"+points+"</font><br><br>"+"<b>Παιχνίδια:</b><br><font color=\"blue\">"+games+"</font><br><br><b>Γρηγορότερος χρόνος:</b><br><font color=\"blue\">"+ fastestTime+"ms</font><br><br><b>Μέσος χρόνος:</b><br><font color=\"blue\">Απροσδιόριστος</font>"+"profileDAILY:"+"<b><big><u>ΚΑΤΑΤΑΞΗ</u></big><br></b><b><br>Πανελλαδικά</b><font color=\"blue\"><br><big><big>#"+dailyRank+"</big></big></font><br><b>Νομός "+newnomos +"</b><font color=\"blue\"><br><big><big>#"+nomosDailyRank+"</big></big></font><br><b>"+perioxi.replace("_", " ")+"</b><br><font color=\"blue\"><big><big>#"+perioxiDailyRank+"</big></big></font>");//TODO
							
						}else{
							pw.println("profile:"+"<b><big><u>ΚΑΤΑΤΑΞΗ</u></big><br></b><b><br>Πανελλαδικά</b><font color=\"blue\"><br><big><big>#"+rank+"</big></big></font><br><b>Νομός "+newnomos +"</b><font color=\"blue\"><br><big><big>#"+nomosRank+"</big></big></font><br><b>"+perioxi.replace("_", " ")+"</b><br><font color=\"blue\"><big><big>#"+perioxiRank+"</big></big></font><br><br>"+"<b>Πόντοι:</b><br><font color=\"blue\">"+points+"</font><br><br>"+"<b>Παιχνίδια:</b><br><font color=\"blue\">"+games+"</font><br><br><b>Γρηγορότερος χρόνος:</b><br><font color=\"blue\">Απροσδιόριστος</font><br><br><b>Μέσος χρόνος:</b><br><font color=\"blue\">Απροσδιόριστος</font>"+"profileDAILY:"+"<b><big><u>ΚΑΤΑΤΑΞΗ</u></big><br></b><b><br>Πανελλαδικά</b><font color=\"blue\"><br><big><big>#"+dailyRank+"</big></big></font><br><b>Νομός "+newnomos +"</b><font color=\"blue\"><br><big><big>#"+nomosDailyRank+"</big></big></font><br><b>"+perioxi.replace("_", " ")+"</b><br><font color=\"blue\"><big><big>#"+perioxiDailyRank+"</big></big></font>");//TODO
						}
					}
				} catch (IOException e) {	
					log.log(Level.SEVERE,e.toString(),e);
					
				}
			}else if(message.equals("request_friends")){
				try {
					friends = d.getFriends(username);
					PrintWriter pw = new PrintWriter(clientSocket.getOutputStream(),true);
					pw.println("friends:"+getFinalFriends(friends));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.log(Level.SEVERE,e.toString(),e);
					
				}
			}else if(message.contains("add_friend:")){
				new notificationHandler(username,message.replace("add_friend:", ""),"frequest");
			}else if(message.contains("accept_friend:")){
				String targetFriends = d.getFriends(message.replace("accept_friend:",""));
				if(targetFriends.equals("")){
					targetFriends+=username;
				}else{
					targetFriends+="~%~*"+username;
				}
				d.updateFriends(message.replace("accept_friend:",""),targetFriends);
				
				String friends = d.getFriends(username);
				if(friends.equals("")){
					friends+=message.replace("accept_friend:","");
				}else{
					friends+="~%~*"+message.replace("accept_friend:","");
				}
				d.updateFriends(username,friends);
				
				friends = d.getFriends(username);
			}else if(message.contains("requestPerioxi")){
				try {
					PrintWriter pw = new PrintWriter(clientSocket.getOutputStream(),true);
					pw.println(nomos+"666"+perioxi);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.log(Level.SEVERE,e.toString(),e);
					
				}
			}else if(message.contains("challenge:")){
				this.isChallengeQueued=true;
				new notificationHandler(username,message.replace("challenge:", ""),"challenge");
				PrintWriter pw2;
				try {
					pw2 = new PrintWriter(clientSocket.getOutputStream(),true);
					pw2.println("challengedperioxi:"+d.getNomos(message.replace("challenge:", ""))+"666"+d.getPerioxi(message.replace("challenge:", ""))+"-$-"+avatar+d.getAvatar(message.replace("challenge:", "")));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.log(Level.SEVERE,e.toString(),e);
					
				}
			}else if(message.contains("challenge_accept:")){
				PrintWriter pw3;
				try {
					pw3 = new PrintWriter(clientSocket.getOutputStream(),true);
					pw3.println("challengerperioxi:"+d.getNomos(message.replace("challenge_accept:", ""))+"666"+d.getPerioxi(message.replace("challenge_accept:", ""))+"-$-"+avatar+d.getAvatar(message.replace("challenge_accept:", "")));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.log(Level.SEVERE,e.toString(),e);
					
				}
				PrintWriter pw;
				try {
					Player challenger = Login.getPlayerByUser(message.replace("challenge_accept:",""));
					pw = new PrintWriter(challenger.clientSocket.getOutputStream(),true);
					pw.println("challenge_accept");
					
					if(challenger.isChallengeQueued){
						PrintWriter pw2;
						try {
							pw2 = new PrintWriter(clientSocket.getOutputStream(),true);
							pw2.println("challenge_on");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							log.log(Level.SEVERE,e.toString(),e);
							
						}
						new InGame(this,challenger);
						break;
					}else{
						PrintWriter pw2;
						try {
							pw2 = new PrintWriter(clientSocket.getOutputStream(),true);
							pw2.println("challenge_off");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							log.log(Level.SEVERE,e.toString(),e);
							
						}
					}
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.log(Level.SEVERE,e.toString(),e);
					
				}catch(NullPointerException npe){
					PrintWriter pw2;
					try {
						pw2 = new PrintWriter(clientSocket.getOutputStream(),true);
						pw2.println("challenge_off");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						log.log(Level.SEVERE,e.toString(),e);
						
					}
				}

			}else if(message.contains("challenge_decline:")){
				PrintWriter pw;
				try {
					pw = new PrintWriter(Login.getSocketByUser(message.replace("challenge_decline:", "")).getOutputStream(),true);
					pw.println("challenge_decline");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.log(Level.SEVERE,e.toString(),e);
					
				} catch(NullPointerException e){
					//do nothing
				}
			}else if(message.contains("request_profile:")){
				String u = message.replace("request_profile:", "");
				String perioxi = d.getPerioxi(u).replace("_", " ");
				String newnomos1 = d.getNomos(u).replace("1", "").replace("2", "").replace("3", "").replace("4", "");
				String newnomos = "";
				for (int i=0;i<Main.NOMOI.length;i++){
					if(Main.NOMOI[i].equals(newnomos1)){
						newnomos = Main.NOMOILOWERCASE[i];
					}
				}
				PrintWriter pw = null;
				try {
					pw = new PrintWriter(clientSocket.getOutputStream(),true);
					if(d.getGames(u)!=0){
						if(!(d.getFastestTime(u)==2147000000)){
							pw.println("profile:"+"<b><big><u>ΚΑΤΑΤΑΞΗ</u></big><br></b><b><br>Πανελλαδικά</b><font color=\"blue\"><br><big><big>#"+d.getRank(u)+"</big></big></font><br><b>Νομός "+newnomos +"</b><font color=\"blue\"><br><big><big>#"+d.getNomosRank(u,d.getNomos(u))+"</big></big></font><br><b>"+perioxi+"</b><br><font color=\"blue\"><big><big>#"+d.getPerioxiRank(u,d.getNomos(u),d.getPerioxi(u))+"</big></big></font><br><br>"+"<b>Πόντοι:</b><br><font color=\"blue\">"+d.getPoints(u)+"</font><br><br>"+"<b>Παιχνίδια:</b><br><font color=\"blue\">"+d.getGames(u)+"</font><br><br><b>Γρηγορότερος χρόνος:</b><br><font color=\"blue\">"+ d.getFastestTime(u)+"ms</font>"+"profileDAILY:"+"<b><big><u>ΚΑΤΑΤΑΞΗ</u></big><br></b><b><br>Πανελλαδικά</b><font color=\"blue\"><br><big><big>#"+d.getDailyRank(u)+"</big></big></font><br><b>Νομός "+newnomos +"</b><font color=\"blue\"><br><big><big>#"+d.getNomosDailyRank(u, d.getNomos(u))+"</big></big></font><br><b>"+perioxi+"</b><br><font color=\"blue\"><big><big>#"+d.getPerioxiDailyRank(u, d.getNomos(u), d.getPerioxi(u))+"</big></big></font>");//TODO
						}else{
							pw.println("profile:"+"<b><big><u>ΚΑΤΑΤΑΞΗ</u></big><br></b><b><br>Πανελλαδικά</b><font color=\"blue\"><br><big><big>#"+d.getRank(u)+"</big></big></font><br><b>Νομός "+newnomos +"</b><font color=\"blue\"><br><big><big>#"+d.getNomosRank(u,d.getNomos(u))+"</big></big></font><br><b>"+perioxi+"</b><br><font color=\"blue\"><big><big>#"+d.getPerioxiRank(u,d.getNomos(u),d.getPerioxi(u))+"</big></big></font><br><br>"+"<b>Πόντοι:</b><br><font color=\"blue\">"+d.getPoints(u)+"</font><br><br>"+"<b>Παιχνίδια:</b><br><font color=\"blue\">"+d.getGames(u)+"</font><br><br><b>Γρηγορότερος χρόνος:</b><br><font color=\"blue\">Απροσδιόριστος</font>"+"profileDAILY:"+"<b><big><u>ΚΑΤΑΤΑΞΗ</u></big><br></b><b><br>Πανελλαδικά</b><font color=\"blue\"><br><big><big>#"+d.getDailyRank(u)+"</big></big></font><br><b>Νομός "+newnomos +"</b><font color=\"blue\"><br><big><big>#"+d.getNomosDailyRank(u, d.getNomos(u))+"</big></big></font><br><b>"+perioxi+"</b><br><font color=\"blue\"><big><big>#"+d.getPerioxiDailyRank(u, d.getNomos(u), d.getPerioxi(u))+"</big></big></font>");//TODO
						}
					}else{
						if(!(d.getFastestTime(u)==2147000000)){
							pw.println("profile:"+"<b><big><u>ΚΑΤΑΤΑΞΗ</u></big><br></b><b><br>Πανελλαδικά<t></b><font color=\"blue\"><br><big><big>#"+d.getRank(u)+"</big></big></font><br><b>Νομός "+newnomos+"</b><font color=\"blue\"><br><big><big>#"+d.getNomosRank(u,d.getNomos(u))+"</big></big></font><br><b>"+perioxi+"</b><br><font color=\"blue\"><big><big>#"+d.getPerioxiRank(u,d.getNomos(u),d.getPerioxi(u))+"</big></big></font><br><br>"+"<b>Πόντοι:</b><br><font color=\"blue\">"+d.getPoints(u)+"</font><br><br>"+"<b>Παιχνίδια:</b><br><font color=\"blue\">"+d.getGames(u)+"</font><br><br><b>Γρηγορότερος χρόνος:</b><br><font color=\"blue\">"+ d.getFastestTime(u)+"ms</font><br><br><b>Μέσος χρόνος:</b><br><font color=\"blue\">Απροσδιόριστος</font>"+"profileDAILY:"+"<b><big><u>ΚΑΤΑΤΑΞΗ</u></big><br></b><b><br>Πανελλαδικά</b><font color=\"blue\"><br><big><big>#"+d.getDailyRank(u)+"</big></big></font><br><b>Νομός "+newnomos +"</b><font color=\"blue\"><br><big><big>#"+d.getNomosDailyRank(u, d.getNomos(u))+"</big></big></font><br><b>"+perioxi+"</b><br><font color=\"blue\"><big><big>#"+d.getPerioxiDailyRank(u, d.getNomos(u), d.getPerioxi(u))+"</big></big></font>");//TODO
						}else{
							pw.println("profile:"+"<b><big><u>ΚΑΤΑΤΑΞΗ</u></big><br></b><b><br>Πανελλαδικά</b><font color=\"blue\"><br><big><big>#"+d.getRank(u)+"</big></big></font><br><b>Νομός "+newnomos +"</b><font color=\"blue\"><br><big><big>#"+d.getNomosRank(u,d.getNomos(u))+"</big></big></font><br><b>"+perioxi+"</b><br><font color=\"blue\"><big><big>#"+ d.getPerioxiRank(u,d.getNomos(u),d.getPerioxi(u))+"</big></big></font><br><br>"+"<b>Πόντοι:</b><br><font color=\"blue\">"+d.getPoints(u)+"</font><br><br>"+"<b>Παιχνίδια:</b><br><font color=\"blue\">"+d.getGames(u)+"</font><br><br><b>Γρηγορότερος χρόνος:</b><br><font color=\"blue\">Απροσδιόριστος</font><br><br><b>Μέσος χρόνος:</b><br><font color=\"blue\">Απροσδιόριστος</font>"+"profileDAILY:"+"<b><big><u>ΚΑΤΑΤΑΞΗ</u></big><br></b><b><br>Πανελλαδικά</b><font color=\"blue\"><br><big><big>#"+d.getDailyRank(u)+"</big></big></font><br><b>Νομός "+newnomos +"</b><font color=\"blue\"><br><big><big>#"+d.getNomosDailyRank(u, d.getNomos(u))+"</big></big></font><br><b>"+perioxi+"</b><br><font color=\"blue\"><big><big>#"+d.getPerioxiDailyRank(u, d.getNomos(u), d.getPerioxi(u))+"</big></big></font>");//TODO
						}
					}
				} catch (IOException e) {	
					log.log(Level.SEVERE,e.toString(),e);
					
				}
			}else if(message.contains("friend_delete:")){
				
				String targetFriends = d.getFriends(message.replace("friend_delete:",""));
				if(targetFriends.contains("~%~*"+username)){
					targetFriends = targetFriends.replace("~%~*"+username, "");
				}else if(targetFriends.contains(username+"~%~*")){
					targetFriends = targetFriends.replace(username+"~%~*", "");
				}else{
					targetFriends = targetFriends.replace(username, "");
				}
				d.updateFriends(message.replace("friend_delete:",""),targetFriends);
				
				String friends = d.getFriends(username);
				if(friends.contains("~%~*"+message.replace("friend_delete:",""))){
					friends = friends.replace("~%~*"+message.replace("friend_delete:",""), "");
				}else if(friends.contains(message.replace("friend_delete:","")+"~%~*")){
					friends = friends.replace(message.replace("friend_delete:","")+"~%~*", "");
				}else{
					friends = friends.replace(message.replace("friend_delete:",""), "");
				}
				d.updateFriends(username,friends);

				friends = d.getFriends(username);
			}else if(message.contains("request_replay")){
				try {
					PrintWriter pw = new PrintWriter(previousOpponentSocket.getOutputStream(),true);
					pw.println("replay");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.log(Level.SEVERE,e.toString(),e);
					
				}
			}else if(message.contains("replay_")){
				if(message.equals("replay_accept")){
					try {
						PrintWriter pw = new PrintWriter(previousOpponentSocket.getOutputStream(),true);
						pw.println("replay_accepted");
						new Matchmaking_GameFoundThreads(this,previousOpponent,true);
						break;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						log.log(Level.SEVERE,e.toString(),e);
						
					}
				}else if(message.contains("replay_decline")){
					try {
						PrintWriter pw = new PrintWriter(previousOpponentSocket.getOutputStream(),true);
						pw.println("replay_declined");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						log.log(Level.INFO,e.toString(),e);
						
					}
					PrintWriter pw2;
					try {
						pw2 = new PrintWriter(clientSocket.getOutputStream(),true);
						pw2.println("666");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						log.log(Level.SEVERE,e.toString(),e);
					}
				}else if(message.equals("replay_accepted")){
					break;
				}else if(message.equals("replay_both_accept")){
					boolean found = false;
					for(int i = 0;i<bothReplayPressed.size();i++){
						if(bothReplayPressed.get(i).equalsIgnoreCase(previousOpponent.username)){
							found=true;
						}
					}
					if(found){
						new Matchmaking_GameFoundThreads(this,previousOpponent,true);
						previousOpponent.forceBreak=true;
						break;
					}else{
						bothReplayPressed.add(username);
						break;
					}
				}
			}else if(message.equals("request_top100")){
				try {
					PrintWriter pw = new PrintWriter(clientSocket.getOutputStream(),true);
					pw.println("top100:"+Rankings.top100Formatted);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.log(Level.SEVERE,e.toString(),e);
					
				}
			}else if(message.contains("requestTop100PerioxiDaily---")){
				try {
					PrintWriter pw = new PrintWriter(clientSocket.getOutputStream(),true);
					String nomosSelected =  message.split(":")[0].split("---")[1].replace("1", "").replace("2", "").replace("3", "").replace("4", "").trim();
					String perioxiSelected = message.split(":")[1].trim();
					int indexPerioxi = 0;
					int indexNomos = 0;
					

					for(int i = 0;i< Main.PERIOXES.length;i++){
						for(int j = 0;j<Main.PERIOXES[i].length;j++){
								if(Main.PERIOXES[i][j].equals(perioxiSelected)){
									System.out.println("test1");
									indexPerioxi =  j;
								}
						}
					}

					for(int i =0;i<Main.NOMOI.length;i++){
							if(Main.NOMOI[i].equals(nomosSelected)){
								indexNomos = i;
							}
					}
					
					String top100PerioxiFormatted="";
					for(int i=0;i<100;i++){
						if(i!=99){
							top100PerioxiFormatted+=LiveRankings.areaTop100Daily[indexNomos][indexPerioxi][i][0]+"-$-"+LiveRankings.areaTop100Daily[indexNomos][indexPerioxi][i][1]+"-*-";
						}else{
							top100PerioxiFormatted+=LiveRankings.areaTop100Daily[indexNomos][indexPerioxi][i][0]+"-$-"+LiveRankings.areaTop100Daily[indexNomos][indexPerioxi][i][1];
						}
					}
					pw.println("replyTop100PerioxiDaily:"+top100PerioxiFormatted);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.log(Level.SEVERE,e.toString(),e);
					
				}
			}else if(message.contains("requestTop100Perioxi---")){
				try {
					PrintWriter pw = new PrintWriter(clientSocket.getOutputStream(),true);
					String nomosSelected =  message.split(":")[0].split("---")[1].replace("1", "").replace("2", "").replace("3", "").replace("4", "").trim();
					String perioxiSelected = message.split(":")[1].trim();
					int indexPerioxi = 0;
					int indexNomos = 0;
					

					for(int i = 0;i< Main.PERIOXES.length;i++){
						for(int j = 0;j<Main.PERIOXES[i].length;j++){
								if(Main.PERIOXES[i][j].equals(perioxiSelected)){
									System.out.println("test1");
									indexPerioxi =  j;
								}
						}
					}

					for(int i =0;i<Main.NOMOI.length;i++){
							if(Main.NOMOI[i].equals(nomosSelected)){
								indexNomos = i;
							}
					}
					
					String top100PerioxiFormatted="";
					for(int i=0;i<100;i++){
						if(i!=99){
							top100PerioxiFormatted+=Rankings.areaTop100[indexNomos][indexPerioxi][i][0]+"-$-"+Rankings.areaTop100[indexNomos][indexPerioxi][i][1]+"-*-";
						}else{
							top100PerioxiFormatted+=Rankings.areaTop100[indexNomos][indexPerioxi][i][0]+"-$-"+Rankings.areaTop100[indexNomos][indexPerioxi][i][1];
						}
					}
					pw.println("replyTop100Perioxi:"+top100PerioxiFormatted);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.log(Level.SEVERE,e.toString(),e);
					
				}
			}else if(message.contains("requestTop100NomosDaily---")){
				try {
					PrintWriter pw = new PrintWriter(clientSocket.getOutputStream(),true);
					String nomosSelected = message.split("---")[1].trim();
					int indexNomos = 0;
					for(int i =0;i<Main.NOMOI.length;i++){
							if(Main.NOMOI[i].equals(nomosSelected)){
								indexNomos = i;
							}
					}
					
					String top100NomosFormatted="";
					for(int i=0;i<100;i++){
						if(i!=99){
							top100NomosFormatted+=LiveRankings.nomosTop100Daily[indexNomos][i][0]+"-$-"+LiveRankings.nomosTop100Daily[indexNomos][i][1]+"-$-"+LiveRankings.nomosTop100Daily[indexNomos][i][2]+"-*-";
						}else{
							top100NomosFormatted+=LiveRankings.nomosTop100Daily[indexNomos][i][0]+"-$-"+LiveRankings.nomosTop100Daily[indexNomos][i][1]+"-$-"+LiveRankings.nomosTop100Daily[indexNomos][i][2];
						}
					}
					pw.println("replyTop100NomosDaily:"+top100NomosFormatted);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.log(Level.SEVERE,e.toString(),e);
					
				}
			}else if(message.contains("requestTop100Nomos---")){
				try {
					PrintWriter pw = new PrintWriter(clientSocket.getOutputStream(),true);
					String nomosSelected = message.split("---")[1].trim();
					int indexNomos = 0;
					for(int i =0;i<Main.NOMOI.length;i++){
							if(Main.NOMOI[i].equals(nomosSelected)){
								indexNomos = i;
							}
					}
					
					String top100NomosFormatted="";
					for(int i=0;i<100;i++){
						if(i!=99){
							top100NomosFormatted+=Rankings.nomosTop100[indexNomos][i][0]+"-$-"+Rankings.nomosTop100[indexNomos][i][1]+"-$-"+Rankings.nomosTop100[indexNomos][i][2]+"-*-";
						}else{
							top100NomosFormatted+=Rankings.nomosTop100[indexNomos][i][0]+"-$-"+Rankings.nomosTop100[indexNomos][i][1]+"-$-"+Rankings.nomosTop100[indexNomos][i][2];
						}
					}
					pw.println("replyTop100Nomos:"+top100NomosFormatted);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.log(Level.SEVERE,e.toString(),e);
					
				}
			}else if(message.equals("request_top100Daily")){
				try {
					PrintWriter pw = new PrintWriter(clientSocket.getOutputStream(),true);
					pw.println("top100Daily:"+LiveRankings.top100DailyFormatted);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.log(Level.SEVERE,e.toString(),e);
					
				}
			}else if(message.equals("request_top100perioxes")){
				try {
					PrintWriter pw = new PrintWriter(clientSocket.getOutputStream(),true);
					boolean found=false;
					int index = 0;
					for(int i =0;i<LiveRankings.perioxesTemp.length;i++){
						//System.out.println(perioxi + " equals " + LiveRankings.perioxesTemp[i]+" = "+perioxi.equalsIgnoreCase(LiveRankings.perioxesTemp[i]));
						if(perioxi.equalsIgnoreCase(LiveRankings.perioxesTemp[i])){
							if(i<100){
								found=true;
								break;
							}else{
								index = i;
								break;
							}
						}
					}
					if(found){
						pw.println("top100perioxes:"+LiveRankings.top100PerioxesTodayFormatted);
					}else{
						pw.println("top100perioxes:"+LiveRankings.top100PerioxesTodayFormatted+"-*-"+index+"666"+LiveRankings.revengeTodayPerioxes[index]);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.log(Level.SEVERE,e.toString(),e);
					
				}
			}
			else if(message.equals("request_top10nomoi")){
				try {
					PrintWriter pw = new PrintWriter(clientSocket.getOutputStream(),true);
					boolean found = false;
					int index = 0;
					String nomosLowercase = "";
					for(int i =0;i<Main.NOMOILOWERCASE.length;i++){
						if(nomos.equalsIgnoreCase(Main.NOMOI[i])){
							nomosLowercase = Main.NOMOILOWERCASE[i];
						}
					}
					for(int i =0;i<LiveRankings.nomoiTemp.length;i++){
						//System.out.println(perioxi + " equals " + LiveRankings.perioxesTemp[i]+" = "+perioxi.equalsIgnoreCase(LiveRankings.perioxesTemp[i]));
						if(nomosLowercase.equalsIgnoreCase(LiveRankings.nomoiTemp[i])){
							if(i<10){
								found=true;
								break;
							}else{
								index = i;
								break;
							}
						}
					}
					if(found){
						pw.println("top10nomoi:"+LiveRankings.top10NomoiTodayFormatted);
					}else{
						pw.println("top10nomoi:"+LiveRankings.top10NomoiTodayFormatted+"-*-"+index+"666"+LiveRankings.revengeTodayNomoi[index]);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.log(Level.SEVERE,e.toString(),e);
					
				}
			}else if(message.contains("exists_player:")){
				String playerRequested = message.replace("exists_player:", "");
				try {
					PrintWriter pw = new PrintWriter(clientSocket.getOutputStream(),true);
					if(d.existsUsername(playerRequested)){
						pw.println("exists_player:"+playerRequested+":true");
					}else{
						pw.println("exists_player:"+playerRequested+":false");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.log(Level.SEVERE,e.toString(),e);
					
				}
			}else if(message.equals("request_generaltop100perioxes")){
				try {
					PrintWriter pw = new PrintWriter(clientSocket.getOutputStream(),true);
					boolean found = false;
					int index = 0;
					for(int i =0;i<Rankings.perioxesTemp.length;i++){
						//System.out.println(nomos + " equals " + Rankings.nomoiTemp[i]+" = "+nomos.equalsIgnoreCase(Rankings.nomoiTemp[i]));
						if(perioxi.equalsIgnoreCase(Rankings.perioxesTemp[i])){
							if(i<100){
								found=true;
								break;
							}else{
								index = i;
								break;
							}
						}
					}
					if(found){
						pw.println("generaltop100perioxes:"+Rankings.top100PerioxesFormatted);
					}else{
						pw.println("generaltop100perioxes:"+Rankings.top100PerioxesFormatted+"-*-"+index+"666"+Rankings.topPerioxes[index]);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.log(Level.SEVERE,e.toString(),e);
					
				}
			}
			else if(message.equals("request_generaltop10nomoi")){
				try {
					PrintWriter pw = new PrintWriter(clientSocket.getOutputStream(),true);
					boolean found = false;
					int index = 0;
					String nomosLowercase = "";
					for(int i =0;i<Main.NOMOILOWERCASE.length;i++){
						if(nomos.equalsIgnoreCase(Main.NOMOI[i])){
							nomosLowercase = Main.NOMOILOWERCASE[i];
						}
					}
					for(int i =0;i<Rankings.nomoiTemp.length;i++){
						//System.out.println(nomos + " equals " + Rankings.nomoiTemp[i]+" = "+nomos.equalsIgnoreCase(Rankings.nomoiTemp[i]));
						if(nomosLowercase.equalsIgnoreCase(Rankings.nomoiTemp[i])){
							if(i<10){
								found=true;
								break;
							}else{
								index = i;
								break;
							}
						}
					}
					if(found){
						pw.println("generaltop10nomoi:"+Rankings.top10NomoiFormatted);
					}else{
						pw.println("generaltop10nomoi:"+Rankings.top10NomoiFormatted+"-*-"+index+"666"+Rankings.topNomoi[index]);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.log(Level.SEVERE,e.toString(),e);
					
				}
			}
		}

	}


	private String getFinalFriends(String f) {
		String friendsArray[];
		if(!f.contains("~%~*")){
			friendsArray = new String[1];
			friendsArray[0] = f;
		}else{
			friendsArray=f.split(Pattern.quote("~%~*"));
		}
		int pointsArray[] = new int[friendsArray.length];
		boolean loggedArray[] = new boolean[friendsArray.length];
		String finalString="";
		loggedArray=Login.isLoggedIn(friendsArray);
		
		for(int i =0;i<friendsArray.length;i++){

			pointsArray[i]=d.getPoints(friendsArray[i]);
			if(i==0){
				finalString+=friendsArray[0]+"=========="+pointsArray[0]+"=========="+loggedArray[0];
			}else{
				finalString+="~%~*"+friendsArray[i]+"=========="+pointsArray[i]+"=========="+loggedArray[i];
			}
		}
		return finalString;
	}



}
