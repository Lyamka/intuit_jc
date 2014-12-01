package hallow_server;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class HallowServlet extends HttpServlet {

	private AtomicInteger idGenerator;
	private AtomicInteger handleCount;
	/**
	 * 
	 */
	private static final long serialVersionUID = -3789749576974484031L;

	public HallowServlet(AtomicInteger handleCount) {
		super();
		
		this.handleCount = handleCount;
		idGenerator = new AtomicInteger();
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {

		//System.out.println(req.getRequestURI());
		handleCount.getAndIncrement();
		
		HttpSession session = req.getSession();
		String userId = (String) session.getAttribute("userId");
		if (userId == null) {
			userId = Integer.toString(idGenerator.getAndIncrement());
			session.setAttribute("userId", userId);
		}
		
		response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        
        response.getWriter().println(HWPageGenerator.generatePage(userId));
	}

	
}
