import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;



public class Matchmaking extends Thread{
	static final Logger log = Logger.getLogger( Matchmaking.class.getName() );
	public static volatile Hashtable<String, Player> onlinePlayers = new Hashtable<String, Player>();
	public static volatile Queue<Player> queuedPlayers = new LinkedList<Player>();
	

	@Override
	public void run() {
		while(!isInterrupted()){
			if(queuedPlayers.size() >= 2){
				new Matchmaking_GameFoundThreads(queuedPlayers.remove(),queuedPlayers.remove(),false);
			}
		}
	}
}
