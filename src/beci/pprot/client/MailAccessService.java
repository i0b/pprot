package beci.pprot.client;

import beci.pprot.shared.MailData;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("mailAcess")
public interface MailAccessService extends RemoteService {
	MailData[] getMailData();
	String getMailBody(int messageNumber);
}
