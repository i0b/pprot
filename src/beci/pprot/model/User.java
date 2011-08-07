package beci.pprot.model;

public class User {
	private String username;
	//TODO: Replace flags with actual rights
	private boolean flag1;
	private boolean flag2;
	
	public User(String username){
		this.username = username;
		this.flag1 = false;
		this.flag2 = false;
	}
	
	public boolean hasFlag1(){
		return this.flag1;
	}
	
	public boolean hasFlag2(){
		return this.flag2;
	}
	
	public String getUsername(){
		return this.username;
	}
}
