package beci.pprot.client;

import beci.pprot.shared.MailData;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MailAccessServiceAsync {
	void getMailData(AsyncCallback<MailData[]> callback);
	void getMailBody(int messageNumber, AsyncCallback<String> callback);
}
