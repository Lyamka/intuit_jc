package hallow_server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Frontend extends HttpServlet implements Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5294817680088593615L;
	private AtomicInteger handleCount;
	private AtomicInteger idGenerator;
	private Logger log;

	private Map<Integer, PlayerSession> sessions;
	private AuthService auth;
	
	public Frontend(Logger log) {
		super();
		
		handleCount = new AtomicInteger();
		idGenerator = new AtomicInteger();
		this.log = log;
		sessions = new HashMap<Integer, PlayerSession>();
		auth = new AuthService(log);
		new Thread(auth).start();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {

		if (req.getRequestURI().contains("favicon.ico")) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        
		handleCount.getAndIncrement();

		HttpSession session = req.getSession();
		Integer sessionId = (Integer) session.getAttribute("sessionId");
		if (sessionId == null) {
			sessionId = idGenerator.getAndIncrement();
			session.setAttribute("sessionId", sessionId);
		}
		
		PlayerSession ps = sessions.get(sessionId);
		if (ps == null) {
			ps = new PlayerSession(sessionId);
		}
		
		handlePlayerSession(ps, response);
	}
	
	private void handlePlayerSession(PlayerSession ps, HttpServletResponse response) throws IOException {
		switch (ps.getCurrentState()) {
		case PlayerSession.LOGGED_IN : {
			response.getWriter().println(HWPageGenerator.getStatusPage(ps));
			break;
		}
		case PlayerSession.LOGGING_IN : {
			response.getWriter().println(HWPageGenerator.getStatusPage(ps));
			break;
		}
		case PlayerSession.NO_SUCH_USER : {
			response.getWriter().println(HWPageGenerator.getLoginPage(ps));
			break;
		}
		case PlayerSession.UNINITIALISED : {
			response.getWriter().println(HWPageGenerator.getLoginPage(ps));
			break;
		}
		}
	}


	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        
        handleCount.getAndIncrement();
        
		HttpSession session = req.getSession();
		Integer sessionId = (Integer) session.getAttribute("sessionId");
		if (sessionId == null) {
			sessionId = idGenerator.getAndIncrement();
		}
		
		PlayerSession ps = sessions.get(sessionId);
		if (ps == null) {
			ps = new PlayerSession(sessionId);
		}
		
		if (ps.getCurrentState() == PlayerSession.UNINITIALISED) {
			String username = (String) req.getParameter("username");
			if (username != null & !username.isEmpty()) {
				ps.setPlayerName(username);
				auth.authPlayerSession(ps);
			}
		}

		handlePlayerSession(ps, response);
	}

	@Override
	public void run() {
		while (true) {
			log.info(handleCount.toString());
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				return;
			}
		}
	}
}
