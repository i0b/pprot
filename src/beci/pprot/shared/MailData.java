package beci.pprot.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class MailData implements Serializable {
	private int messageNumber;
	private Date composed;
	private String sender;
	private String subject;
	private ArrayList<String> attachment;
	
	public MailData() {}
	
	public MailData(int messageNumber, Date composed, String sender, String subject, ArrayList<String> attachment) {
		this.messageNumber = messageNumber;
		this.composed = composed;
		this.sender = sender;
		this.subject = subject;
		this.attachment = attachment;
	}

	public int getMessageNumber() {
		return messageNumber;
	}

	public void setMessageNumber(int messageNumber) {
		this.messageNumber = messageNumber;
	}
	
	public Date getComposed() {
		return composed;
	}

	public void setComposed(Date composed) {
		this.composed = composed;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public ArrayList<String> getAttachment() {
		return attachment;
	}

	public void setAttachment(ArrayList<String> attachment) {
		this.attachment = attachment;
	}

}