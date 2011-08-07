package beci.pprot.model;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseWriter {
	public boolean writeUser(User u){
		try{
			Connection c = ConnectionManager.getConnection();
			Statement s = c.createStatement();
			s.exe
			
			
			
		}
		catch(Exception e){
			return false;
		}
	}
}
