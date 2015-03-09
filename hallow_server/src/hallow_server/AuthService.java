package hallow_server;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.jetty.util.ConcurrentArrayQueue;

public class AuthService implements Runnable {

	private Map<String, Integer> players;
	private Logger log;
	private ConcurrentArrayQueue<PlayerSession> authQueue;
	
	private void createPlayers() {
		players = new HashMap<String, Integer>();
		players.put("Lyamka", 1);
		players.put("Vanesz", 2);
		players.put("Igoryan", 3);
		players.put("Slaffko", 4);
	}
	
	public AuthService(Logger log) {
		authQueue = new ConcurrentArrayQueue<PlayerSession>();
		createPlayers();
		this.log = log;
	}
	
	public synchronized void authPlayerSession(PlayerSession ps) {
		authQueue.offer(ps);
	}
	
	public void Login(PlayerSession ps) {
		Integer playerId = findPlayerIdByName(ps.getPlayerName());
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
		PlayerSession ps = null;
		
		while (true) {
			if (authQueue.size() == 0) {
				try {
					Thread.sleep(1000);
					continue;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			while ((ps = authQueue.poll()) != null) {
				if (ps.getCurrentState() == PlayerSession.LOGGING_IN) {
					Login(ps);
				}
			}
		}
	}
}
