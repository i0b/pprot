package beci.pprot.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;

import beci.pprot.client.MailAccessService;
import beci.pprot.client.PrintDataService;
import beci.pprot.shared.MailData;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class MailAccessServiceImpl extends RemoteServiceServlet implements
		MailAccessService {
	private Properties properties;
	final static String CONFIGFILE = "configuration.xml";

	public MailAccessServiceImpl() {
		try {
			loadProperties();
		} catch (IOException e) {
			properties = System.getProperties();
			e.printStackTrace();
		}
	}

	private void loadProperties() throws IOException {
		properties = new Properties();
		FileInputStream fis;
		fis = new FileInputStream(CONFIGFILE);
		properties.loadFromXML(fis);
	}

	public MailData[] getMailData() {
		MailData[] mailData = null;

		try {
			String host = properties.getProperty("mail.imap.host");
			String username = properties.getProperty("mail.imap.user");
			String password = properties.getProperty("mail.imap.password");

			// Get session
			Session session = Session.getDefaultInstance(properties, null);

			System.out.println("IMAP session initialized.");
			// Get the store
			Store store = session.getStore("imap");

			System.out.println("Connecting to " + host + "..");
			store.connect(host, username, password);
			System.out.println("Connected.");

			// Get folder
			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_ONLY); // ?? for imap, too?

			// Get directory
			Message messages[] = folder.getMessages();

			mailData = new MailData[messages.length];

			for (int i = 0, n = messages.length; i < n; i++) {
				String attachment = new String();

				if (messages[i].isMimeType("multipart/*")) {
					Multipart multipart = (Multipart) messages[i].getContent();

					for (int j = 1; j < multipart.getCount(); j++) {
						Part part = multipart.getBodyPart(j);
						String disp = part.getDisposition();

						if (disp != null
								&& disp.equalsIgnoreCase(Part.ATTACHMENT)
								&& part.isMimeType("application/pdf"))
							attachment = part.getFileName();
					}
				}

				mailData[i] = new MailData(messages[i].getSentDate(), String
						.valueOf(messages[i].getFrom()[0]), messages[i]
						.getSubject(), attachment);
			}

			// Close connection
			folder.close(false);
			store.close();
		} catch (Exception e) {
			System.out.println("Connection to mail server Failed");
			System.out.println(e.toString());
		}

		return mailData;
	}
}
