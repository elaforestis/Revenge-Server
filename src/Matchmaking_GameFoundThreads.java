import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Matchmaking_GameFoundThreads extends Thread{
	static final Logger log = Logger.getLogger( Matchmaking_GameFoundThreads.class.getName() );
	Player p1 = null;
	Player p2 = null;
	boolean bypass = false;;
	
	public Matchmaking_GameFoundThreads(Player p1, Player p2,boolean bypass) {
		this.p1 = p1;
		this.p2 = p2;
		this.bypass = bypass;
		start();
	}
	

	public void run(){
		if(!bypass){
			if(p1.clientSocket.isClosed()){
				Matchmaking.queuedPlayers.add(p2);
				return;
			}

			if(p2.clientSocket.isClosed()){
				Matchmaking.queuedPlayers.add(p1);
				return;
			}
			
			PrintWriter pw1 = null;
			PrintWriter pw2 = null;
			try{
				pw1 = new PrintWriter(p1.clientSocket.getOutputStream(),true);
				pw1.println("precheck");
			}catch(Exception e){
				log.log(Level.SEVERE,e.toString(),e);
				
				Matchmaking.queuedPlayers.add(p2);
			}
			try{
				pw2 = new PrintWriter(p2.clientSocket.getOutputStream(),true);
				pw2.println("precheck");
			}catch(Exception e){
				log.log(Level.SEVERE,e.toString(),e);
				
				Matchmaking.queuedPlayers.add(p1);
			}
			
			try{
				if(!pw1.checkError() && !pw2.checkError()){
					pw1.println("game_queue_found:"+p2.username+" ("+p2.nomos+"-"+p2.perioxi+")" + "-$-"+p1.avatar+""+p2.avatar);
					pw2.println("game_queue_found:"+p1.username+" ("+p1.nomos+"-"+p1.perioxi+")" + "-$-"+p2.avatar+""+p1.avatar);
				}
			}catch(Exception e){
				log.log(Level.SEVERE,e.toString(),e);
				
			}
			p1.forceBreak = true;
			p2.forceBreak = true;
		}
		new InGame(p1,p2);

	}
}

