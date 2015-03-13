package hallow_server;

import java.util.logging.Level;
import java.util.logging.Logger;

import messageSystem.MessageManager;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.session.HashSessionIdManager;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
 
public class HallowServer {
	private Logger log;
	static MessageManager messageManager = new MessageManager();
	
    public HallowServer() throws Exception {
    	log = Logger.getLogger(this.getClass().getName());
    	log.setLevel(Level.INFO);

    	Frontend frontend = new Frontend(log);
    	(new Thread(frontend)).start();
    	
    	Server server = new Server(8080);
        
    	ContextHandlerCollection chc = new ContextHandlerCollection();
        server.setHandler(chc);
    	
        HashSessionIdManager idManager = new HashSessionIdManager();
        server.setSessionIdManager(idManager);
        
        HashSessionManager manager = new HashSessionManager();
        SessionHandler sessions = new SessionHandler(manager);
        
        // Динамический контент
        ServletContextHandler root = new ServletContextHandler(chc, "/");
        root.setSessionHandler(sessions);
        root.addServlet(new ServletHolder(frontend), "/");
         
        // Статика
        String webDir  = HallowServer.class.getClassLoader().getResource("static").getPath();
        
        ContextHandler stat = new ContextHandler(chc, "/static");
        ResourceHandler staticFilesHandler = new ResourceHandler();
        staticFilesHandler.setResourceBase(webDir);
        staticFilesHandler.setDirectoriesListed(true);        
        staticFilesHandler.setWelcomeFiles(new String[]{ "index_static.html", "index.html" });
        
        stat.setHandler(staticFilesHandler);
        
        server.start();
        server.join();
    }
    
    public static void main(String[] args) throws Exception {
        new HallowServer();
    }
}
