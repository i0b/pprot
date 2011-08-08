package beci.pprot.server;

import java.io.FileInputStream;
import java.util.Properties;

public class Configuration {
	private final static String CONFIGFILE = "configuration.xml";

	private static Properties properties;
	
	private static Configuration instance = null;
	
	public static Configuration getInstance(){
		if (instance == null)
			instance = new Configuration();
		
		return instance;
	}
	
	private Configuration(){
		properties = new Properties();
		FileInputStream fis;
		try {
			fis = new FileInputStream(CONFIGFILE);
			properties.loadFromXML(fis);
			fis.close();
		} catch (Exception e) {
			properties = System.getProperties();
			System.out.println("Properties could not be loaded from " + CONFIGFILE + ".");
			e.printStackTrace();
		}
	}
	
	public Properties getProperties(){
		return properties;
	}
	
	/*private void saveProperties(){
		try {
			FileOutputStream fos = new FileOutputStream(CONFIGFILE);
			properties.storeToXML(fos, "ExamnData Configuration", "UTF-8");
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
}
