package hallow_server;


public class PlayerSession {
	public static final int UNINITIALISED = 0;
	public static final int LOGGING_IN = 1;
	public static final int NO_SUCH_USER = 2;
	public static final int LOGGED_IN = 10;
	
	private int currentState = PlayerSession.UNINITIALISED;
	private int sessionId = 0;
	private int playerId = 0;
	private String playerName = "";
	
	public PlayerSession(int sessionId) {
		this.sessionId	 = sessionId;
	}
	
	public synchronized void setPlayerName(String playerName) {
		this.playerName = playerName;
		this.playerId = 0;
		this.currentState = playerName.isEmpty() ? PlayerSession.UNINITIALISED : PlayerSession.LOGGING_IN;
	}
	
	public int getCurrentState() {
		return currentState;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(playerName)
		.append(" (id: ")
		.append(playerId)
		.append(")");
				
		return sb.toString();
	}

	public String getPlayerName() {
		return playerName;
	}

	public synchronized void setUserNotFound() {
		this.currentState = PlayerSession.NO_SUCH_USER;
	}

	public synchronized void setUserFound(Integer playerId) {
		this.playerId = playerId;
		this.currentState = PlayerSession.LOGGED_IN;
	}
}
