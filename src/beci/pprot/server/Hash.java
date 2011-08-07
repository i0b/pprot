package beci.pprot.server;

import java.security.MessageDigest;

public class Hash {
	public static String generateHash(String s){
		try{
			byte[] pw = s.getBytes("UTF-8");
			byte[] hash = MessageDigest.getInstance("MD5").digest(pw);
			
			s = "";
			for(byte b:hash){
				s += (char)b;
			}
			return s;
		}
		catch(Exception e){
			return null;
		}
	}
}
