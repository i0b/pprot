package beci.pprot.model;

import java.io.IOException;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.SQLException;

import beci.pprot.server.Hash;

public class UserManagment {
	public boolean login(String username, String password) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, IOException{
		password = Hash.generateHash(password);
		Connection c = ConnectionManager.getConnection();
		Statement s = c.createStatement()
	}
}
