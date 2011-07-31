package beci.pprot.client;

import beci.pprot.shared.PrintData;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("printFiles")
public interface PrintDataService extends RemoteService {
	boolean printFiles(PrintData data);
}
