import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;


public class KillDced extends Thread{
	static final Logger log = Logger.getLogger( KillDced.class.getName() );

	public void run(){
		while(!isInterrupted()){
			try {
				sleep(600000);		//final = 10 mins
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				log.log(Level.SEVERE,e.toString(),e);
			}
			Enumeration<String> keys;
			keys=Matchmaking.onlinePlayers.keys();
			while(keys.hasMoreElements()){
				String currKey = (String)keys.nextElement();
				Player p = Matchmaking.onlinePlayers.get(currKey);
				PrintWriter pw;
				try{
					if(!p.clientSocket.isClosed()){
						pw = new PrintWriter(p.clientSocket.getOutputStream(),true);
						pw.println("x");
/*						if(pw.checkError()){
							System.out.println("Disconnected client!");
							Matchmaking.onlinePlayers.remove((String)currKey);
							continue;
						}*/
					}/*else{
						Matchmaking.onlinePlayers.remove((String)currKey);
						continue;
					}
*/
				} catch (IOException ex) {
					log.log(Level.SEVERE,ex.toString(),ex);
				}
				
				
			}
		}
	}
}
