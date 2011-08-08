package beci.pprot.server;

import java.util.Properties;

import beci.pprot.client.PrintDataService;
import beci.pprot.shared.PrintData;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class PrintDataServiceImpl extends RemoteServiceServlet implements PrintDataService {
	private static String PRINTCOMMAND = new String();
	
	public PrintDataServiceImpl() {
		loadPrintCommand();
	}
	
	private static void loadPrintCommand() {
		Properties properties = Configuration.getInstance().getProperties();
		
		if (properties.getProperty("print.command") != null)
			PRINTCOMMAND = properties.getProperty("printCommand");
	}
	
	public boolean printFiles(PrintData data) {
		//TODO generate a proper parameter for the PRINTCOMMAND
		String fileList = new String();
		
		for (int i=0; i<data.getFiles().length; i++)
			fileList += " " + data.getFiles()[i];
		
		System.out.println("Executing: " + PRINTCOMMAND + fileList);
		
		try {
			Process process = Runtime.getRuntime().exec(PRINTCOMMAND + fileList);
			int exitValue = process.waitFor();
			
			if (exitValue == 0)
				return true;
			else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
