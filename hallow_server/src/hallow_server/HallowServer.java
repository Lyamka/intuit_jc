package hallow_server;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.session.HashSessionIdManager;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
 
public class HallowServer implements Runnable {
	private AtomicInteger handleCount;
	private Logger log;
	
    public HallowServer() throws Exception {
    	handleCount = new AtomicInteger();
    	log = Logger.getLogger(this.getClass().getName());
    	log.setLevel(Level.INFO);

    	Thread logThread = new Thread(this);
    	logThread.start();
    	
    	Server server = new Server(8080);
        
        HashSessionIdManager idManager = new HashSessionIdManager();
        server.setSessionIdManager(idManager);
        
        HashSessionManager manager = new HashSessionManager();
        SessionHandler sessions = new SessionHandler(manager);
        
        ContextHandlerCollection chc = new ContextHandlerCollection();
        server.setHandler(chc);
        
        // Динамический контент
        ServletContextHandler root = new ServletContextHandler(chc, "/");
        root.setSessionHandler(sessions);
        root.addServlet(new ServletHolder(new HallowServlet(handleCount)), "/");
         
        // Статика
        String webDir  = HallowServer.class.getClassLoader().getResource("static").getPath();
        
        ContextHandler stat = new ContextHandler(chc, "/static");
        ResourceHandler staticFilesHandler = new ResourceHandler();
        staticFilesHandler.setResourceBase(webDir);
        staticFilesHandler.setDirectoriesListed(true);        
        staticFilesHandler.setWelcomeFiles(new String[]{ "index_static.html", "index.html" });
        
        stat.setHandler(staticFilesHandler);
        System.out.println(staticFilesHandler.getResourceBase());
        
        server.start();
        server.join();
    }
    
    public static void main(String[] args) throws Exception {
        new HallowServer();
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
