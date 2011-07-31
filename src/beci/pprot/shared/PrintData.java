package beci.pprot.shared;

import java.io.Serializable;

public class PrintData implements Serializable{
	private String[] files;
	//private String emailAddress; TODO use this in the advanced version

	public PrintData(){}
	
	public PrintData(String[] files) {
		this.files = files;
	}
	
	public String[] getFiles() {
		return files;
	}

	public void setFiles(String[] files) {
		this.files = files;
	}
}
