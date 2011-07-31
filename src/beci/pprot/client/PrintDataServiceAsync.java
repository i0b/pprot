package beci.pprot.client;

import beci.pprot.shared.PrintData;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PrintDataServiceAsync {
	void printFiles(PrintData data, AsyncCallback<Boolean> callback);
}