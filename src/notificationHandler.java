import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;


public class notificationHandler extends Thread{
	static final Logger log = Logger.getLogger( notificationHandler.class.getName() );
	String adder;
	String target;
	String notificationType;
	public static volatile Vector<String> allFRequests = new Vector<String>();
	public static volatile Vector<String> allChallenges = new Vector<String>();

	public notificationHandler(String adder, String target,String notificationType) {
		//System.out.println("notification handler added");
		this.adder=adder;
		this.target=target;
		this.notificationType=notificationType;
		if(notificationType.equals("frequest")){
			boolean f = false;
			for(int i=0;i<allFRequests.size();i++){
				if(((String)allFRequests.get(i)).equals(adder+"----->>>>>"+target)){
					f=true;
				}
			}
			if(!f){
				allFRequests.add(adder+"----->>>>>"+target);
				start();
			}
		}else if(notificationType.equals("challenge")){
			boolean f = false;
			for(int i=0;i<allChallenges.size();i++){
				if(((String)allChallenges.get(i)).equals(adder+"----->>>>>"+target)){
					f=true;
				}
			}
			if(!f){
				allChallenges.add(adder+"----->>>>>"+target);
				start();
			}
		}
	}
	
	public void run(){
		if(notificationType.equals("frequest")){
			while(true){
				if(Login.isLoggedIn(target)){
					//System.out.println("notification target online");
					Socket s = Login.getNSocketByUser(target);
					try {
						if(!s.isClosed()){
							allFRequests.remove(adder+"----->>>>>"+target);
							PrintWriter pw = new PrintWriter(s.getOutputStream(),true);
							pw.println("NOTIFY:FREQUEST:"+adder);
							break;
						}else{
							continue;
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						log.log(Level.SEVERE,e.toString(),e);
						
					}
				}else{
					try {
						sleep(30000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						log.log(Level.SEVERE,e.toString(),e);
						
					}
				}
				
			}
		}else if(notificationType.equals("challenge")){
			while(true){
				if(Login.isLoggedIn(target)){
					//System.out.println("notification target online");
					Socket s = Login.getNSocketByUser(target);
					try {
						if(!s.isClosed()){
							allChallenges.remove(adder+"----->>>>>"+target);
							PrintWriter pw = new PrintWriter(s.getOutputStream(),true);
							pw.println("NOTIFY:CHALLENGE:"+adder);
							System.out.println("NOTIFY:CHALLENGE:"+adder);
							break;
						}else{
							continue;
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						log.log(Level.SEVERE,e.toString(),e);
						
					}
					
					
				}else{
					try {
						sleep(30000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						log.log(Level.SEVERE,e.toString(),e);
						
					}
				}
				
			}
		}
			
	}
}
