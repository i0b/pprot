package beci.pprot.server;

import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;

import beci.pprot.client.MailAccessService;
import beci.pprot.shared.MailData;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class MailAccessServiceImpl extends RemoteServiceServlet implements
		MailAccessService {
	private static Properties properties;
	private static Message[] messages = null;
	private static Folder mailFolder = null;
	private static Session session = null;
	private static Store store = null;

	public MailAccessServiceImpl() {
		properties = Configuration.getInstance().getProperties();
	}

	private Boolean openConnection() {
		if (store != null && store.isConnected() && mailFolder != null && mailFolder.isOpen())
			return true;
		
		try {
			String host = properties.getProperty("mail.imap.host");
			String username = properties.getProperty("mail.imap.user");
			String password = properties.getProperty("mail.imap.password");

			// Get session
			session = Session.getDefaultInstance(properties, null);

			System.out.println("IMAP session initialized.");
			// Get the store
			store = session.getStore("imap");

			System.out.println("Connecting to " + host + "..");
			store.connect(host, username, password);
			System.out.println("Connected.");

			// Get folder
			mailFolder = store.getFolder(properties
					.getProperty("mail.imap.folder"));
			mailFolder.open(Folder.READ_ONLY);
		} catch (Exception e) {
			System.out.println("Connection to mail server Failed.");
			System.out.println(e.toString());
			return false;
		}

		try {
			messages = mailFolder.getMessages();
			return true;

		} catch (Exception e) {
			System.out.println("Could not update messages from "
					+ properties.getProperty("mail.imap.folder"));
			System.out.println(e.toString());
			return false;
		}
	}

	private Boolean closeConnection() {
		try {
			if (mailFolder != null && mailFolder.isOpen())
				mailFolder.close(false);
			if (store != null && store.isConnected())
				store.close();

			return true;
		} catch (Exception e) {
			System.out
					.println("Could not close the connection to mail server.");
			System.out.println(e.toString());

			return false;
		}
	}

	public MailData[] getMailData() {
		if (!openConnection())
			return null;

		MailData[] mailData = null;

		try {
			mailData = new MailData[messages.length];

			for (int i = 0, n = messages.length; i < n; i++) {
				ArrayList<String> attachment = new ArrayList<String>();

				if (messages[i].isMimeType("multipart/*")) {
					Multipart multipart = (Multipart) messages[i].getContent();

					for (int j = 1; j < multipart.getCount(); j++) {
						Part part = multipart.getBodyPart(j);
						String disp = part.getDisposition();

						if (disp != null
								&& disp.equalsIgnoreCase(Part.ATTACHMENT)
								&& part.isMimeType("application/pdf"))
							attachment.add(part.getFileName());
					}
				}

				mailData[i] = new MailData(messages[i].getMessageNumber(),
						messages[i].getSentDate(), String.valueOf(messages[i]
								.getFrom()[0]), messages[i].getSubject(),
						attachment);
			}
		} catch (Exception e) {
			System.out.println("Could not read the data from messages.");
			System.out.println(e.toString());
		}

		closeConnection();

		return mailData;
	}
	
	public synchronized String getMailBody(int messageNumber) {
		// Problem right now: the gui can open different messages shortly one after an other
		// 					  which causes trouble because the 'getMailBody' method is still
		// 					  finding the old message to display.
		if (!openConnection())
			return new String();

		String body = new String();

		try {
			for (int i = 0, n = messages.length; i < n; i++) {
				if (messages[i].getMessageNumber() == messageNumber) {
					if (messages[i].isMimeType("multipart/*")) {
						Part partZero = ((Multipart) messages[i].getContent())
								.getBodyPart(0);

						if (partZero.isMimeType("text/*"))
							body = (String)partZero.getContent();
					} else
						body = (String)messages[i].getContent();
				}
			}
		} catch (Exception e) {
			// There are way to many exceptions thrown - 
			System.out.println("Could not read the body from mail number "
					+ String.valueOf(messageNumber) + ".");
			System.out.println(e.toString());
		}

		closeConnection();

		return body.replaceAll("\n", "<br />");
	}
}
