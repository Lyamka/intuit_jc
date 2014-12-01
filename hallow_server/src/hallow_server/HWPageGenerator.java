package hallow_server;

public class HWPageGenerator {
	// Говнохтмл генератор
	public static String generatePage(String userID) {
		return "<html>"
    			+ "<body onload='setInterval(function() "
    			+ "{ window.location.reload(); }, 1000);'>"
    			+ "<h1>Hallow world</h1>"
    			+ "<p>Hallow user id #" + userID + "</p>"
    			+ "</body></html>";
	}
}
