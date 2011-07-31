package beci.pprot.server;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * This class will be initialized on startup of the server.
 * Note that a listener has to be in the web.xml
 *
 */
public class StartUp implements ServletContextListener {
	public void contextDestroyed(ServletContextEvent arg0) {
	}
	
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("Initializing 'UpdateExaminationFile'...");
		new UpdateExaminationFile();
	}
	
}
