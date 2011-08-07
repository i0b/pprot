package beci.pprot.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
	private final static String CONFIGFILE = "configuration.xml";
	private static Properties properties = null;
	
	
	private static void loadProperties() throws IOException {
		properties = new Properties();
		FileInputStream fis;
		fis = new FileInputStream(CONFIGFILE);
		properties.loadFromXML(fis);
		fis.close();
	}
	
	public static Connection getConnection() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
		loadProperties();
		
		java.sql.Connection conn = null;
		String userName = properties.getProperty("model.connection.user", "root");
        String password = properties.getProperty("model.connection.password", "");
        String url = properties.getProperty("model.connection.url", "jdbc:mysql://localhost/test");
        Class.forName ("com.mysql.jdbc.Driver").newInstance ();
        conn = DriverManager.getConnection (url, userName, password);
        System.out.println ("Database connection established");
        return conn;
	}
}
