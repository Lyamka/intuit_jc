package hallow_server;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class AuthService implements Runnable {

	private Map<String, Integer> players;
	private Logger log;
	
	private void createPlayers() {
		players = new HashMap<String, Integer>();
		players.put("Lyamka", 1);
		players.put("Vanesz", 2);
		players.put("Igoryan", 3);
		players.put("Slaffko", 4);
	}
	
	private List<PlayerSession> authQueue;
	
	public AuthService(Logger log) {
		authQueue = Collections.synchronizedList(new LinkedList<PlayerSession>());
		createPlayers();
		this.log = log;
	}
	
	public void authPlayerSession(PlayerSession ps) {
		authQueue.add(ps);
	}
	
	public void Login(PlayerSession ps) {
		Integer playerId = findPlayerIdByName(ps.getPlayerName());
		
		log.info("Sleeping in auth thread");
		/*try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		if (playerId == null) {
			ps.setUserNotFound();
			return;
		}
		
		ps.setUserFound(playerId);
	}
	
	private Integer findPlayerIdByName(String name) {
		return players.get(name);
	}
	
	@Override
	public void run() {
		while (true) {
			if (authQueue.size() == 0) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				for (PlayerSession ps : authQueue) {
					if (ps.getCurrentState() == PlayerSession.LOGGING_IN) {
						Login(ps);
					}
					authQueue.remove(ps);
					System.out.println(authQueue.size());
					System.out.println("authq is empty : " + authQueue.isEmpty());
				}
			}
		}
	}
}
