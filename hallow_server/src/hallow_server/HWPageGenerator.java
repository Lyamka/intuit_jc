package hallow_server;

public class HWPageGenerator {
	// Говнохтмл генератор
	
	public static String getLoginPage(PlayerSession ps) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html><body><form method='POST' action = '/'>");
		
		switch (ps.getCurrentState()) {
		case PlayerSession.NO_SUCH_USER : {
			sb.append("<p>No such user ").append(ps.getPlayerName()).append("</p>");
			break;
		}
		
		case PlayerSession.UNINITIALISED : {
			sb.append("<p>Enter your name</p>");
		}
		}
		
		sb.append("<input name = 'username' type = text>")
		.append("<input type = submit>")
		.append("</form></body></html>");
		
		return sb.toString();
	}
	
	public static String getStatusPage(PlayerSession ps) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html><body onload='setInterval(function() { window.location.reload(); }, 1000);'>")
		.append("<h1>Hallow world</h1>");
		
		switch (ps.getCurrentState()) {
		case PlayerSession.LOGGED_IN : {
			sb.append("<p>Hallow user " + ps + "</p>");
			break;
		}
		case PlayerSession.LOGGING_IN : {
			sb.append("You entered username <b>").append(ps.getPlayerName()).append("</b>. ")
			.append("Please wait while authentificating");
			break;
		}
		}
		sb.append("</body></html>");
		return sb.toString();
	}
}
