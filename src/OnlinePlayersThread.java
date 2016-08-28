import java.util.logging.Level;
import java.util.logging.Logger;


public class OnlinePlayersThread extends Thread{
	static final Logger log = Logger.getLogger( OnlinePlayersThread.class.getName() );

	public void run(){
		while(!isInterrupted()){
			try {
				sleep(1000);		//1 sec
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				log.log(Level.SEVERE,e.toString(),e);
			}
			ServerGui.setOnline(Matchmaking.onlinePlayers.size());
		}
	}
}
