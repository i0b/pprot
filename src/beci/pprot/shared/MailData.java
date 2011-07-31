package beci.pprot.shared;

import java.io.Serializable;
import java.util.Date;

public class MailData implements Serializable {
	private Date composed;
	private String sender;
	private String subject;
	private String attachment;
	
	public MailData() {}
	
	public MailData(Date composed, String sender, String subject, String attachment) {
		this.composed = composed;
		this.sender = sender;
		this.subject = subject;
		this.attachment = attachment;
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

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

}